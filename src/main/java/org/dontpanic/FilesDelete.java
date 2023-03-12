package org.dontpanic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Delete a directory with {@link Files#delete(Path)}
 */
public class FilesDelete implements DirectoryDeleter {

    @Override
    public void delete(File dir) throws IOException {
        Files.delete(dir.toPath());
    }
}
