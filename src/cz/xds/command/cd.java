package cz.xds.command;

import cz.xds.*;

import java.io.PrintStream;

/**
 * Vypisuje obsah aktualniho adresare
 */
public class cd implements Command {
    public void execute(FileSystem fs, PrintStream outStream, Object[] param) throws FileSystemException {
        if (param.length == 1) {
            outStream.println(fs.getCurrentDirectory().getFullPath());
            return;
        }

        Directory d = Path.parseDirectory(fs, (String) param[1]);
        fs.changeDirectory(d);
    }

    public String help(boolean briefOnly) {
        if (briefOnly)
            return new String("cd [dir]");

        return new String("cd - change current directory\n\tdir - path to set as current. If not given, the current " +
                "directory is printed out");
    }
}
