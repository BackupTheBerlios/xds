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

    protected FileSystem(Directory root) {
        this.root = root;
        this.aktDir = root;
    }

    /**
     * Tovarna pro vyrobu souborovych systemu
     *
     * @param commandFile Soubor s prikazy, ktere ma system pouzivat pro praci
     * @param in          Vstupni stream
     * @param out         Stream, na ktery bude smerovan veskery vystup
     * @return Vytvoreny novy objekt FileSystem
     * @throws IOException
     */
    public static FileSystem createFileSystem(java.io.File commandFile, InputStream in, PrintStream out) throws IOException {
        FileSystem fs = new FileSystem(new Directory(new IDFactory()));
        fs.input = new BufferedReader(new InputStreamReader(in));
        fs.output = out;
        fs.cl = new FileSystemClassLoader(commandFile);
        return fs;
    }

    /**
     * Nacte ze vstupniho streamu prikaz
     *
     * @return Vektor parametru
     * @throws IOException
     */
    protected Vector getCommand() throws IOException {
        StringTokenizer st = new StringTokenizer(input.readLine());
        Vector retVal = new Vector();

        while (st.hasMoreTokens())
            retVal.add(st.nextToken());

        return retVal;
    }

    /**
     * Nastavi aktualni adresar
     *
     * @param d Novy aktualni adresar
     */
    public void changeDirectory(Directory d) {
        aktDir = d;
    }

    /**
     * Vraci aktualni adresar
     *
     * @return Aktualni adresar
     */
    public Directory getCurrentDirectory() {
        return aktDir;
    }

    /**
     * Vraci ukazatel na korenovy adresar (vsechny polozky v systemu jsou jeho primymi
     * nebo neprimymi potomky). Nikdy nevraci null, protoze korenovy adresar existuje
     * i u prazdneho souboroveho systemu.
     *
     * @return Ukazatel na korenovy adresar
     */
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

    /**
     * Vypise dotazovaci retezec, tzv. prompt
     */
    protected void printPrompt() {
        output.print("XDS:" + aktDir.getFullPath() + ">");
    }

    /**
     * Provede zadany prikaz
     *
     * @param params Vektor parametru nactenych napr. ze vstupniho streamu
     * @throws FileSystemException
     */
    protected void dispatchCommand(Vector params) throws FileSystemException {
        if (params.size() > 0)
            ex.execute(this, output, params.toArray());
    }

    /**
     * Jakysi pokus o emulaci konzoly pro praci se souborovym systemem. Da se prirovnat
     * k shellu UNIXu (ovsem velmi zjednodusenemu). V cyklu se zde vypisuje prompt,
     * ceka na prikaz, ktery se pote provede. Vysledek je smerovan na vystupni stream.
     *
     * @throws IOException
     */
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
                    input.close();
                    output.close();
                    //System.exit(0);
                }
                output.println(inner.getMessage());
            } else {
                output.println("Internal error: " + inner.getClass().getName());
            }
        }
    }
}