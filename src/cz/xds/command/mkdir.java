package cz.xds.command;

import cz.xds.*;

import java.io.PrintStream;

/**
 * Vypisuje obsah aktualniho adresare
 */
public class mkdir implements Command {
    public void execute(FileSystem fs, PrintStream outStream, Object[] param) throws FileSystemException {
        if (param.length < 2) {
            throw new FileSystemException(new String("Invalid usage. Use: ") + help(true));
        }

        Path p = new Path((String) param[1], false);
        Directory dir = p.getDirectory(fs);

        dir.createSubDir(p.getItemName());
    }

    public String help(boolean briefOnly) {
        return new String(briefOnly ? "mkdir dir" : "mkdir - create a new directory.\n\tdir - new directory name");
    }
}
