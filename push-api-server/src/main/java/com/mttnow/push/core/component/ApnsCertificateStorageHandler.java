package com.mttnow.push.core.component;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * This class handles actions necessary for certificate storage.
 */

@Component
public class ApnsCertificateStorageHandler {

    public static String PUSH_DIR= "ios.certificate.path";

    /**
     * Gives the path to the application's certificate storage
     * @param appId
     * @return
     * @throws ConfigurationException
     */
    public String getApplicationCertificateStorage(String appId) throws ConfigurationException {
        return (new PropertiesConfiguration("push.properties")).getString(PUSH_DIR) + appId;
    }

    /**
     * Method executed by Spring when this class is first called. (bean initialization)
     * Assures that at least the last folder of the storage path for certificates exists.
     */
    public void init() throws ConfigurationException {
        String iosCertificatePath = (new PropertiesConfiguration("push.properties")).getString(PUSH_DIR);
        File appDirFile = new File(iosCertificatePath);
        if(!appDirFile.exists()){
            appDirFile.mkdir();
        }
    }
}
