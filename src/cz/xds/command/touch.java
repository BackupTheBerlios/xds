package cz.xds.command;

import cz.xds.*;

import java.io.PrintStream;

/**
 * Vypisuje obsah aktualniho adresare
 */
public class touch implements Command {
    public void execute(FileSystem fs, PrintStream outStream, Object[] param) throws FileSystemException {
        if (param.length != 3)
            throw new FileSystemException("Bad arguments for command touch.\nUsage: touch filename filetype");

        String param1 = (String) param[1];
        Directory dir = fs.getCurrentDirectory();

        int index = param1.lastIndexOf(Path.PATH_SEPARATOR);

        if (index != -1) {
            dir = Path.parseDirectory(fs, param1.substring(0, index + 1));
            param1 = param1.substring(index + 1);
        }

        String param2 = (String) param[2];

        dir.createNewFile(param1, param2);
    }

    public String help(boolean briefOnly) {
        return new String("touch filename filetype");
        //TODO: kompletni napoveda
    }
}
