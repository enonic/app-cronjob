package com.enonic.app.cronjob.scheduler;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import com.enonic.app.cronjob.model.JobDescriptor;
import com.enonic.app.cronjob.model.JobDescriptors;
import com.enonic.app.cronjob.runner.JobRunner;

@Component(immediate = true)
public final class JobSchedulerImpl
    implements JobScheduler
{
    private final static Logger LOG = LoggerFactory.getLogger( JobSchedulerImpl.class );

    private final Timer timer;

    private final Map<JobDescriptor, TimerTask> tasks;

    private JobRunner runner;

    public JobSchedulerImpl()
    {
        this.timer = new Timer( "JobScheduler" );
        this.tasks = Maps.newHashMap();
    }

    @Deactivate
    public void deactivate()
    {
        this.timer.cancel();
        this.tasks.clear();
    }

    @Override
    public void schedule( final JobDescriptors jobs )
    {
        for ( final JobDescriptor job : jobs )
        {
            LOG.info( "Added job " + job.getDescription() );
            schedule( job );
        }
    }

    @Override
    public void unschedule( final JobDescriptors jobs )
    {
        jobs.forEach( this::unschedule );
    }

    @Override
    public void schedule( final JobDescriptor job )
    {
        final long delay = job.getTrigger().nextExecution().toMillis();
        final JobExecutionTask task = new JobExecutionTask( job, this, this.runner );

        this.tasks.put( job, task );
        this.timer.schedule( task, delay );
    }

    private void unschedule( final JobDescriptor job )
    {
        final TimerTask task = this.tasks.remove( job );
        if ( task != null )
        {
            task.cancel();
        }
    }

    @Reference
    public void setRunner( final JobRunner runner )
    {
        this.runner = runner;
    }
}
