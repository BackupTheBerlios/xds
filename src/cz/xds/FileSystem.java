package cz.xds;

import java.io.*;
import java.util.StringTokenizer;
import java.util.Vector;
import java.lang.reflect.*;

/**
    Rozhrani umoznujici vyvoreni souboroveho systemu a praci s nim
 */
public class FileSystem {

    private Directory root;
    private Directory aktDir;
    protected BufferedReader br = new BufferedReader(new InputStreamReader(System.in));     // TODO: read from socket?

    protected FileSystem(Directory root) {
        this.root = root;
        this.aktDir = root;
    }

    public static FileSystem createFileSystem() {
        Attributes at = new Attributes (false, false);
        Directory d = new Directory(Path.PATH_SEPARATOR, null, at);
        FileSystem fs = new FileSystem(d);
        return fs;
    }

    protected Vector getCommand() throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());
        Vector retVal = new Vector();

        while (st.hasMoreTokens())
            retVal.add(st.nextToken());

        return retVal;
    }

    public void changeDirectory(Directory d) {
        aktDir = d;
    }

    public Directory getCurrentDirectory() {
        return aktDir;
    }

    public Directory getRootDirectory() {
        return root;
    }

    private class CommandClassLoader extends ClassLoader {
    }

    private ClassLoader cl = new CommandClassLoader();

    private InvocationHandler handler = new InvocationHandler() {
       public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
           String command = "cz.xds.command."+((Object[])args[2])[0];
           Class classExecute;
           try {
                classExecute = cl.loadClass(command);
           } catch (ClassNotFoundException e) {
               throw new FileSystemException ("Command not found");
           }
           Method methodExecute = classExecute.getMethod("execute", new Class[] { FileSystem.class, PrintStream.class, Object[].class } );
           Object executeObject = classExecute.newInstance();
           methodExecute.invoke(executeObject, args);
           return null;
       }
    };

    private Command ex = (Command)Proxy.newProxyInstance(Command.class.getClassLoader(), new Class[] { Command.class }, handler);

    protected void printPrompt() {
        System.out.print("XDS:"+aktDir.getFullPath()+">");
    }

    protected void dispatchCommand(Vector params) throws FileSystemException {
        ex.execute(this, System.out, params.toArray());
    }

    public void getPrompt() throws IOException {
        printPrompt();
        Vector comm = getCommand();

        try {
            dispatchCommand(comm);
        } catch (Throwable t) {
            Throwable inner = t;
            while ((inner.getCause()) != null) {
                inner = inner.getCause();
            }
            //TODO: more somphisticated system exception dump?
            if (inner.getMessage() != null && inner.getMessage().equals("exit")) {
                System.exit(0);
            }
            System.out.println(inner.getMessage());
        }
    }
}