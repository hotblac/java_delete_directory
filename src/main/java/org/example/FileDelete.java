package org.example;

import java.io.File;
import java.io.IOException;

/**
 * Delete a directory with {@link File#delete()}
 */
public class FileDelete {
    public void delete(File dir) throws IOException {
        boolean success = dir.delete();
        if (!success) throw new IOException("Failed to delete " + dir);
    }
}