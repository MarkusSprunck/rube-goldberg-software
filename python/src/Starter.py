'''
Created on 14.05.2016

@author: Markus Sprunck
'''

class Starter(object):
   
    def __init__(self, message):
        self.message = message
        
    def sayHello(self):
        print self.message
        
if __name__ == '__main__':
    starter = Starter("Hello, from Python")
    starter.sayHello()