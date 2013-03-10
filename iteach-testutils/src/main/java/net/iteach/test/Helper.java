package net.iteach.test;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public final class Helper {

    public static String getResourceAsString(String path) throws IOException {
        return getResourceAsString(Helper.class, path);
    }

    public static String getResourceAsString(Object reference, String path) throws IOException {
        return getResourceAsString(reference.getClass(), path);
    }

    public static String getResourceAsString(Class<?> referenceClass, String path) throws IOException {
        InputStream in = referenceClass.getResourceAsStream(path);
        if (in == null) {
            throw new IOException("Cannot find resource at " + path);
        } else {
            try {
                return IOUtils.toString(in, "UTF-8");
            } finally {
                in.close();
            }
        }
    }

    private Helper() {
    }

}
