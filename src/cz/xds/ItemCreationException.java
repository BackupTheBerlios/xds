package cz.xds;

/**
 * Vyjimka, ktera muze nastat pri vytvareni nove polozky 
 */
public class ItemCreationException extends FileSystemException {
    public ItemCreationException(String s) {
        super(s);
    }
}
