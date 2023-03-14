package org.dontpanic;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class ApacheCommonsDelete implements DirectoryDeleter {
    @Override
    public void delete(File dir) throws IOException {
        FileUtils.deleteDirectory(dir);
    }
}
