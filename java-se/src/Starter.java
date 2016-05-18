import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import static java.nio.file.StandardCopyOption.*;

public class Starter {
    
    private static final String CPP_SRC_PATH = "../java-se/src/HelloWorldTemplate.cpp";
    private static final String PYTHON_SRC_PATH = "../java-se/src/HelloWorldTemplate.py";
    private static final String JAVA_SE_SRC_PATH = "../java-se/src/HelloWorld.java";
    private static final String JAVA_SE_TARGET_PATH = "./HelloWorld.java";
    
    public static void main(String[] args) throws IOException, InterruptedException {
        
        // read all sources and encode
        final String contentJava = new String(Files.readAllBytes(Paths.get(JAVA_SE_SRC_PATH)));
        final String contentPython = new String(Files.readAllBytes(Paths.get(PYTHON_SRC_PATH)));
        final String contentCpp = new String(Files.readAllBytes(Paths.get(CPP_SRC_PATH)));
        final String contentJavaEncoded = Base64.getEncoder().encodeToString(contentJava.getBytes());
        final String contentPythonEncoded = Base64.getEncoder().encodeToString(contentPython.getBytes());
        final String contentCppEncoded = Base64.getEncoder().encodeToString(contentCpp.getBytes());
        final String contentAll = String.format("%s %s %s", contentJavaEncoded, contentPythonEncoded, contentCppEncoded);
        final String contentAllEncoded = Base64.getEncoder().encodeToString(contentAll.getBytes());
        System.out.print(contentAllEncoded);
        
        // cleanup
        Files.deleteIfExists(Paths.get("HelloWorld-Java.log"));
        Files.deleteIfExists(Paths.get("HelloWorld-Cpp.log"));
        Files.deleteIfExists(Paths.get("HelloWorld-Python.log"));
        Files.deleteIfExists(Paths.get("HelloWorld.java"));
        Files.deleteIfExists(Paths.get("HelloWorld.class"));
        Files.deleteIfExists(Paths.get("HelloWorld$1.class"));
        Files.deleteIfExists(Paths.get("HelloWorld.cpp"));
        Files.deleteIfExists(Paths.get("HelloWorld"));
        Files.deleteIfExists(Paths.get("HelloWorld.py"));
        
        Thread.sleep(1000);
        
        // start
        Files.copy(Paths.get(JAVA_SE_SRC_PATH), Paths.get(JAVA_SE_TARGET_PATH), REPLACE_EXISTING);
        new ProcessBuilder("javac", "HelloWorld.java").start().waitFor();
        new ProcessBuilder("java", "HelloWorld", contentAllEncoded, "10").start();
        
    }
}
