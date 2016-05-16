import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class HelloWorld {
    
    static final String LOG_FILE = "rube-goldberg-software.log";
    
    private static final String RESULT_FILE = "HelloWorld.py";
    
    private void executeProgram() throws IOException {
        appendMessage(LOG_FILE, " - JavaSE - execute " + RESULT_FILE, true);
        new Thread() {
            public void run() {
                ProcessBuilder pb = new ProcessBuilder("python", RESULT_FILE);
                try {
                    pb.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    
    private void createProgram() throws IOException {
        appendMessage(LOG_FILE, " - JavaSE - delete  " + RESULT_FILE, true);
        Files.deleteIfExists(Paths.get(RESULT_FILE));
        
        appendMessage(LOG_FILE, " - JavaSE - create  " + RESULT_FILE, true);
        String content = new String(Files.readAllBytes(Paths.get("../java-se/src/template_python.txt")));
        appendMessage(RESULT_FILE, content, false);
    }
    
    private void appendMessage(String file, String message, boolean addTimeStamp) throws IOException {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
            if (addTimeStamp) {
                LocalDateTime time = LocalDateTime.now();
                out.print(time.toString());
            }
            out.println(message);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    
    private void run() throws IOException {
        appendMessage(LOG_FILE, " - JavaSE - Hello, World!", true);
        createProgram();
        executeProgram();
        appendMessage(LOG_FILE, " - JavaSE - End", true);
    }
    
    public static void main(String[] args) throws IOException {
        HelloWorld helloWorld = new HelloWorld();
        helloWorld.appendMessage(LOG_FILE, " - JavaSE - Start with #args=" + args.length, true);
        helloWorld.run();
        helloWorld.appendMessage(LOG_FILE, " - JavaSE - Exit", true);
    }
    
}
