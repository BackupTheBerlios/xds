package cz.xds.command;

import cz.xds.Command;
import cz.xds.FileSystemException;
import cz.xds.FileSystem;

/**
    Vypisuje obsah aktualniho adresare
 */
public class mkdir implements Command {
    public void execute(Object[] param) throws FileSystemException {
        ((FileSystem)param[0]).getCurrentDirectory().createSubDir((String)param[2]);
    }
}
