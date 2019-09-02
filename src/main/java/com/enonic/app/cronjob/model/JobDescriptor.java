package com.enonic.app.cronjob.model;

import com.enonic.xp.app.ApplicationKey;
import com.enonic.xp.resource.ResourceKey;

public interface JobDescriptor
{

    String getKey();

    String getName();

    String getCron();

    ApplicationKey getApplicationKey();

    ResourceKey getScript();

}
