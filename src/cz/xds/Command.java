package cz.xds;

/**

 */
public interface Command {
      public void execute(Object[] param) throws FileSystemException;  
}
