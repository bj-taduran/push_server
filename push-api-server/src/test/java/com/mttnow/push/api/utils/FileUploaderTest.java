package com.mttnow.push.api.utils;

import com.mttnow.push.core.component.ApnsCertificateStorageHandler;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(PowerMockRunner.class)
@PrepareForTest(FileUtils.class)
public class FileUploaderTest {
    @Test
    public void testSaveUploadedFile() throws Exception {
        ApnsCertificateStorageHandler handler = new ApnsCertificateStorageHandler();
        String dir = handler.getApplicationCertificateStorage(Long.toString(System.currentTimeMillis()));
        String expected = dir+System.getProperty("file.separator")+"Sample.txt";
        String response = FileUploader.saveUploadedFile(dir, "Sample.txt", "Hello World".getBytes());

        assertEquals(expected, response);
        assertTrue(FileUtils.deleteQuietly(new File(expected)));
        FileUtils.deleteDirectory(new File(dir));
    }
}
