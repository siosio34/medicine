#include <iostream>
#include <string>
#include <mysql_connection.h>
#include <cppconn/driver.h>
#include <cppconn/exception.h>
#include <cppconn/resultset.h>
#include <cppconn/statement.h>
#include <vector>
#include "Pill.h"
using namespace std;

class MYSQLConnector {
public:
	MYSQLConnector();
	bool setting(sql::Driver*& driver, sql::Connection*& con, sql::Statement*& stmt, sql::ResultSet*& res);
	void free(sql::Connection* con, sql::Statement* stmt, sql::ResultSet* res);
	int signup(string id, string password);
	int login(string id, string password);
	void registerSearch(string userId, string pillName, string pillSrc, string imgSrc);
	string getSearchList(string userId);
	string getMatchedURL(Pill pill, string userid);

private:
	//string serverIp쓰니까 안됬었음
	sql::SQLString serverIp;
	sql::SQLString serverId;
	sql::SQLString serverPw;
	sql::SQLString serverSchema;
};