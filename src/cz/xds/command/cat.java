package cz.xds.command;

import cz.xds.*;

import java.io.PrintStream;

/**
 * Vypisuje textovy obsah souboru
 */
public class cat implements Command {
    public void execute(FileSystem fs, PrintStream outStream, Object[] param) throws FileSystemException {
        if (param.length != 2)
            throw new FileSystemException("Bad arguments for command cat.\nUsage: cat filename");

        Path p = new Path((String) param[1], false);
        Directory dir = p.getDirectory(fs);

        FileSystemItem f = dir.findItem(p.getItemName());
        if (f instanceof Link) {
            f = ((Link) f).getTarget();
        }
        if (f instanceof File) {
            outStream.println(new String(((File) f).getData()));
        } else {
            outStream.println("Argument is not a file");
        }

    }

    public String help(boolean briefOnly) {
        return new String(briefOnly ? "cat filename" : "cat - view file content.\n" +
                "\tfilename - name of file to view");
    }

}
