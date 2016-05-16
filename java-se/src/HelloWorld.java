import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;

public class HelloWorld {
    
    static final String LOG_FILE = "rube-goldberg-software.log";
    
    private static final String RESULT_FILE = "HelloWorld.py";
    
    private String contentJavaEncoded;
    private String contentPythonEncoded;
    private String contentCppEncoded;
    
    public HelloWorld(String contentJavaEncoded, String contentPythonEncoded, String contentCppEncoded) {
        this.contentJavaEncoded = contentJavaEncoded;
        this.contentPythonEncoded = contentPythonEncoded;
        this.contentCppEncoded = contentCppEncoded;
    }
    
    private void executeProgram() throws IOException {
        appendMessage(LOG_FILE, " - JavaSE - execute " + RESULT_FILE, true);
        new Thread() {
            public void run() {
                ProcessBuilder pb = new ProcessBuilder("python", RESULT_FILE, contentJavaEncoded, contentPythonEncoded, contentCppEncoded);
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
        String contentPython = new String(Base64.getDecoder().decode(contentPythonEncoded), "UTF-8");
        appendMessage(RESULT_FILE, contentPython, false);
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
        HelloWorld helloWorld = new HelloWorld(args[0], args[1], args[2]);
        helloWorld.appendMessage(LOG_FILE, " - JavaSE - Number of arguments " + args.length, true);
        helloWorld.run();
        helloWorld.appendMessage(LOG_FILE, " - JavaSE - Exit", true);
    }
    
}
