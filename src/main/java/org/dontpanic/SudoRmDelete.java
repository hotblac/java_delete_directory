package org.dontpanic;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Execute a shell command to delete a directory.
 * Extremely smelly and vulnerable to nasty attacks if the directory name is not validated.
 * Requires application to run as root or a user with sudo privileges.
 * Sudo access must be granted without password by removing password requirement from /etc/sudoers.
 * DON'T DO THIS!!
 */
public class SudoRmDelete implements DirectoryDeleter {
    @Override
    public void delete(File dir) throws IOException {
        Process proc = null;
        try {
            proc = new ProcessBuilder("sudo", "rm", "-rf", dir.getPath()).start();
            boolean done = proc.waitFor(10, TimeUnit.SECONDS);
            if (proc.exitValue() != 0) {
                throw new IOException("Non-zero exit code");
            }
            if (!done) {
                proc.destroyForcibly();
            }
        } catch (InterruptedException e)  {
            Thread.currentThread().interrupt();
            throw new IOException(e);
        } finally {
            if (proc != null) proc.destroyForcibly();
        }
    }
}
