package cz.xds.command;

import cz.xds.Command;
import cz.xds.FileSystem;
import cz.xds.FileSystemException;

import java.io.PrintStream;

/**
 * Vytvari vyjimku ukonceni systemu
 */
public class exit implements Command {
    public void execute(FileSystem fs, PrintStream outStream, Object[] param) throws FileSystemException {
        outStream.println("\n[exit]");

        throw new FileSystemException("exit");
    }

    public String help(boolean briefOnly) {
        return new String(briefOnly ? "exit has no switches" : "exit - exits xds system. Nothing to switch");
    }
}