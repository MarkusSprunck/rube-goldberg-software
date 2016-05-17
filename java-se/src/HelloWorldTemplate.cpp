#include <string>
#include <iostream>
#include <fstream>
#include <ctime>
#include <sys/time.h>

using namespace std;

class HelloWorld {

public:
	static const string LOG_FILE;

public:
	HelloWorld() {
	}

	string getTime() {
		time_t now;
		time(&now);
		timeval curTime;
		gettimeofday(&curTime, NULL);
		int milli = curTime.tv_usec / 1000;
		char buf[sizeof "2011-10-08T07:07:09"];
		strftime(buf, sizeof buf, "%FT%T", localtime(&now));
		sprintf(buf, "%s.%d", buf, milli);
		return buf;
	}

	void appendMessage(std::string file, std::string message, bool addTimeStamp) {
		ofstream outfile;
		outfile.open("rube-goldberg-software.log", ios_base::app);
		outfile << getTime() << message << endl;
	}

	void run() {
		appendMessage(LOG_FILE, " - cpp    - Hello, World!", true);
		//createProgram();
		//executeProgram();
		appendMessage(LOG_FILE, " - cpp    - End", true);
	}

};

const string HelloWorld::LOG_FILE = "rube-goldberg-software.log";

int main(int argc, char *argv[]) {
	string contentAll = argv[1];
	HelloWorld* helloWorld = new HelloWorld();
	helloWorld->appendMessage(HelloWorld::LOG_FILE, string(" - cpp    - Number of arguments ") + to_string(argc - 1), true);
	helloWorld->appendMessage(HelloWorld::LOG_FILE, string(" - cpp    - ") + contentAll, true);
	helloWorld->run();
	helloWorld->appendMessage(HelloWorld::LOG_FILE, " - cpp    - Exit", true);
	return 0;
}

