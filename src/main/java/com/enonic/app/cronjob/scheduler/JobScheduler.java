package com.enonic.app.cronjob.scheduler;

import com.enonic.app.cronjob.model.JobDescriptor;
import com.enonic.app.cronjob.model.JobDescriptors;

public interface JobScheduler
{
    void schedule( JobDescriptor job );

    void schedule( JobDescriptors jobs );

    void unschedule( JobDescriptors jobs );
}
