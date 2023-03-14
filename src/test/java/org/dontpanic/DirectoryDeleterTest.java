package org.dontpanic;

import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class DirectoryDeleterTest {

    @TempDir
    File tempDir;

    static Stream<Arguments> deleters() {
        return Stream.of(
                arguments(named("FileDelete", new FileDelete())),
                arguments(named("FilesDelete", new FilesDelete())),
                arguments(named("FileWalkDelete", new FileWalkDelete())),
                arguments(named("ApacheCommonsDelete", new ApacheCommonsDelete())),
                arguments(named("SudoRmDelete", new SudoRmDelete()))
        );
    }

    @ParameterizedTest
    @MethodSource("deleters")
    void deleteEmptyDirectory(DirectoryDeleter deleter) throws Exception {
        File emptyDir = new File(tempDir, "emptyDir");
        assertTrue(emptyDir.mkdir());
        deleter.delete(emptyDir);
        assertFalse(emptyDir.exists());
    }

    @ParameterizedTest
    @MethodSource("deleters")
    void deleteNonEmptyDirectory(DirectoryDeleter deleter) throws Exception {
        File nonEmptyDir = new File(tempDir, "nonEmptyDir");
        assertTrue(nonEmptyDir.mkdir());
        File contents = new File(nonEmptyDir, "file.txt");
        assertTrue(contents.createNewFile());
        deleter.delete(nonEmptyDir);
        assertFalse(nonEmptyDir.exists());
    }

    @ParameterizedTest
    @MethodSource("deleters")
    void deleteDirectoryWithNoPermissions(DirectoryDeleter deleter) throws IOException {
        File noPermissionDirectory = new File(tempDir, "unownedDir");
        assertTrue(noPermissionDirectory.mkdir());
        Files.setPosixFilePermissions(noPermissionDirectory.toPath(), Collections.emptySet());
        deleter.delete(noPermissionDirectory);
        assertFalse(noPermissionDirectory.exists());
    }

    @ParameterizedTest
    @MethodSource("deleters")
    void deleteNonEmptyDirectoryWithNoPermissions(DirectoryDeleter deleter) throws IOException {
        File noPermissionDirectory = new File(tempDir, "unownedDir");
        assertTrue(noPermissionDirectory.mkdir());
        File contents = new File(noPermissionDirectory, "file.txt");
        assertTrue(contents.createNewFile());
        Files.setPosixFilePermissions(noPermissionDirectory.toPath(), Collections.emptySet());
        deleter.delete(noPermissionDirectory);
        assertFalse(noPermissionDirectory.exists());
    }
}