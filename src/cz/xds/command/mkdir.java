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

        String name = (String) param[1];
        Directory dir = fs.getCurrentDirectory();

        int index = name.lastIndexOf(Path.PATH_SEPARATOR);

        if (index != -1) {
            dir = Path.parseDirectory(fs, name.substring(0, index + 1));
            name = name.substring(index + 1);
        }

        dir.createSubDir(name);
    }

    public String help(boolean briefOnly) {
        return new String(briefOnly ? "mkdir dir" : "mkdir - create a new directory.\n\tdir - new directory name");
    }
}
