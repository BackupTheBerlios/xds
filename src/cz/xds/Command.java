package cz.xds;

import java.io.PrintStream;

/**
 * Rozhrani implementovane vsemi prikazy souboroveho systemu
 *
 * @author vsch
 */
public interface Command {
    /**
     * Metoda je reflexivne volana pri vyvolani prikazu s nazvem tridy implementujici toto rozhrani
     *
     * @param fs        Souborovy system volajici tento prikaz
     * @param outStream Vystupni proud
     * @param param     Predavane parametry
     * @throws FileSystemException
     */
    public void execute(FileSystem fs, PrintStream outStream, Object[] param) throws FileSystemException;

    public String help(boolean briefOnly);
}
