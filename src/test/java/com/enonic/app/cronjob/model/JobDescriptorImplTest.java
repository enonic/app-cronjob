package com.enonic.app.cronjob.model;

import org.junit.Test;

import com.enonic.xp.app.ApplicationKey;

import static org.junit.Assert.*;

public class JobDescriptorImplTest
{
    @Test
    public void testBuilder()
    {
        final JobDescriptorImpl.Builder builder = JobDescriptorImpl.builder();
        builder.name( "myJob" );
        builder.application( ApplicationKey.from( "foo.bar" ) );
        builder.cron( "* * * * *" );

        final JobDescriptor descriptor = builder.build();
        assertEquals( "foo.bar:myJob", descriptor.getKey() );
        assertEquals( "myJob", descriptor.getName() );
        assertEquals( "foo.bar:/jobs/myJob.js", descriptor.getScript().toString() );
        assertEquals( "foo.bar:myJob @ every minute", descriptor.getDescription() );
        assertEquals( "foo.bar:myJob", descriptor.toString() );
        assertNotNull( descriptor.nextExecution() );
    }
}
