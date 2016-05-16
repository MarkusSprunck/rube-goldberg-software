#include <iostream>
#include <fstream>
#include <unistd.h>

using namespace std;

int main() {
	cout << "Hello, from C++" << endl; // prints Hello, from C++
	usleep(1000000u);
	cout << "Hello, from C++" << endl; // prints Hello, from C++

	std::ofstream outfile;
  	outfile.open("rube-goldberg-software.log", std::ios_base::app);
  	outfile << "Hello, from C++" << endl;

	return 0;
}
