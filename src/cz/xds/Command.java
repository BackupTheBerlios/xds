package cz.xds;

import java.io.*;

/**
 * @author vsch
 */
public interface Command {
      public void execute(FileSystem fs, PrintStream outStream, Object[] param) throws FileSystemException;
}
