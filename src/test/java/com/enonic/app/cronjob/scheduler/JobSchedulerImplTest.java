package com.enonic.app.cronjob.scheduler;

import java.time.Duration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.enonic.app.cronjob.model.JobDescriptor;
import com.enonic.app.cronjob.model.JobDescriptors;
import com.enonic.app.cronjob.runner.JobRunner;

public class JobSchedulerImplTest
{
    private JobSchedulerImpl scheduler;

    private JobRunner runner;

    @Before
    public void setup()
    {
        this.runner = Mockito.mock( JobRunner.class );
        this.scheduler = new JobSchedulerImpl();
        this.scheduler.setRunner( this.runner );
    }

    @Test
    public void testRun()
        throws Exception
    {
        final JobDescriptor job = Mockito.mock( JobDescriptor.class );
        Mockito.when( job.nextExecution() ).thenReturn( Duration.ofMillis( 190 ) );

        final JobDescriptors jobs = new JobDescriptors();
        jobs.add( job );

        this.scheduler.schedule( jobs );

        Thread.sleep( 200 );
        Mockito.verify( this.runner, Mockito.times( 1 ) ).run( job );

        this.scheduler.unschedule( jobs );
        this.scheduler.deactivate();
    }

    @Test
    public void testRun_nothing()
    {
        final JobDescriptors jobs = new JobDescriptors();
        this.scheduler.schedule( jobs );
        this.scheduler.unschedule( jobs );
        this.scheduler.deactivate();
    }
}

