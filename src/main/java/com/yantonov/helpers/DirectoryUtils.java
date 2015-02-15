package com.yantonov.helpers;

import java.io.File;
import java.io.IOException;

public class DirectoryUtils {
    public static void deleteRecursive(File path) throws IOException {
        if (!path.exists())
            return;

        if (path.isDirectory())
            for (File f : path.listFiles())
                deleteRecursive(f);

        if (!path.delete())
            throw new IOException("Failed to delete: " + path);
    }
}
