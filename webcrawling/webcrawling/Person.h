#pragma once
#include <iostream>
using namespace std;

class Person {
public:
	Person() {}
	string getName() { return name; }
	string getId() { return id; }
	string getPassword() { return password; }
	void setName(string _name) { name = _name; }
	void setId(string _id) { id = _id; }
	void setPassword(string _password) { password = _password; }

private:
	string name;
	string id;
	string password;
//	int phoneNum;
};