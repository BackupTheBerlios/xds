package cz.xds;

/**
 * Testovaci trida
 */
public class TestClass {
    public static void main(String[] args) {
        try {
            String name = "classes\\command.zip";
            if (args.length > 1) {
                name = args[1];
            }
            System.out.println("Using command file " + name);
            java.io.File commands = new java.io.File(name);
            FileSystem fs = FileSystem.createFileSystem(commands, System.in, System.out);
            while (true) {
                fs.getPrompt();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}