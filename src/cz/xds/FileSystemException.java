package cz.xds;

/**
    Obecna vyjimka souboroveho systemu
 */
public class FileSystemException extends Exception {
    public FileSystemException(String s) {
        super(s);
    }
}
