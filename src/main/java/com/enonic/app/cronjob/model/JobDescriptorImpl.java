package com.enonic.app.cronjob.model;

import java.time.Duration;

import com.enonic.xp.app.ApplicationKey;
import com.enonic.xp.resource.ResourceKey;

final class JobDescriptorImpl
    implements JobDescriptor
{
    private final String key;

    private final String name;

    private final CronTrigger trigger;

    private final ResourceKey script;

    private JobDescriptorImpl( final Builder builder )
    {
        this.name = builder.name;
        this.trigger = CronTrigger.from( builder.cron );
        this.key = builder.application.toString() + ":" + this.name;
        this.script = ResourceKey.from( builder.application, "/jobs/" + this.name + ".js" );
    }

    @Override
    public String getKey()
    {
        return this.key;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public Duration nextExecution()
    {
        return this.trigger.nextExecution();
    }

    @Override
    public ResourceKey getScript()
    {
        return this.script;
    }

    @Override
    public String getDescription()
    {
        return this.key + " @ " + this.trigger.toString();
    }


    @Override
    public String toString()
    {
        return this.key;
    }

    static final class Builder
    {
        private ApplicationKey application;

        private String name;

        private String cron;

        Builder application( final ApplicationKey application )
        {
            this.application = application;
            return this;
        }

        Builder name( final String name )
        {
            this.name = name;
            return this;
        }

        Builder cron( final String cron )
        {
            this.cron = cron;
            return this;
        }

        JobDescriptorImpl build()
        {
            return new JobDescriptorImpl( this );
        }
    }

    static Builder builder()
    {
        return new Builder();
    }
}
