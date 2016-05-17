import base64
import os
import sys
import time
import datetime
import subprocess
from subprocess import Popen
from findertools import sleep

class HelloWorld(object):
    
    LOG_FILE = "rube-goldberg-software.log";
    
    RESULT_FILE = "HelloWorld.cpp";
    
    def __init__(self, contentAll):
        self.contentAll = contentAll
    
    def executeProgram(self):
        self.appendMessage(self.LOG_FILE, " - Python - execute", True);
  
        args = ['c++', self.RESULT_FILE, '-o', 'HelloWorld']
        subprocess.call(args) 
        args = ['./HelloWorld', self.contentAll]
        Popen(args) 
  
    def createProgram(self):
        self.appendMessage(self.LOG_FILE, " - Python - delete", True);
        if os.path.exists(self.RESULT_FILE):
            os.remove(self.RESULT_FILE)
    
        self.appendMessage(self.LOG_FILE, " - Python - create", True);
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
        f.close()
 
    def run(self):
        self.appendMessage(self.LOG_FILE, " - Python - Hello, World!", True);
        self.createProgram()
        self.executeProgram()
        self.appendMessage(self.LOG_FILE, " - Python - End", True);

if __name__ == '__main__':
    contentAll = sys.argv[1];
    helloWorld = HelloWorld(contentAll)
    helloWorld.appendMessage(helloWorld.LOG_FILE, " - Python - Number of arguments " + str(len(sys.argv) - 1), True);
    time.sleep(10)
    helloWorld.appendMessage(helloWorld.LOG_FILE, " - Python - " + str(sys.argv[1]), True);
    helloWorld.run()
    helloWorld.appendMessage(helloWorld.LOG_FILE, " - Python - Exit", True);


