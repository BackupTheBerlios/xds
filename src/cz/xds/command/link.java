package cz.xds.command;

import cz.xds.*;

import java.io.PrintStream;
import java.util.Iterator;

/**
 * Date: 13.12.2004
 * Time: 0:02:50
 *
 * @author Vasek
 */
public class link implements Command {
    public void execute(FileSystem fs, PrintStream outStream, Object[] param) throws FileSystemException {
        if (param.length < 3)
            throw new FileSystemException(new String("Invalid usage of 'link'. Use: ") + help(true));

        Path linkTargetPath = new Path((String) param[1], false), linkPath = new Path((String) param[2], false);
        Directory srcDir = linkTargetPath.getDirectory(fs);
        Directory destDir = linkPath.getDirectory(fs);

        FileSystemItem target = srcDir.findItem(linkTargetPath.getItemName());
        if (target != null) {
            target.createLink(destDir, linkPath.getItemName());
            return;
        }
        throw new FileSystemException("Item not found");
    }

    public String help(boolean briefOnly) {
        return new String(briefOnly ? "link target linkpath" : "link - create a symlink to given item.\n" +
                "\ttarget - target item\n\tlinkpath - path to newly created symlink");
    }


}
