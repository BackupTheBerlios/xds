package cz.xds;

/**
 * Date: 13.12.2004
 * Time: 2:17:51
 */
public class AccessException extends FileSystemException {
    public AccessException(String s) {
        super("Access to resource " + s + " has been denied");
    }
}
