package cz.xds.command;

import cz.xds.*;

import java.io.PrintStream;

/**
 * Created by IntelliJ IDEA.
 * User: Va�ek
 * Date: 12.12.2004
 * Time: 21:45:47
 *
 * zkou�ka p�id�n� ��dku
 */
public class pwd implements Command {
	public void execute(FileSystem fs, PrintStream outStream, Object[] param) {
        outStream.println(fs.getCurrentDirectory().getFullPath());
    }
}
