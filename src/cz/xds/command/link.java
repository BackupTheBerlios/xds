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
            throw new FileSystemException("Invalid usage of 'link'. Use: link target name");

        String linkName = (String)param[2], linkTarget = (String)param[1];
        Iterator it = fs.getCurrentDirectory().getIterator();

        while (it.hasNext()) {
            FileSystemItem fsi = (FileSystemItem)it.next();
            if (fsi.getName().equals(linkTarget)) {
                fsi.createLink(linkName);
                return;
            }
        }
        throw new FileSystemException("Item not found");

        //fs.getCurrentDirectory().createLink(new Path(String)param[1]);
    }

}
