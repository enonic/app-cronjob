package com.enonic.app.cronjob.component;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.enonic.app.cronjob.model.JobDescriptor;
import com.enonic.app.cronjob.model.JobDescriptorImpl;
import com.enonic.app.cronjob.model.JobDescriptors;
import com.enonic.lib.cron.provider.CronJobProvider;
import com.enonic.xp.app.ApplicationKey;
import com.enonic.xp.portal.script.PortalScriptService;
import com.enonic.xp.resource.ResourceKey;
import com.enonic.xp.script.ScriptExports;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class CronJobManagerImplTest
{

    @InjectMocks
    private CronJobManagerImpl instance;

    @Mock
    private CronJobProvider cronJobProvider;

    @Mock
    private PortalScriptService portalScriptService;

    private JobDescriptor descriptor;

    @Before
    public void setUp()
    {
        this.descriptor = Mockito.mock( JobDescriptor.class );
    }

    @Test
    public void testSchedule()
    {
        // prepare
        final ScriptExports scriptExports = Mockito.mock( ScriptExports.class );
        final ResourceKey scriptKey = ResourceKey.from( "foo.bar:/jobs/myJob.js" );

        final JobDescriptors descriptors = new JobDescriptors();
        descriptors.add( descriptor );

        // mock
        Mockito.when( descriptor.getApplicationKey() ).thenReturn( ApplicationKey.from( "foo.bar" ) );
        Mockito.when( descriptor.getScript() ).thenReturn( scriptKey );
        Mockito.when( portalScriptService.execute( any( ResourceKey.class ) ) ).thenReturn( scriptExports );

        // test
        instance.schedule( descriptors );

        // verify
        Mockito.verify( portalScriptService, Mockito.times( 1 ) ).execute( scriptKey );

    }

    @Test
    public void testScheduleWithoutJobDescriptors()
    {
        instance.schedule( null );
    }

    @Test
    public void testUnschedule()
    {
        // prepare
        final JobDescriptors descriptors = new JobDescriptors();
        descriptors.add( JobDescriptorImpl.builder().
            name( "myJob" ).
            application( ApplicationKey.from( "foo.bar" ) ).
            cron( "* * * * *" ).
            build() );

        // test
        instance.unschedule( descriptors );

        // verify
        Mockito.verify( cronJobProvider, Mockito.times( 1 ) ).unschedule( anyString() );
    }

    @Test
    public void testUnscheduleWithoutJobDescriptors()
    {
        instance.unschedule( null );
    }

}
