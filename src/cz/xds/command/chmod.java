package cz.xds.command;

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

        String name = (String) param[1];
        Iterator it = fs.getCurrentDirectory().getIterator();
        Attributes attrSet = new Attributes((String) param[2]);

        while (it.hasNext()) {
            FileSystemItem fsi = (FileSystemItem) it.next();
            if (fsi.getName().equals(name)) {
                fsi.setAttributes(attrSet);
                return;
            }
        }
        throw new FileSystemException("Item not found");
    }

    public String help(boolean briefOnly) {
        return new String(briefOnly ? "chmod itemname {rh}|{-}" : "chmod - change item attributes\n" +
                "\titemname - name of the item\n\tattributes - attribute string\n\t\tr = read only\n\t\th = hidden" +
                "\n\t\t- = no attributes at all");
    }
}
