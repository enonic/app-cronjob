package com.enonic.app.cronjob.model;

import org.osgi.framework.Bundle;

public interface JobDescriptorParser
{
    JobDescriptors parse( Bundle bundle );
}
