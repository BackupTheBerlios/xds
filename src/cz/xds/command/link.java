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

        // TODO Support links in foreign directories
        String linkName = (String) param[2], linkTarget = (String) param[1];
        Directory dir = fs.getCurrentDirectory();

        int index = linkTarget.lastIndexOf(Path.PATH_SEPARATOR);

        if (index != -1) {
            dir = Path.parseDirectory(fs, linkTarget.substring(0, index + 1));
            linkTarget = linkTarget.substring(index + 1);
        }

        Iterator it = dir.getIterator();

        while (it.hasNext()) {
            FileSystemItem fsi = (FileSystemItem) it.next();
            if (fsi.getName().equals(linkTarget)) {
                fsi.createLink(linkName);
                return;
            }
        }
        throw new FileSystemException("Item not found");

        //fs.getCurrentDirectory().createLink(new Path(String)param[1]);
    }

    public String help(boolean briefOnly) {
        return new String(briefOnly ? "link target linkpath" : "link - create a symlink to given item.\n" +
                "\ttarget - target item\n\tlinkpath - path to newly created symlink");
    }


}
