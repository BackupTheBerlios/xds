package cz.xds.command;

import cz.xds.Command;
import cz.xds.FileSystem;

import java.io.PrintStream;

/**
 * Created by IntelliJ IDEA.
 * User: Va�ek
 * Date: 12.12.2004
 * Time: 21:45:47
 * <p/>
 * zkou�ka p�id�n� ��dku
 */
public class pwd implements Command {
    public void execute(FileSystem fs, PrintStream outStream, Object[] param) {
        outStream.println(fs.getCurrentDirectory().getFullPath());
    }

    public String help(boolean briefOnly) {
        return new String(briefOnly ? "pwd has no switches" : "pwd - print current directory. No switches.");
    }
}
