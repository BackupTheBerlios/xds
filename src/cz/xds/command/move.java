package cz.xds.command;

import cz.xds.*;

import java.io.PrintStream;

/**
 * Presouva polozku do jineho adresare
 */
public class move implements Command {
    public void execute(FileSystem fs, PrintStream outStream, Object[] param) throws FileSystemException {
        if (param.length < 3) {
            throw new FileSystemException(new String("Invalid usage. Use: ") + help(true));
        }

        String name = (String) param[1];
        Directory dir = fs.getCurrentDirectory();
        Directory destDir = fs.getCurrentDirectory();

        int index = name.lastIndexOf(Path.PATH_SEPARATOR);
        if (index != -1) {
            dir = Path.parseDirectory(fs, name.substring(0, index + 1));
            name = name.substring(index + 1);
        }

        destDir = Path.parseDirectory(fs, (String) param[2]);
        FileSystemItem toMove = dir.findItem(name);
        toMove.move(destDir);
    }

    public String help(boolean briefOnly) {
        return new String(briefOnly ? "move source dest" : "move - moves item to another directory\n\t");
    }
}
