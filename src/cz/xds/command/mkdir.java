package cz.xds.command;

import cz.xds.Command;
import cz.xds.FileSystem;
import cz.xds.FileSystemException;

import java.io.PrintStream;

/**
 * Vypisuje obsah aktualniho adresare
 */
public class mkdir implements Command {
    public void execute(FileSystem fs, PrintStream outStream, Object[] param) throws FileSystemException {
        if (param.length < 2) {
            throw new FileSystemException(new String("Invalid usage. Use: ") + help(true));
        }

        fs.getCurrentDirectory().createSubDir((String) param[1]);
    }

    public String help(boolean briefOnly) {
        return new String(briefOnly ? "mkdir dir" : "mkdir - create a new directory.\n\tdir - new directory name");
    }
}
