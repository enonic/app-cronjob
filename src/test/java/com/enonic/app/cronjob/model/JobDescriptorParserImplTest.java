package com.enonic.app.cronjob.model;

import java.io.File;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;
import org.osgi.framework.Bundle;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import static org.junit.Assert.*;

public class JobDescriptorParserImplTest
{
    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();

    private JobDescriptorParserImpl parser;

    private Bundle bundle;

    @Before
    public void setup()
    {
        this.parser = new JobDescriptorParserImpl();
        this.bundle = Mockito.mock( Bundle.class );
        Mockito.when( this.bundle.getSymbolicName() ).thenReturn( "foo.bar" );
    }

    private void writeJobsXml( final String xml )
        throws Exception
    {
        final File file = this.temporaryFolder.newFile( "jobs.xml" );
        Files.write( xml, file, Charsets.UTF_8 );

        Mockito.when( this.bundle.getResource( "/jobs/jobs.xml" ) ).thenReturn( file.toURI().toURL() );
    }

    @Test
    public void parse()
        throws Exception
    {
        writeJobsXml( "<jobs><job name=\"myJob\" cron=\"* * * * *\"/></jobs>" );

        final JobDescriptors jobs = this.parser.parse( this.bundle );
        assertNotNull( jobs );
        assertEquals( 1, jobs.size() );

        final JobDescriptor job = jobs.get( 0 );
        assertEquals( "myJob", job.getName() );
        assertEquals( "foo.bar:myJob", job.toString() );
    }

    @Test
    public void parse_noName()
        throws Exception
    {
        writeJobsXml( "<jobs><job cron=\"* * * * *\"/></jobs>" );

        final JobDescriptors jobs = this.parser.parse( this.bundle );
        assertNotNull( jobs );
        assertEquals( 0, jobs.size() );
    }

    @Test
    public void parse_noCron()
        throws Exception
    {
        writeJobsXml( "<jobs><job name=\"myJob\"/></jobs>" );

        final JobDescriptors jobs = this.parser.parse( this.bundle );
        assertNotNull( jobs );
        assertEquals( 0, jobs.size() );
    }

    @Test
    public void parse_illegalXml()
        throws Exception
    {
        writeJobsXml( "<a>" );

        final JobDescriptors jobs = this.parser.parse( this.bundle );
        assertNull( jobs );
    }

    @Test
    public void parse_noJobsXml()
    {
        final JobDescriptors jobs = this.parser.parse( this.bundle );
        assertNull( jobs );
    }
}
