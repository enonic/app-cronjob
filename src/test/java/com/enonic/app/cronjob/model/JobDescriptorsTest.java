package com.enonic.app.cronjob.model;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class JobDescriptorsTest
{
    @Test
    public void testSimple()
    {
        final JobDescriptor descriptor = Mockito.mock( JobDescriptor.class );

        final JobDescriptors descriptors = new JobDescriptors();
        descriptors.add( descriptor );
        assertEquals( 1, descriptors.size() );
    }
}
