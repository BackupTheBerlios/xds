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
            final String fileName = name;

            System.out.println("Using command file " + name);
            java.io.File commands = new java.io.File(fileName);
            FileSystem fs = FileSystem.createFileSystem(commands, System.in, System.out);

            while (true) {
                fs.getPrompt();
            }
            /*
                      Runnable r = new Runnable() {
                          public void run() {
                              try {
                                  ServerSocket ss = new ServerSocket(1000);
                                  while (true) {
                                      Socket soc = ss.accept();
                                      java.io.File commands = new java.io.File(fileName);
                                      Runner run = new Runner(commands, soc);
                                      Thread r = new Thread(run);
                                      r.start();
                                  }
                              } catch (IOException e) {
                                  e.printStackTrace();
                              }
                          }
                      };

                      Thread t = new Thread(r);
                      t.start();
          */
/*
            Socket soc = new Socket("127.0.0.1", 1000);
            final PrintStream out = new PrintStream(soc.getOutputStream());
            final InputStream in = soc.getInputStream();

            Runnable read = new Runnable() {
                public void run() {
                    int read = 0;
                    final int buff = 1024;
                    byte[] buffer = new byte[buff];

                    try {
                        while (true) {
                            read = System.in.read(buffer, 0, buff);
                            out.write(buffer, 0, read);
                        }
                    } catch (IOException e) {
                        //e.printStackTrace();
                    }
                }
            };
*/
/*
            Runnable write = new Runnable() {
                public void run() {
                    int read = 0;
                    final int buff = 1024;
                    byte[] buffer = new byte[buff];

                    try {
                        while (true) {
                            read = in.read(buffer, 0, buff);
                            System.out.write(buffer, 0, read);
                        }
                    } catch (IOException e) {
                        //e.printStackTrace();
                    }
                }
            };
*/
            /*
            Thread readThread = new Thread(read);
            Thread writeThread = new Thread(write);
            readThread.start();
            writeThread.start();
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/*
class Runner implements Runnable {
    java.io.File commands;
    Socket soc;

    public Runner(java.io.File commands, Socket soc) {
        this.commands = commands;
        this.soc = soc;
    }

    public void run() {
        try {
            FileSystem fs = FileSystem.createFileSystem(commands, soc.getInputStream(), new PrintStream(soc.getOutputStream()));
            while (true) {
                fs.getPrompt();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
*/