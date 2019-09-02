package com.enonic.app.cronjob;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enonic.app.cronjob.component.CronJobManager;
import com.enonic.app.cronjob.model.JobDescriptorParser;
import com.enonic.app.cronjob.model.JobDescriptorParserImpl;
import com.enonic.app.cronjob.model.JobDescriptors;

@Component(immediate = true)
public final class AppListener
    implements BundleTrackerCustomizer<JobDescriptors>
{

    private final static Logger LOG = LoggerFactory.getLogger( AppListener.class );

    private BundleTracker<JobDescriptors> tracker;

    private CronJobManager cronJobManager;

    protected JobDescriptorParser jobDescriptorParser;

    @Reference
    public void setCronJobManager( final CronJobManager cronJobManager )
    {
        this.cronJobManager = cronJobManager;
    }

    @Activate
    public void activate( final BundleContext context )
    {
        this.jobDescriptorParser = new JobDescriptorParserImpl();
        this.tracker = new BundleTracker<>( context, Bundle.ACTIVE, this );
        this.tracker.open();
    }

    @Deactivate
    public void deactivate()
    {
        this.tracker.close();
    }

    @Override
    public JobDescriptors addingBundle( final Bundle bundle, final BundleEvent event )
    {
        final JobDescriptors jobs = this.jobDescriptorParser.parse( bundle );
        if ( jobs != null )
        {
            LOG.debug( String.format( "Schedule the %d jobs for %s bundle.", jobs.size(), bundle.getSymbolicName() ) );
            cronJobManager.schedule( jobs );
        }

        return jobs;
    }

    @Override
    public void modifiedBundle( final Bundle bundle, final BundleEvent event, final JobDescriptors jobs )
    {
        // Do nothing
    }

    @Override
    public void removedBundle( final Bundle bundle, final BundleEvent event, final JobDescriptors jobs )
    {
        LOG.debug( String.format( "Unschedule the %d jobs for %s bundle.", jobs.size(), bundle.getSymbolicName() ) );
        cronJobManager.unschedule( jobs );
    }

}
