package org.dontpanic;

import java.io.File;
import java.io.IOException;

public interface DirectoryDeleter {

    void delete(File dir) throws IOException;
}
