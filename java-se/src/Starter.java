/**
 * Copyright (C) 2016, Markus Sprunck
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: -
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. - Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. - The name of its contributor may be used to endorse
 * or promote products derived from this software without specific prior written
 * permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import static java.nio.file.StandardCopyOption.*;

public class Starter {
    
    private static final String CPP_SRC_PATH = "../cpp/src/HelloWorld.cpp";
    private static final String PYTHON_SRC_PATH = "../python/src/HelloWorld.py";
    private static final String JAVA_SE_SRC_PATH = "../java-se/src/HelloWorld.java";
    
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
        Files.deleteIfExists(Paths.get("HelloWorld.log"));
        Files.deleteIfExists(Paths.get("HelloWorld.java"));
        Files.deleteIfExists(Paths.get("HelloWorld.class"));
        Files.deleteIfExists(Paths.get("HelloWorld$1.class"));
        Files.deleteIfExists(Paths.get("HelloWorld.cpp"));
        Files.deleteIfExists(Paths.get("HelloWorld"));
        Files.deleteIfExists(Paths.get("HelloWorld.py"));
        
        Thread.sleep(1000);
        
        // start
        Files.copy(Paths.get(JAVA_SE_SRC_PATH), Paths.get(System.getProperty("user.dir") + "/HelloWorld.java"), REPLACE_EXISTING);
        new ProcessBuilder("javac", "HelloWorld.java").start().waitFor();
        new ProcessBuilder("java", "HelloWorld", contentAllEncoded, "3").start();
        
    }
}
