package cz.xds.command;

import cz.xds.*;

import java.io.PrintStream;

/**
 * Date: 13.12.2004
 * Time: 2:19:09
 */
public class chmod implements Command {
    public void execute(FileSystem fs, PrintStream outStream, Object[] param) throws FileSystemException {
        if (param.length != 3)
            throw new FileSystemException(new String("Invalid usage. Use: ") + help(true));

        String name = (String) param[1];
        Directory dir = fs.getCurrentDirectory();

        int index = name.lastIndexOf(Path.PATH_SEPARATOR);

        if (index != -1) {
            dir = Path.parseDirectory(fs, name.substring(0, index + 1));
            name = name.substring(index + 1);
        }

        Attributes attrSet = new Attributes((String) param[2]);

        FileSystemItem fsi = dir.findItem(name);
        if (fsi != null)
            fsi.setAttributes(attrSet);
    }

    public String help(boolean briefOnly) {
        return new String(briefOnly ? "chmod itemname {rh}|{-}" : "chmod - change item attributes\n" +
                "\titemname - name of the item\n\tattributes - attribute string\n\t\tr = read only\n\t\th = hidden" +
                "\n\t\t- = no attributes at all");
    }
}
