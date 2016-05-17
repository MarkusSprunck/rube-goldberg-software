#include <iostream>
#include <fstream>
#include <unistd.h>
#include <ctime>
#include <sys/time.h>

using namespace std;

std::string getTime() {
	timeval curTime;
	time_t now;

	time(&now);
	gettimeofday(&curTime, NULL);

	int milli = curTime.tv_usec / 1000;
	char buf[sizeof "2011-10-08T07:07:09"];
	strftime(buf, sizeof buf, "%FT%T", localtime(&now));
	sprintf(buf, "%s.%d", buf, milli);

	return buf;
}

int main(int argc,char *argv[]) {

	std::ofstream outfile;
	outfile.open("rube-goldberg-software.log", std::ios_base::app);

	outfile << getTime() << " - cpp    - Number of arguments " << (argc-1) << endl;
	outfile << getTime() << " - cpp    - " << argv[1] << endl;
	usleep(2000000u);
	outfile << getTime() << " - cpp    - Hello, from C++" << endl;

	return 0;
}