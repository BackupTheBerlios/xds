package cz.xds.command;

import cz.xds.*;

/**
 * Created by IntelliJ IDEA.
 * User: Va�ek
 * Date: 12.12.2004
 * Time: 21:45:47
 *
 * zkou�ka p�id�n� ��dku
 */
public class pwd implements Command {
	public void execute(Object[] param) {
        System.out.println(((FileSystem)param[0]).getCurrentDirectory().getFullPath());
    }
}
