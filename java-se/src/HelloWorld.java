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
    
    private final String contentAllEncoded;
    
    private final Integer numberOfRounds;
    
    public HelloWorld(String contentAllEncoded, Integer numberOfRounds) {
        this.contentAllEncoded = contentAllEncoded;
        this.numberOfRounds = numberOfRounds;
    }
    
    private void executeProgram() {
        appendMessage(LOG_FILE, " - JavaSE - execute " + RESULT_FILE, true);
        new Thread() {
            public void run() {
                Integer remainingNumberOfRounds = (numberOfRounds-1);
                ProcessBuilder pb = new ProcessBuilder("python", RESULT_FILE, contentAllEncoded, remainingNumberOfRounds.toString());
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
        String contentPythonEncoded = new String(Base64.getDecoder().decode(contentAllEncoded), "UTF-8").split(" ")[1];
        String contentPython = new String(Base64.getDecoder().decode(contentPythonEncoded), "UTF-8");
        appendMessage(RESULT_FILE, contentPython, false);
    }
    
    private void appendMessage(String file, String message, boolean addTimeStamp) {
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
    
    public static void main(String[] args) throws IOException, InterruptedException {
        Thread.sleep(1000);
        
        final String contentAll = args[0];
        final Integer numberOfRounds = Integer.parseInt(args[1]);
        HelloWorld helloWorld = new HelloWorld(contentAll, numberOfRounds);
        helloWorld.appendMessage(LOG_FILE, " - JavaSE - Number of arguments " + args.length, true);
        helloWorld.appendMessage(LOG_FILE, " - JavaSE - Number of rounds " + numberOfRounds, true);
        if (numberOfRounds > 0) {
            helloWorld.run();
            helloWorld.appendMessage(LOG_FILE, " - JavaSE - Exit", true);
        } else {
            helloWorld.appendMessage(LOG_FILE, " - JavaSE - Stopped", true);
        }
    }
    
}
