package cz.xds.command;

import cz.xds.*;

import java.io.PrintStream;

/**
 * Created by IntelliJ IDEA.
 * User: Vašek
 * Date: 12.12.2004
 * Time: 21:45:47
 *
 * zkouška pøidání øádku
 */
public class pwd implements Command {
	public void execute(FileSystem fs, PrintStream outStream, Object[] param) {
        outStream.println(fs.getCurrentDirectory().getFullPath());
    }
}
