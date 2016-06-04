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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;

public class HelloWorld {
    
    static final String LOG_FILE = "HelloWorld.log";
    
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
                Integer remainingNumberOfRounds = (numberOfRounds - 1);
                ProcessBuilder pb = new ProcessBuilder("python", RESULT_FILE, contentAllEncoded,
                        remainingNumberOfRounds.toString());
                try {
                    pb.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    
    private void createProgram() throws IOException {
        boolean succeeded = Files.deleteIfExists(Paths.get(RESULT_FILE));
        appendMessage(LOG_FILE,
                " - JavaSE - delete  " + RESULT_FILE + "    succeeded=" + succeeded, true);
        appendMessage(LOG_FILE, " - JavaSE - create  " + RESULT_FILE, true);
        String contentPythonEncoded = new String(Base64.getDecoder().decode(contentAllEncoded),
                "UTF-8").split(" ")[1];
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
            out.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    
    private void run() throws IOException {
        appendMessage(LOG_FILE, " - JavaSE - Hello, World!", true);
        createProgram();
        executeProgram();
        appendMessage(LOG_FILE, " - JavaSE - end", true);
    }
    
    public static void main(String[] args) throws IOException, InterruptedException {
        // wait
        Thread.sleep(3000);
        
        final String contentAll = args[0];
        final Integer numberOfRounds = Integer.parseInt(args[1]);
        HelloWorld helloWorld = new HelloWorld(contentAll, numberOfRounds);
        helloWorld.appendMessage(LOG_FILE, " - JavaSE - round " + numberOfRounds, true);
        if (numberOfRounds > 0) {
            helloWorld.run();
            helloWorld.appendMessage(LOG_FILE, " - JavaSE - exit", true);
        } else {
            helloWorld.appendMessage(LOG_FILE, " - JavaSE - stopped", true);
        }
    }
    
}
