package com.enonic.app.cronjob.model;

import com.enonic.xp.app.ApplicationKey;
import com.enonic.xp.resource.ResourceKey;

public final class JobDescriptorImpl
    implements JobDescriptor
{
    private final String key;

    private final String cron;

    private final String name;

    private final ApplicationKey application;

    private final ResourceKey script;

    private JobDescriptorImpl( final Builder builder )
    {
        this.name = builder.name;
        this.cron = builder.cron;
        this.application = builder.application;
        this.key = builder.application.toString() + ":" + this.name;
        this.script = ResourceKey.from( builder.application, "/jobs/" + this.name + ".js" );
    }

    @Override
    public String getKey()
    {
        return key;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public String getCron()
    {
        return cron;
    }

    @Override
    public ApplicationKey getApplicationKey()
    {
        return application;
    }

    @Override
    public ResourceKey getScript()
    {
        return script;
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

        public JobDescriptorImpl build()
        {
            return new JobDescriptorImpl( this );
        }
    }

    public static Builder builder()
    {
        return new Builder();
    }
}
