package cz.xds.command;

import cz.xds.*;

import java.io.PrintStream;

/**
 * Vymaze polozku
 */
public class delete implements Command {
    public void execute(FileSystem fs, PrintStream outStream, Object[] param) throws FileSystemException {
        if (param.length < 2) {
            throw new FileSystemException(new String("Invalid usage. Use : " + help(true)));
        }

        String name = (String) param[1];
        Directory dir = fs.getCurrentDirectory();

        int index = name.lastIndexOf(Path.PATH_SEPARATOR);

        if (index != -1) {
            dir = Path.parseDirectory(fs, name.substring(0, index + 1));
            name = name.substring(index + 1);
        }

        FileSystemItem target = dir.findItem(name);
        if (target != null) {
            // Prece si nesmazu adresar ve ktere jsem, ale asi by ten kod nemel byt tady... :)
            Directory tmp = fs.getCurrentDirectory().getParent();
            if (fs.getCurrentDirectory() != fs.getRootDirectory()) {
                while (tmp != null) {
                    if (tmp == dir) throw new FileSystemException("Can't delete actual directory");
                    tmp = tmp.getParent();
                }
            }

            target.delete();
            return;
        }
        throw new FileSystemException("Item not found");
    }

    public String help(boolean briefOnly) {
        return new String(briefOnly ? "delete itemname" : "delete - remove item from xds\n" +
                "\titemname - item name. May be link, file or directory (which is deleted recursively)");
    }
}
