package com.enonic.app.cronjob.component;

import java.util.concurrent.Callable;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.enonic.app.cronjob.model.JobDescriptor;
import com.enonic.app.cronjob.model.JobDescriptors;
import com.enonic.lib.cron.model.params.ScheduleParams;
import com.enonic.lib.cron.provider.CronJobProvider;
import com.enonic.xp.context.ContextAccessor;
import com.enonic.xp.portal.script.PortalScriptService;
import com.enonic.xp.script.ScriptExports;

@Component(immediate = true)
public class CronJobManagerImpl
    implements CronJobManager
{
    private CronJobProvider cronJobProvider;

    private PortalScriptService portalScriptService;

    @Reference
    public void setCronJobProvider( final CronJobProvider cronJobProvider )
    {
        this.cronJobProvider = cronJobProvider;
        this.cronJobProvider.setContext( ContextAccessor.current() );
    }

    @Reference
    public void setPortalScriptService( final PortalScriptService portalScriptService )
    {
        this.portalScriptService = portalScriptService;
    }

    @Override
    public void schedule( JobDescriptors jobDescriptors )
    {
        if ( jobDescriptors == null )
        {
            return;
        }
        jobDescriptors.forEach( this::scheduleJob );
    }

    @Override
    public void unschedule( JobDescriptors jobDescriptors )
    {
        if ( jobDescriptors == null )
        {
            return;
        }
        jobDescriptors.forEach( job -> cronJobProvider.unschedule( job.getName() ) );
    }

    private void scheduleJob( JobDescriptor jobDescriptor )
    {
        ScheduleParams scheduleParams = createScheduleParams( jobDescriptor );

        cronJobProvider.schedule( scheduleParams );
    }

    private ScheduleParams createScheduleParams( JobDescriptor jobDescriptor )
    {
        final ScheduleParams result = new ScheduleParams();

        result.setName( jobDescriptor.getName() );
        result.setCron( jobDescriptor.getCron() );
        result.setApplicationKey( jobDescriptor.getApplicationKey().getName() );
        result.setScript( createScript( jobDescriptor ) );

        return result;
    }

    private Callable<Object> createScript( JobDescriptor jobDescriptor )
    {
        final ScriptExports scriptExports = portalScriptService.execute( jobDescriptor.getScript() );

        return () -> {
            if ( scriptExports.hasMethod( "run" ) )
            {
                scriptExports.executeMethod( "run" );
            }

            return null;
        };
    }

}
