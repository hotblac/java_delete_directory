package org.dontpanic;

import java.io.File;
import java.io.IOException;

/**
 * Delete a directory with {@link File#delete()}
 */
public class FileDelete implements DirectoryDeleter {

    @Override
    public void delete(File dir) throws IOException {
        boolean success = dir.delete();
        if (!success) throw new IOException("Failed to delete " + dir);
    }
}