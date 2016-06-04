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
		return (ret);
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
		string command = "java HelloWorld " + this->contentAllEncoded + " " + this->numberOfRounds
				+ " &";
		std::system(command.c_str());
	}

	void createProgram() {
		remove(RESULT_FILE.c_str());
		appendMessage(LOG_FILE,
				" - cpp    - delete  " + RESULT_FILE + "  succeeded="
						+ ((!std::ifstream(RESULT_FILE.c_str())) ? "true" : "false"), true);

		appendMessage(LOG_FILE, " - cpp    - create  " + RESULT_FILE, true);
		string contentAll = base64_decode(contentAllEncoded);
		istringstream buf(contentAll);
		istream_iterator<string> beg(buf), end;
		vector<string> tokens(beg, end);
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
			char buf[sizeof "2011-10-08T07:07:090"];
			strftime(buf, sizeof buf, "%FT%T", localtime(&now));
			sprintf(buf, "%s.%03d", buf, milli);
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
		appendMessage(LOG_FILE, " - cpp    - end", true);
	}

};

const string HelloWorld::LOG_FILE = "HelloWorld.log";

const string HelloWorld::RESULT_FILE = "HelloWorld.java";

const string HelloWorld::BASE64_CHAR =
		"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

int main(int argc, char *argv[]) {
	// wait
	usleep(3000000u);

	string contentAll = argv[1];
	string numberOfRounds = argv[2];
	HelloWorld* helloWorld = new HelloWorld(contentAll, numberOfRounds);
	helloWorld->appendMessage(HelloWorld::LOG_FILE, string(" - cpp    - round ") + numberOfRounds,
			true);
	if (stoi(numberOfRounds) > 0) {
		helloWorld->run();
		helloWorld->appendMessage(HelloWorld::LOG_FILE, " - cpp    - exit", true);
	} else {
		helloWorld->appendMessage(HelloWorld::LOG_FILE, " - cpp    - stopped", true);
	}
	return (0);
}

