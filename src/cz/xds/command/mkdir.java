package cz.xds.command;

import cz.xds.Command;
import cz.xds.FileSystemException;
import cz.xds.FileSystem;

import java.io.PrintStream;

/**
    Vypisuje obsah aktualniho adresare
 */
public class mkdir implements Command {
    public void execute(FileSystem fs, PrintStream outStream, Object[] param) throws FileSystemException {
        fs.getCurrentDirectory().createSubDir((String)param[1]);
    }

    public String help(boolean briefOnly) {
        return new String("mkdir dir");
        //TODO: kompletni napoveda
    }
}
