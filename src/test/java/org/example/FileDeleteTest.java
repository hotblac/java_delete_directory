package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileDeleteTest {

    @TempDir
    File tempDir;

    private final FileDelete fileDelete = new FileDelete();

    @Test
    void deleteEmptyDirectory() throws Exception {
        File emptyDir = new File(tempDir, "emptyDir");
        assertTrue(emptyDir.mkdir());
        fileDelete.delete(emptyDir);
        assertFalse(emptyDir.exists());
    }

    @Test
    void deleteNonEmptyDirectory() throws Exception {
        File nonEmptyDir = new File(tempDir, "nonEmptyDir");
        assertTrue(nonEmptyDir.mkdir());
        File contents = new File(nonEmptyDir, "file.txt");
        assertTrue(contents.createNewFile());
        fileDelete.delete(nonEmptyDir);
        assertFalse(nonEmptyDir.exists());
    }
}