package com.enonic.app.cronjob.model;

import java.time.Duration;

import org.junit.Test;

import static org.junit.Assert.*;

public class CronTriggerTest
{
    @Test
    public void testCreate()
    {
        final CronTrigger trigger = CronTrigger.from( "* * * * *" );

        final Duration duration = trigger.nextExecution();
        assertTrue( duration.getSeconds() >= 0 );

        assertEquals( "every minute", trigger.toString() );
    }
}
