package com.sbytestream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {
    public static String readTextFileContent(String filePath) throws IOException {
        String result = new String(Files.readAllBytes(Paths.get(filePath)));
        return result;
    }
}
