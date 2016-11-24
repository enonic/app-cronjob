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

import com.enonic.app.cronjob.model.JobDescriptorParser;
import com.enonic.app.cronjob.model.JobDescriptors;
import com.enonic.app.cronjob.scheduler.JobScheduler;

@Component(immediate = true)
public final class AppListener
    implements BundleTrackerCustomizer<JobDescriptors>
{
    private final static Logger LOG = LoggerFactory.getLogger( AppListener.class );

    private BundleTracker<JobDescriptors> tracker;

    private JobScheduler jobScheduler;

    @Activate
    public void activate( final BundleContext context )
    {
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
        final JobDescriptors jobs = JobDescriptorParser.parse( bundle );
        if ( jobs != null )
        {
            this.jobScheduler.schedule( jobs );
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
        this.jobScheduler.unschedule( jobs );
    }

    @Reference
    public void setJobScheduler( final JobScheduler jobScheduler )
    {
        this.jobScheduler = jobScheduler;
    }
}
