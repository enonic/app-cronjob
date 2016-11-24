package com.enonic.app.cronjob.runner;

import com.enonic.app.cronjob.model.JobDescriptor;

public interface JobRunner
{
    void run( JobDescriptor job );
}
