package cz.xds;

/**
 * Vyjimka znaci pokus o provedeni neplatne operace na polozce souboroveho systemu (napr. pokus o smazani polozky read-only.
 */
public class AccessException extends FileSystemException {
    public AccessException(String s) {
        super("Access to resource " + s + " has been denied");
    }
}
