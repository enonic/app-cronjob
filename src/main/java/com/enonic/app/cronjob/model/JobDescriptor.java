package com.enonic.app.cronjob.model;

import java.time.Duration;

import com.enonic.xp.resource.ResourceKey;

public interface JobDescriptor
{
    String getKey();

    String getName();

    ResourceKey getScript();

    String getDescription();

    Duration nextExecution();
}
