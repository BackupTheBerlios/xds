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

        Path p = new Path((String) param[1], false);
        Directory dir = p.getDirectory(fs);

        FileSystemItem target = dir.findItem(p.getItemName());
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
