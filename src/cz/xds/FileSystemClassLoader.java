package cz.xds;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Trida umoznuje nacitani prikazu souboroveho systemu ze souboru zip
 */
public class FileSystemClassLoader extends ClassLoader {

    protected static final String prefix = "cz.xds.command.";
    protected java.io.File sourceFile;
    private ZipFile zipFile;

    /**
     * Implicitni konstruktor
     *
     * @param sourceFile Zdrojovy zip soubor s prikazy (tridy implementujici rozhrani Command)
     * @throws IOException
     */
    public FileSystemClassLoader(java.io.File sourceFile) throws IOException {
        this.sourceFile = sourceFile;
        this.zipFile = new ZipFile(sourceFile, ZipFile.OPEN_READ);
    }

    /**
     * Pretizena metoda umoznujici najit definici tridy v zip souboru
     *
     * @param name Jmeno hledane tridy
     * @return Vraci objekt tridy Class reprezentujici nalezenou tridu
     * @throws ClassNotFoundException
     */
    protected Class findClass(String name) throws ClassNotFoundException {
        if (name.startsWith(prefix)) {
            name = name.substring(prefix.length());
        } else
            throw new ClassNotFoundException();

        try {
            ZipEntry ze = zipFile.getEntry(name + ".class");

            if (ze != null) {
                InputStream in = zipFile.getInputStream(ze);
                int length = (int) ze.getSize();
                return defineClass(prefix + name, loadData(in, length), 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        throw new ClassNotFoundException();
    }

    /**
     * Nacita data ze souboru do pole byte[]
     *
     * @param in
     * @param length
     * @return
     * @throws IOException
     */
    private byte[] loadData(InputStream in, int length) throws IOException {
        byte[] data = new byte[length];
        int pos = 0;
        int max = length;
        int read;

        while ((max > 0) && (read = in.read(data, pos, max)) != -1) {
            pos += read;
            max -= read;
        }

        return data;
    }
}
