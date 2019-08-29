package com.enonic.app.cronjob;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.enonic.app.cronjob.component.CronJobManager;
import com.enonic.app.cronjob.model.JobDescriptorParser;
import com.enonic.app.cronjob.model.JobDescriptors;

import static junit.framework.TestCase.assertSame;
import static org.junit.Assert.*;

public class AppListenerTest
{
    private AppListener listener;

    private JobDescriptorParser jobDescriptorParser;

    private CronJobManager cronJobManager;

    @Before
    public void setup()
    {
        this.cronJobManager = Mockito.mock( CronJobManager.class );
        this.jobDescriptorParser = Mockito.mock( JobDescriptorParser.class );

        this.listener = new AppListener();
        this.listener.jobDescriptorParser = this.jobDescriptorParser;
        this.listener.setCronJobManager( cronJobManager );
    }

    @Test
    public void testLifecycle()
        throws Exception
    {
        final BundleContext context = Mockito.mock( BundleContext.class );

        this.listener.activate( context );
        Mockito.verify( context, Mockito.times( 1 ) ).addBundleListener( Mockito.any() );
        this.listener.deactivate();
    }

    @Test
    public void addingBundle()
    {
        final Bundle bundle = Mockito.mock( Bundle.class );
        Mockito.when( bundle.getSymbolicName() ).thenReturn( "foo.bar" );

        final JobDescriptors jobs = new JobDescriptors();
        Mockito.when( this.jobDescriptorParser.parse( bundle ) ).thenReturn( jobs );

        final JobDescriptors result = this.listener.addingBundle( bundle, null );
        assertSame( jobs, result );

        Mockito.verify( this.cronJobManager, Mockito.times( 1 ) ).schedule( jobs );
    }

    @Test
    public void addingBundle_noJobsXml()
    {
        final Bundle bundle = Mockito.mock( Bundle.class );
        Mockito.when( bundle.getSymbolicName() ).thenReturn( "foo.bar" );

        final JobDescriptors jobs = this.listener.addingBundle( bundle, null );
        assertNull( jobs );
    }

    @Test
    public void modifiedBundle()
    {
        final Bundle bundle = Mockito.mock( Bundle.class );
        final JobDescriptors jobs = new JobDescriptors();

        this.listener.modifiedBundle( bundle, null, jobs );
    }

    @Test
    public void removedBundle()
    {
        final Bundle bundle = Mockito.mock( Bundle.class );
        final JobDescriptors jobs = new JobDescriptors();

        this.listener.removedBundle( bundle, null, jobs );

        Mockito.verify( this.cronJobManager, Mockito.times( 1 ) ).unschedule( jobs );
    }
}
