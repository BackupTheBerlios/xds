package cz.xds.command;

import cz.xds.*;

/**
 * Created by IntelliJ IDEA.
 * User: Vašek
 * Date: 12.12.2004
 * Time: 21:45:47
 *
 * zkouška pøidání øádku
 */
public class pwd implements Command {
	public void execute(Object[] param) {
        System.out.println(((FileSystem)param[0]).getCurrentDirectory().getFullPath());
    }
}
