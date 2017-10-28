package com.tw.metadata.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * Created by pzzheng on 10/28/17.
 */
public class ResourceUtil {
    public enum ResourceType {
        TEST_RESOURCE(TEST_RESOURCE_PATH), MAIN_RESOURCE(MAIN_RESOURCE_PATH);

        private String resourcePath;

        ResourceType(String resourcePath) {
            this.resourcePath = resourcePath;
        }


        public String path() {
            return resourcePath;
        }
    }
    public static final String TEST_RESOURCE_PATH = "src/test/resources";
    public static final String MAIN_RESOURCE_PATH = "src/main/resources";

    public static File createFileFromObject(Object object, String fileName, ResourceType resourceType) throws IOException {
        File file = new File(resourceType.path() + "/" + fileName);
        new ObjectMapper().writeValue(file, object);
        file.createNewFile();
        return file;
    }

    public static File createFileFromObject(Object object, String fileName) throws IOException {
        return createFileFromObject(object, fileName, ResourceType.MAIN_RESOURCE);
    }
}
