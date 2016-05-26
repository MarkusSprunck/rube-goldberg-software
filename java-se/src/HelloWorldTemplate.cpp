#include <stdio.h>
#include <sys/time.h>
#include <unistd.h>
#include <cstdlib>
#include <ctime>
#include <fstream>
#include <iostream>
#include <iterator>
#include <sstream>
#include <string>
#include <vector>

using namespace std;

class HelloWorld {

public:
	static const string LOG_FILE;

	static const string RESULT_FILE;

	static const string BASE64_CHAR;

private:

	string base64_decode(string const& encoded_string) {
		size_t in_len = encoded_string.size();
		size_t i = 0;
		size_t j = 0;
		int in_ = 0;
		unsigned char char_array_4[4], char_array_3[3];
		std::string ret;

		while (in_len-- && (encoded_string[in_] != '=')) {
			char_array_4[i++] = encoded_string[in_];
			in_++;
			if (i == 4) {
				for (i = 0; i < 4; i++)
					char_array_4[i] = static_cast<unsigned char>(BASE64_CHAR.find(char_array_4[i]));

				char_array_3[0] = (char_array_4[0] << 2) + ((char_array_4[1] & 0x30) >> 4);
				char_array_3[1] = ((char_array_4[1] & 0xf) << 4) + ((char_array_4[2] & 0x3c) >> 2);
				char_array_3[2] = ((char_array_4[2] & 0x3) << 6) + char_array_4[3];

				for (i = 0; (i < 3); i++)
					ret += char_array_3[i];
				i = 0;
			}
		}

		if (i) {
			for (j = i; j < 4; j++)
				char_array_4[j] = 0;

			for (j = 0; j < 4; j++)
				char_array_4[j] = static_cast<unsigned char>(BASE64_CHAR.find(char_array_4[j]));

			char_array_3[0] = (char_array_4[0] << 2) + ((char_array_4[1] & 0x30) >> 4);
			char_array_3[1] = ((char_array_4[1] & 0xf) << 4) + ((char_array_4[2] & 0x3c) >> 2);
			char_array_3[2] = ((char_array_4[2] & 0x3) << 6) + char_array_4[3];

			for (j = 0; (j < i - 1); j++)
				ret += char_array_3[j];
		}
		return ret;
	}

private:
	string contentAllEncoded;
	string numberOfRounds;

public:
	HelloWorld(string contentAllEncoded, string numberOfRounds) {
		this->contentAllEncoded = contentAllEncoded;
		this->numberOfRounds = numberOfRounds;
	}

	void executeProgram() {
		appendMessage(LOG_FILE, " - cpp    - execute " + RESULT_FILE, true);
		std::system("javac  HelloWorld.java");
		string command = "java HelloWorld " + this->contentAllEncoded + " " + this->numberOfRounds;
		std::system(command.c_str());
	}

	void createProgram() {
		remove(RESULT_FILE.c_str());
		appendMessage(LOG_FILE, " - cpp    - delete  " + RESULT_FILE + "  succeeded=" + ((!std::ifstream(RESULT_FILE.c_str())) ? "true" : "false"), true);

		appendMessage(LOG_FILE, " - cpp    - create  " + RESULT_FILE, true);
		string contentAll = base64_decode(contentAllEncoded);
		istringstream buf(contentAll);
		istream_iterator<string> beg(buf), end;
		vector < string > tokens(beg, end);
		string contentCppEncoded = tokens.at(0);
		string contentCpp = base64_decode(contentCppEncoded);
		appendMessage(RESULT_FILE, contentCpp, false);
	}

	void appendMessage(std::string file, std::string message, bool addTimeStamp) {
		ofstream outfile;
		outfile.open(file, ios_base::app);
		if (addTimeStamp) {
			time_t now;
			time(&now);
			timeval curTime;
			gettimeofday(&curTime, NULL);
			int milli = curTime.tv_usec / 1000;
			char buf[sizeof "2011-10-08T07:07:09"];
			strftime(buf, sizeof buf, "%FT%T", localtime(&now));
			sprintf(buf, "%s.%d", buf, milli);
			outfile << buf;
		}
		outfile << message << endl;
		outfile.flush();
		outfile.close();
	}

	void run() {
		appendMessage(LOG_FILE, " - cpp    - Hello, World!", true);
		createProgram();
		executeProgram();
		appendMessage(LOG_FILE, " - cpp    - End", true);
	}

};

const string HelloWorld::LOG_FILE = "HelloWorld.log";

const string HelloWorld::RESULT_FILE = "HelloWorld.java";

const string HelloWorld::BASE64_CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

int main(int argc, char *argv[]) {
	// wait a second
	usleep(1000000u);

	string contentAll = argv[1];
	string numberOfRounds = argv[2];
	HelloWorld* helloWorld = new HelloWorld(contentAll, numberOfRounds);
	helloWorld->appendMessage(HelloWorld::LOG_FILE, string(" - cpp    - Number of rounds ") + numberOfRounds, true);
	if (stoi(numberOfRounds) > 0) {
		helloWorld->run();
		helloWorld->appendMessage(HelloWorld::LOG_FILE, " - cpp    - Exit", true);
	} else {
		helloWorld->appendMessage(HelloWorld::LOG_FILE, " - cpp    - Stopped", true);
	}
	return 0;
}

