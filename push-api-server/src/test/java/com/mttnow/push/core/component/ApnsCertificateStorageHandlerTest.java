package com.mttnow.push.core.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest(value=File.class)
public class ApnsCertificateStorageHandlerTest {

    ApnsCertificateStorageHandler handler;
    String sampleAppId;
    String path;

    @Before
    public void setUp() throws ConfigurationException {
        handler = new ApnsCertificateStorageHandler();
        sampleAppId = Long.toString(System.currentTimeMillis());
        path = handler.getApplicationCertificateStorage(sampleAppId);
    }

    @Test
    public void shouldHaveDefaultStorage() throws Exception{
        assertNotNull(path);
        assertTrue(path.contains(System.getProperty("file.separator")));
    }

    @Test
    public void shouldGetCorrectStorage() throws Exception{
        String expected = (new PropertiesConfiguration("push.properties"))
                .getProperty(handler.PUSH_DIR) + sampleAppId;
        assertEquals(expected, path);
    }

    @Test
    public void testInit() throws Exception {
        ApnsCertificateStorageHandler handler = new ApnsCertificateStorageHandler();
        handler.init();
        String iosCertificatePath = (String) (new PropertiesConfiguration("push.properties")).getProperty(handler.PUSH_DIR);
        assertTrue((new File(iosCertificatePath)).exists());
    }

}
