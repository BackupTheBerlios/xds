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

        Path sourcePath = new Path((String) param[1], false), destPath = new Path((String) param[2], true);
        Directory srcDir = sourcePath.getDirectory(fs);
        Directory destDir = destPath.getDirectory(fs);

        FileSystemItem toMove = srcDir.findItem(sourcePath.getItemName());

        Directory testDir = destDir;
        while ((testDir = testDir.getParent()) != null)
            if (testDir == srcDir)
                throw new FileSystemException("Invalid move attempt");

        toMove.move(destDir);
    }

    public String help(boolean briefOnly) {
        return new String(briefOnly ? "move source dest" : "move - moves item to another directory\n\t");
    }
}
