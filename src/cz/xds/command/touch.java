package cz.xds.command;

import cz.xds.*;

import java.io.PrintStream;

/**
 * Vytvari novy soubor pozadovaneho typu
 */
public class touch implements Command {
    public void execute(FileSystem fs, PrintStream outStream, Object[] param) throws FileSystemException {
        if (param.length != 3)
            throw new FileSystemException("Bad arguments for command touch.\nUsage: touch filename filetype");

        Path p = new Path((String) param[1], false);
        Directory dir = p.getDirectory(fs);

        dir.createNewFile(p.getItemName(), (String) param[2]);
    }

    public String help(boolean briefOnly) {
        return new String(briefOnly ? "touch filename type" : "touch - create a new file.\n" +
                "\tfilename - new file name\n\ttype - new file type");
    }
}
