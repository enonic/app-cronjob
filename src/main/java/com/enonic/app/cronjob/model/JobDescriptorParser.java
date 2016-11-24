package com.enonic.app.cronjob.model;

import java.net.URL;

import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.google.common.base.Strings;

import com.enonic.xp.app.ApplicationKey;
import com.enonic.xp.xml.DomElement;
import com.enonic.xp.xml.DomHelper;

public final class JobDescriptorParser
{
    private final static Logger LOG = LoggerFactory.getLogger( JobDescriptorParser.class );

    private final ApplicationKey applicationKey;

    public JobDescriptorParser( final ApplicationKey key )
    {
        this.applicationKey = key;
    }

    public static JobDescriptors parse( final Bundle bundle )
    {
        return new JobDescriptorParser( ApplicationKey.from( bundle ) ).doParse( bundle );
    }

    private JobDescriptors doParse( final Bundle bundle )
    {
        final URL url = bundle.getResource( "/jobs/jobs.xml" );
        if ( url == null )
        {
            return null;
        }

        return doParse( url );
    }

    private JobDescriptors doParse( final URL url )
    {
        try
        {
            return doParse( DomHelper.parse( url.openStream() ) );
        }
        catch ( final Exception e )
        {
            LOG.error( "Failed to parse /jobs/jobs.xml", e );
            return null;
        }
    }

    private JobDescriptors doParse( final Document doc )
    {
        return doParse( DomElement.from( doc.getDocumentElement() ) );
    }

    private JobDescriptors doParse( final DomElement root )
    {
        final JobDescriptors list = new JobDescriptors();
        for ( final DomElement child : root.getChildren( "job" ) )
        {
            final JobDescriptor desc = parseJob( child );
            if ( desc != null )
            {
                list.add( desc );
            }
        }

        return list;
    }

    private JobDescriptor parseJob( final DomElement root )
    {
        final String name = root.getAttribute( "name" );
        if ( Strings.isNullOrEmpty( name ) )
        {
            return null;
        }

        final String cron = root.getAttribute( "cron" );
        if ( Strings.isNullOrEmpty( cron ) )
        {
            return null;
        }

        return JobDescriptor.builder().
            application( this.applicationKey ).
            name( name ).
            cron( cron ).
            build();
    }
}
