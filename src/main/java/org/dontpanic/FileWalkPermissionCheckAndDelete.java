package org.dontpanic;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

/**
 * Deletes a directory by walking all contained files and fixing permissions before deleting them.
 */
public class FileWalkPermissionCheckAndDelete implements DirectoryDeleter {

    public static final Set<PosixFilePermission> REQUIRED_PERMISSIONS = Set.of(
            PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_EXECUTE, PosixFilePermission.OWNER_WRITE);

    @Override
    public void delete(File dir) throws IOException {
        fixPermission(dir.toPath());
        Files.walkFileTree(dir.toPath(), new SimpleFileVisitor<>(){
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                File[] subdirs = dir.toFile().listFiles(File::isDirectory);
                if (subdirs == null) return FileVisitResult.CONTINUE;
                for (File subdir : subdirs) {
                    fixPermission(subdir.toPath());
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Ensure that directories have sufficient permission to be deleted.
     * That is, owner has read and execute permission (to traverse) and write (to delete contents).
     */
    private void fixPermission(Path dir) throws IOException {
        Set<PosixFilePermission> permissions = Files.getPosixFilePermissions(dir);
        if (!permissions.containsAll(REQUIRED_PERMISSIONS)) {
            permissions.addAll(REQUIRED_PERMISSIONS);
            Files.setPosixFilePermissions(dir, permissions);
        }
    }
}
