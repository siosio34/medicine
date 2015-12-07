#pragma once
#include "MYSQLConnector.h"
#include "Customer.h"
#include "Manager.h"
#include "PillAnalyzer.h"
#include "base64.h"
#include "client_http.hpp"
#include "server_http.hpp"
#include <boost/property_tree/ptree.hpp>
#include <boost/property_tree/json_parser.hpp>
#include <boost/algorithm/string/replace.hpp>
#include <iomanip>
#include <fstream>
#include <vector>
using namespace std;

typedef SimpleWeb::Server<SimpleWeb::HTTP> HttpServer;
typedef SimpleWeb::Client<SimpleWeb::HTTP> HttpClient;

class ServerSystem {
public:
	ServerSystem();
	static void Run();

private:
	static vector<Customer> customerList;
	static vector<Manager> managerList;
};