'''
 Copyright (C) 2016, Markus Sprunck
 
 All rights reserved.
 
 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met: -
 Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer. - Redistributions in binary
 form must reproduce the above copyright notice, this list of conditions and
 the following disclaimer in the documentation and/or other materials provided
 with the distribution. - The name of its contributor may be used to endorse
 or promote products derived from this software without specific prior written
 permission.
 
 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 POSSIBILITY OF SUCH DAMAGE.
'''
 
import base64
import os
import sys
import time
import datetime
import subprocess
from subprocess import Popen

class HelloWorld(object):
    
    LOG_FILE = "HelloWorld.log";
    
    RESULT_FILE = "HelloWorld.cpp";
    
    def __init__(self, contentAll):
        self.contentAll = contentAll
        self.numberOfRounds = numberOfRounds
    
    def executeProgram(self):
        self.appendMessage(self.LOG_FILE, " - Python - execute", True);
  
        args = ['c++', self.RESULT_FILE, '-o', 'HelloWorld']
        subprocess.call(args) 
        args = ['./HelloWorld', self.contentAll, self.numberOfRounds]
        Popen(args) 
  
    def createProgram(self):
        if os.path.exists(self.RESULT_FILE):
            os.remove(self.RESULT_FILE)
            os.remove(self.RESULT_FILE[:-4])
            os.remove(self.RESULT_FILE[:-4] + ".class")
            os.remove(self.RESULT_FILE[:-4] + "$1.class")
        self.appendMessage(self.LOG_FILE, " - Python - delete " + self.RESULT_FILE 
            + "    succeeded=" 
            + str(not os.path.exists(self.RESULT_FILE)).lower(), True);
      
        self.appendMessage(self.LOG_FILE, " - Python - create "  + self.RESULT_FILE, True);
        contentCppEncoded = base64.b64decode(self.contentAll).split(' ')[2] 
        contentCpp = base64.b64decode(contentCppEncoded)
        self.appendMessage(self.RESULT_FILE, contentCpp, False);
        
    def appendMessage(self, file, message, addTimeStamp):
        f = open(file, 'a+')
        if (addTimeStamp):
            time = datetime.datetime.now()
            f.write(time.strftime("%Y-%m-%dT%H:%M:%S.%f")[:-3])
        f.write(message)
        f.write('\n')
        f.flush()
        f.close()
 
    def run(self):
        self.appendMessage(self.LOG_FILE, " - Python - Hello, World!", True);
        self.createProgram()
        self.executeProgram()
        self.appendMessage(self.LOG_FILE, " - Python - end", True);

if __name__ == '__main__':
    # wait
    time.sleep(3)
    
    contentAll = sys.argv[1]
    numberOfRounds = sys.argv[2]
    helloWorld = HelloWorld(contentAll)
    helloWorld.appendMessage(helloWorld.LOG_FILE, " - Python - round " + str(sys.argv[2]), True);
    if int(numberOfRounds) > 0:
        helloWorld.run()
        helloWorld.appendMessage(helloWorld.LOG_FILE, " - Python - exit", True);
    else:
        helloWorld.appendMessage(helloWorld.LOG_FILE, " - Python - stopped", True);
    
  

