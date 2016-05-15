import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class HelloWorld {
    
    private static final String LOG_FILE = "../result/rube-goldberg-software.log";
    private static final String RESULT_FILE = "../python/src/HelloWorld.py";
    static {
        try {
            Files.deleteIfExists(Paths.get(LOG_FILE));
            Files.deleteIfExists(Paths.get(RESULT_FILE));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void main(String[] args) throws IOException {
        appendMessage(LOG_FILE, " - JavaSE - Start Hello, World!", true);
        createPythonProgram();
        executePythonProgram();
        appendMessage(LOG_FILE, " - JavaSE - End", true);
    }
    
    private static void executePythonProgram() throws IOException {
        appendMessage(LOG_FILE, " - JavaSE - execute " + RESULT_FILE, true);
        
        ProcessBuilder pb = new ProcessBuilder("python", RESULT_FILE);
        Process p = pb.start();
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        
        appendMessage(LOG_FILE, " - JavaSE - result  " + RESULT_FILE + " is '" + in.readLine() + "'", true);
    }
    
    private static void createPythonProgram() throws IOException {
        appendMessage(LOG_FILE, " - JavaSE - create  "+ RESULT_FILE, true);
        
        appendMessage(RESULT_FILE, "print 'Hello, World!'", false);
    }
    
    private static void appendMessage(String file, String message, boolean addTimeStamp) throws IOException {
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
}
