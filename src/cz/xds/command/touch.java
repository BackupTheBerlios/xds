package cz.xds.command;

import cz.xds.Command;
import cz.xds.FileSystemException;
import cz.xds.FileSystem;

/**
    Vypisuje obsah aktualniho adresare
 */
public class touch implements Command {
    public void execute(Object[] param) throws FileSystemException {
         if (param.length != 4) {
             throw new FileSystemException("Bad arguments for command touch.\nUsage: touch filename filetype");
         }

         String param1 = (String)param[2];
         String param2 = (String)param[3];

        ((FileSystem)param[0]).getCurrentDirectory().createNewFile(param1, param2);
    }
}
