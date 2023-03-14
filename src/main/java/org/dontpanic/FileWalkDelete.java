package org.dontpanic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * Deletes a directory by walking all contained files and deleting them.
 */
public class FileWalkDelete implements DirectoryDeleter {
    @Override
    public void delete(File dir) throws IOException {
        try (Stream<Path> paths = Files.walk(dir.toPath())) {
            paths.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }
}
