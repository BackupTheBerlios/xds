package cz.xds.command;

import cz.xds.Command;
import cz.xds.FileSystemException;
import cz.xds.FileSystem;

import java.io.PrintStream;

/**
    Vypisuje obsah aktualniho adresare
 */
public class touch implements Command {
    public void execute(FileSystem fs, PrintStream outStream, Object[] param) throws FileSystemException {
         if (param.length != 2)
             throw new FileSystemException("Bad arguments for command touch.\nUsage: touch filename filetype");

         String param1 = (String)param[1];
         String param2 = (String)param[2];

        fs.getCurrentDirectory().createNewFile(param1, param2);
    }
}
