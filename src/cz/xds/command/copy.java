package cz.xds.command;

import cz.xds.*;

import java.io.PrintStream;

/**
 * Date: 18.12.2004
 * Time: 18:59:55
 */
public class copy implements Command {
    public void execute(FileSystem fs, PrintStream outStream, Object[] param) throws FileSystemException {
        if (param.length < 3) {
            throw new FileSystemException(new String("Invalid usage. Use: ") + help(true));
        }

        Path sourcePath = new Path((String) param[1], false), destPath = new Path((String) param[2], true);
        Directory srcDir = sourcePath.getDirectory(fs);
        Directory destDir = destPath.getDirectory(fs);

        FileSystemItem toCopy = srcDir.findItem(sourcePath.getItemName());

        if (toCopy == null)
            throw new FileSystemException("Item not found");

        if (toCopy instanceof Directory && ((Directory)toCopy).isNondirectParentOf(destDir))
            throw new FileSystemException("Invalid copy attempt");

        toCopy.copy(destDir);
    }

    public String help(boolean briefOnly) {
        if (briefOnly)
            return new String("copy source dest");

        return new String("copy - copy source item to another directory\n\tsource - source item name." +
                "\n\tdest - target directory");
    }
}

