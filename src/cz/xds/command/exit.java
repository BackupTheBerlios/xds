package cz.xds.command;

import cz.xds.Command;
import cz.xds.FileSystemException;
import cz.xds.FileSystem;

import java.io.PrintStream;

/**
    Vytvari vyjimku ukonceni systemu
 */
public class exit implements Command {
    public void execute(FileSystem fs, PrintStream outStream, Object[] param) throws FileSystemException {
        outStream.println("\n[exit]");

        throw new FileSystemException("exit");
    }
}