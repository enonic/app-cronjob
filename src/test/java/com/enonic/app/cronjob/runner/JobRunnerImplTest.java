package com.enonic.app.cronjob.runner;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.enonic.app.cronjob.model.JobDescriptor;
import com.enonic.xp.portal.script.PortalScriptService;
import com.enonic.xp.resource.ResourceKey;
import com.enonic.xp.script.ScriptExports;

public class JobRunnerImplTest
{
    private JobRunnerImpl runner;

    private PortalScriptService scriptService;

    @Before
    public void setup()
    {
        this.scriptService = Mockito.mock( PortalScriptService.class );
        this.runner = new JobRunnerImpl();
        this.runner.setPortalScriptService( this.scriptService );
    }

    @Test
    public void testRun()
    {
        final ScriptExports exports = Mockito.mock( ScriptExports.class );

        final ResourceKey scriptKey = ResourceKey.from( "foo.bar:/jobs/myJob.js" );
        Mockito.when( this.scriptService.execute( scriptKey ) ).thenReturn( exports );

        final JobDescriptor descriptor = Mockito.mock( JobDescriptor.class );
        Mockito.when( descriptor.getScript() ).thenReturn( scriptKey );

        this.runner.run( descriptor );

        Mockito.verify( exports, Mockito.times( 1 ) ).executeMethod( "run" );
    }

    @Test
    public void testRun_exception()
    {
        final ResourceKey scriptKey = ResourceKey.from( "foo.bar:/jobs/myJob.js" );
        Mockito.when( this.scriptService.execute( scriptKey ) ).thenThrow( new RuntimeException() );

        final JobDescriptor descriptor = Mockito.mock( JobDescriptor.class );
        Mockito.when( descriptor.getScript() ).thenReturn( scriptKey );

        this.runner.run( descriptor );
    }
}
