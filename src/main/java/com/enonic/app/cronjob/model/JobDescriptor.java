package com.enonic.app.cronjob.model;

import com.enonic.xp.app.ApplicationKey;
import com.enonic.xp.resource.ResourceKey;

public final class JobDescriptor
{
    private final String key;

    private final String name;

    private final CronTrigger trigger;

    private final ResourceKey script;

    private JobDescriptor( final Builder builder )
    {
        this.name = builder.name;
        this.trigger = CronTrigger.from( builder.cron );
        this.key = builder.application.toString() + ":" + this.name;
        this.script = ResourceKey.from( builder.application, "/jobs/" + this.name + ".js" );
    }

    public String getKey()
    {
        return this.key;
    }

    public String getName()
    {
        return this.name;
    }

    public CronTrigger getTrigger()
    {
        return this.trigger;
    }

    public ResourceKey getScript()
    {
        return this.script;
    }

    public String getDescription()
    {
        return this.key + " @ " + this.trigger.toString();
    }

    @Override
    public String toString()
    {
        return this.key;
    }

    public static final class Builder
    {
        private ApplicationKey application;

        private String name;

        private String cron;

        public Builder application( final ApplicationKey application )
        {
            this.application = application;
            return this;
        }

        public Builder name( final String name )
        {
            this.name = name;
            return this;
        }

        public Builder cron( final String cron )
        {
            this.cron = cron;
            return this;
        }

        public JobDescriptor build()
        {
            return new JobDescriptor( this );
        }
    }

    public static Builder builder()
    {
        return new Builder();
    }
}
