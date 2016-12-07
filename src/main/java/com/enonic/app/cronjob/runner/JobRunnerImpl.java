package com.enonic.app.cronjob.runner;

import java.util.concurrent.TimeUnit;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;

import com.enonic.app.cronjob.model.JobDescriptor;
import com.enonic.xp.content.ContentConstants;
import com.enonic.xp.context.Context;
import com.enonic.xp.portal.script.PortalScriptService;
import com.enonic.xp.resource.ResourceKey;
import com.enonic.xp.script.ScriptExports;

@Component(immediate = true)
public final class JobRunnerImpl
    implements JobRunner
{
    private final static Logger LOG = LoggerFactory.getLogger( JobRunnerImpl.class );

    private PortalScriptService portalScriptService;

    @Override
    public void run( final JobDescriptor job )
    {
        try
        {
            LOG.info( "Executing job [" + job + "]" );

            final Stopwatch stopwatch = Stopwatch.createStarted();
            executeInContext( job.getScript() );

            LOG.info( "Executed job [" + job + "] in " + ( stopwatch.elapsed( TimeUnit.MILLISECONDS ) ) + " ms" );
        }
        catch ( final Exception e )
        {
            LOG.error( "Error executing job [" + job + "]", e );
        }
    }

    private void executeInContext( final ResourceKey key )
    {
        final Context context = ContentConstants.CONTEXT_MASTER;
        context.runWith( () -> doExecute( key ) );
    }

    private void doExecute( final ResourceKey key )
    {
        final ScriptExports exports = this.portalScriptService.execute( key );
        exports.executeMethod( "run" );
    }

    @Reference
    public void setPortalScriptService( final PortalScriptService portalScriptService )
    {
        this.portalScriptService = portalScriptService;
    }
}
