package cz.xds;

/**
    Testovaci trida
 */
public class TestClass {
    public static void main(String[] args) {
        try {
            FileSystem fs = FileSystem.createFileSystem();
            while(true) {
            fs.getPrompt();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
        d.createNewFile("MyFile", "data", null);
        Directory sub = d.createSubDir("MyDir");
        Directory sub2 = sub.createSubDir("MyDir2");
        File f = sub2.createNewFile("MyFile", "data", null);

        System.out.println(f.getFullPath());
        */
    }
}