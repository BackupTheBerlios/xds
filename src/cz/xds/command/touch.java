package cz.xds.command;

import cz.xds.*;

import java.io.PrintStream;

/**
 * Vytvari novy soubor pozadovaneho typu
 */
public class touch implements Command {
    public void execute(FileSystem fs, PrintStream outStream, Object[] param) throws FileSystemException {
        if (param.length < 3)
            throw new FileSystemException("Bad arguments for command touch.\nUsage: touch filename filetype [text content]");

        Path p = new Path((String) param[1], false);
        Directory dir = p.getDirectory(fs);

        if (param.length == 3)
            dir.createNewFile(p.getItemName(), (String) param[2]);
        if (param.length > 3) {
            StringBuffer s = new StringBuffer();
            for (int j = 3; j < param.length; j++) {
                s.append((String) param[j]).append(" ");
            }
            dir.createNewFile(p.getItemName(), (String) param[2], s.toString().getBytes());
        }
    }

    public String help(boolean briefOnly) {
        return new String(briefOnly ? "touch filename type [text content]" : "touch - create a new file.\n" +
                "\tfilename - new file name\n\ttype - new file type\n\ttext content - data of new file");
    }
}
