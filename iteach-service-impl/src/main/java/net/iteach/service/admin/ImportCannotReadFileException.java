package net.iteach.service.admin;

import net.sf.jstring.support.CoreException;

import java.io.IOException;

public class ImportCannotReadFileException extends CoreException {
    public ImportCannotReadFileException(String originalFilename, IOException ex) {
        super(ex, originalFilename, ex);
    }
}
