package cz.xds.command;

import cz.xds.Command;
import cz.xds.FileSystemException;

/**
    Vytvari vyjimku ukonceni systemu
 */
public class exit implements Command {
    public void execute(Object[] param) throws FileSystemException {
        throw new FileSystemException("exit");
    }
}