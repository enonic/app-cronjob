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

public final class JobDescriptorParserImpl
    implements JobDescriptorParser
{
    private final static Logger LOG = LoggerFactory.getLogger( JobDescriptorParserImpl.class );

    @Override
    public JobDescriptors parse( final Bundle bundle )
    {
        final URL url = bundle.getResource( "/jobs/jobs.xml" );
        if ( url == null )
        {
            return null;
        }

        final ApplicationKey key = ApplicationKey.from( bundle );
        return doParse( key, url );
    }

    private JobDescriptors doParse( final ApplicationKey key, final URL url )
    {
        try
        {
            return doParse( key, DomHelper.parse( url.openStream() ) );
        }
        catch ( final Exception e )
        {
            LOG.error( "Failed to parse /jobs/jobs.xml", e );
            return null;
        }
    }

    private JobDescriptors doParse( final ApplicationKey key, final Document doc )
    {
        return doParse( key, DomElement.from( doc.getDocumentElement() ) );
    }

    private JobDescriptors doParse( final ApplicationKey key, final DomElement root )
    {
        final JobDescriptors list = new JobDescriptors();
        for ( final DomElement child : root.getChildren( "job" ) )
        {
            final JobDescriptor desc = parseJob( key, child );
            if ( desc != null )
            {
                list.add( desc );
            }
        }

        return list;
    }

    private JobDescriptor parseJob( final ApplicationKey key, final DomElement root )
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

        return JobDescriptorImpl.builder().
            application( key ).
            name( name ).
            cron( cron ).
            build();
    }
}
