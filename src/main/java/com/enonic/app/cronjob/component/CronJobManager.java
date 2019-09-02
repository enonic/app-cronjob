package com.enonic.app.cronjob.component;

import com.enonic.app.cronjob.model.JobDescriptors;

public interface CronJobManager
{

    void schedule( JobDescriptors jobDescriptors );

    void unschedule( JobDescriptors jobDescriptors );

}
