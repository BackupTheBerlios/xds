package cz.xds.command;

import cz.xds.Command;
import cz.xds.FileSystem;

import java.util.Iterator;

/**
 * Vypisuje obsah aktualniho adresare
 */
public class dir implements Command {
    public void execute(Object[] param) {
        Iterator it = ((FileSystem) param[0]).getCurrentDirectory().getIterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }
}
