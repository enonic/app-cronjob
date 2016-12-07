package com.enonic.app.cronjob.scheduler;

import org.junit.Test;
import org.mockito.Mockito;

import com.enonic.app.cronjob.model.JobDescriptor;
import com.enonic.app.cronjob.runner.JobRunner;

public class JobExecutionTaskTest
{
    @Test
    public void runAndReSchedule()
    {
        final JobDescriptor job = Mockito.mock( JobDescriptor.class );
        final JobScheduler scheduler = Mockito.mock( JobScheduler.class );
        final JobRunner runner = Mockito.mock( JobRunner.class );

        final JobExecutionTask task = new JobExecutionTask( job, scheduler, runner );
        task.run();

        Mockito.verify( runner, Mockito.times( 1 ) ).run( job );
        Mockito.verify( scheduler, Mockito.times( 1 ) ).schedule( job );
    }
}
