package cz.xds;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Rozhrani umoznujici vyvoreni souboroveho systemu a praci s nim
 */
public class FileSystem {

    private Directory root;
    private Directory aktDir;
    private ClassLoader cl;
    protected BufferedReader input;
    protected PrintStream output;
    //protected IDFactory idFactory = ;

    protected FileSystem(Directory root) {
        //root.idFactory = idFactory;
        this.root = root;
        this.aktDir = root;
    }

    public static FileSystem createFileSystem(java.io.File commandFile, InputStream in, PrintStream out) throws IOException {
        FileSystem fs = new FileSystem(new Directory(new IDFactory()));
        fs.input = new BufferedReader(new InputStreamReader(in));
        fs.output = out;
        fs.cl = new FileSystemClassLoader(commandFile);
        return fs;
    }

    protected Vector getCommand() throws IOException {
        StringTokenizer st = new StringTokenizer(input.readLine());
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

    private InvocationHandler handler = new InvocationHandler() {
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            int paramCount = ((Object[]) args[2]).length;
            String command = "cz.xds.command." + ((Object[]) args[2])[0];
            Class classExecute;
            try {
                classExecute = cl.loadClass(command);
            } catch (ClassNotFoundException e) {
                throw new FileSystemException("Command not found");
            }

            Method methodExecute = null;
            boolean helpNeeded = false, helpType = false;

            if (paramCount > 1) {
                String secondParam = (String) (((Object[]) args[2])[1]);

                if (secondParam.equals("/?") || secondParam.equals("--help")) {
                    helpType = secondParam.equals("/?");
                    methodExecute = classExecute.getMethod("help", new Class[]{boolean.class});
                    helpNeeded = true;
                }
            }

            if (methodExecute == null)
                methodExecute = classExecute.getMethod("execute", new Class[]{FileSystem.class, PrintStream.class, Object[].class});

            Object executeObject = classExecute.newInstance();

            if (helpNeeded) {
                ((PrintStream) args[1]).println((String) methodExecute.invoke(executeObject, new Object[]{new Boolean(true)}));

                if (!helpType)
                    ((PrintStream) args[1]).println((String) methodExecute.invoke(executeObject, new Object[]{new Boolean(false)}));
            } else
                methodExecute.invoke(executeObject, args);
            return null;
        }
    };

    private Command ex = (Command) Proxy.newProxyInstance(Command.class.getClassLoader(), new Class[]{Command.class}, handler);

    protected void printPrompt() {
        output.print("XDS:" + aktDir.getFullPath() + ">");
    }

    protected void dispatchCommand(Vector params) throws FileSystemException {
        if (params.size() > 0)
            ex.execute(this, output, params.toArray());
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

            if (inner instanceof FileSystemException) {
                if (inner.getMessage() != null && inner.getMessage().equals("exit")) {
                    System.exit(0);
                }
                output.println(inner.getMessage());
            } else {
                output.println("Internal error: " + inner.getClass().getName());
            }
        }
    }
}