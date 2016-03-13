
package com.mttnow.push.api.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FileUploader {

    /**
     * @param directory The directory path on which the file will be saved
     * @param fileName The file name of the file to be saved
     * @param fileInBytes The actualy file in byte array format
     * @return the absolute path
     */
    public static String saveUploadedFile(String directory, String fileName, byte[] fileInBytes) throws IOException {
        File appDirFile = new File(directory);
        if(!appDirFile.exists()){
            appDirFile.mkdir();
        }
        String fullPath = directory+System.getProperty("file.separator")+fileName;
        FileUtils.writeByteArrayToFile(new File(fullPath), fileInBytes);

        return fullPath;
    }
}
