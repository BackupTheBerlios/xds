package cz.xds.command;

import cz.xds.Command;
import cz.xds.FileSystem;
import cz.xds.FileSystemException;
import cz.xds.*;

import java.io.PrintStream;
import java.util.Iterator;

/**
 * Date: 13.12.2004
 * Time: 2:19:09
 */
public class chmod implements Command {
    public void execute(FileSystem fs, PrintStream outStream, Object[] param) throws FileSystemException {
        if (param.length != 3)
            throw new FileSystemException(new String("Invalid usage. Use: ") + help(true));

        String name = (String)param[1];
        Iterator it = fs.getCurrentDirectory().getIterator();
        Attributes attrSet = new Attributes((String)param[2]);

        while (it.hasNext()) {
            FileSystemItem fsi = (FileSystemItem)it.next();
            if (fsi.getName().equals(name)) {
                fsi.setAttributes(attrSet);
                return;
            }
        }
        throw new FileSystemException("Item not found");
    }

    public String help(boolean briefOnly) {
        return new String("chmod file [r|h]");
        //TODO: kompletni napoveda
    }
}
