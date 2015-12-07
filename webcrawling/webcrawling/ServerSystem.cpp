#include "ServerSystem.h"

vector<Customer> ServerSystem::customerList;
vector<Manager> ServerSystem::managerList;

ServerSystem::ServerSystem() {

}

string getJsonData(string shape, string color, string dosageForm, string divisionLine, string page) {
	HttpClient client("terms.naver.com");
	string data = "mode=exteriorSearch&shape=" + shape + "&color=" + color + "&dosageForm=" + dosageForm + "&divisionLine=" + divisionLine + "&identifier=" + "&page=" + page;
	stringstream ss(data);
	auto r1 = client.request("GET", "/medicineSearch.nhn", ss);
	char* contents;

	if (r1->content) {
		std::string s((istreambuf_iterator<char>(r1->content.rdbuf())),
			istreambuf_iterator<char>());

		std::vector<string> imgSrc;
		std::vector<string> pillSrc;
		std::vector<string> pillName;
		const char* ptr = s.c_str();
		const char* startPtr;
		const char* endPtr = ptr;
		while (startPtr = strstr(endPtr, "http://dthumb.phinf.naver.net/")) {
			endPtr = strstr(startPtr, "\"");
			imgSrc.push_back(s.substr(startPtr - s.c_str(), endPtr - startPtr));
			startPtr = strstr(endPtr, "/entry.nhn");
			endPtr = strstr(startPtr, "\"");
			pillSrc.push_back(s.substr(startPtr - s.c_str(), endPtr - startPtr));
			startPtr = strstr(endPtr, "<strong>");
			endPtr = strstr(startPtr, "[");
			pillName.push_back(s.substr(startPtr - s.c_str() + 8, endPtr - startPtr - 9));
		}
		/*
		startPtr = strstr(ptr, "paginate");
		startPtr = strstr(startPtr - 10, "<div");
		endPtr = strstr(startPtr, "</div");
		string pageData = s.substr(startPtr - s.c_str(), endPtr - startPtr + 6);
		boost::replace_all(pageData, "\"", "'");
		boost::replace_all(pageData, "	", "");
		boost::replace_all(pageData, "\r", "");
		boost::replace_all(pageData, "\n", "");
		cout << pageData << endl;
		*/
		//pageData.replace(pageData.begin(), pageData.end(), '\"', '\'');

		string jsonData = "[";
//		jsonData.append("\"pageData\":\"" + pageData + "\",");
		jsonData.append("\"shape\":\"" + shape + "\",");
		jsonData.append("\"color\":\"" + color + "\",");
		jsonData.append("\"dosageForm\":\"" + dosageForm + "\",");
		jsonData.append("\"divisionLine\":\"" + divisionLine + "\",");
		for (int i = 0; i < imgSrc.size(); i++) {
			jsonData.append("{");
			jsonData.append("\"imgSrc\":\"" + imgSrc[i] + "\",");
			jsonData.append("\"pillSrc\":\"" + pillSrc[i] + "\",");
			jsonData.append("\"pillName\":\"" + pillName[i] + "\"}");
			if (i != imgSrc.size() - 1)
				jsonData.append(",");
		}
		jsonData.append("]");
		return jsonData;
	}
}
/*------ Base64 Decoding Table ------*/
static int DecodeMimeBase64[256] = {
	-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  /* 00-0F */
	-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  /* 10-1F */
	-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63,  /* 20-2F */
	52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1,  /* 30-3F */
	-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,  /* 40-4F */
	15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,  /* 50-5F */
	-1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,  /* 60-6F */
	41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1,  /* 70-7F */
	-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  /* 80-8F */
	-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  /* 90-9F */
	-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  /* A0-AF */
	-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  /* B0-BF */
	-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  /* C0-CF */
	-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  /* D0-DF */
	-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  /* E0-EF */
	-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1   /* F0-FF */
};

int base64_decodetest(char *text, unsigned char *dst, int numBytes)
{
	const char* cp;
	int space_idx = 0, phase;
	int d, prev_d = 0;
	unsigned char c;
	space_idx = 0;
	phase = 0;
	for (cp = text; *cp != '\0'; ++cp) {
		d = DecodeMimeBase64[(int)*cp];
		if (d != -1) {
			switch (phase) {
			case 0:
				++phase;
				break;
			case 1:
				c = ((prev_d << 2) | ((d & 0x30) >> 4));
				if (space_idx < numBytes)
					dst[space_idx++] = c;
				++phase;
				break;
			case 2:
				c = (((prev_d & 0xf) << 4) | ((d & 0x3c) >> 2));
				if (space_idx < numBytes)
					dst[space_idx++] = c;
				++phase;
				break;
			case 3:
				c = (((prev_d & 0x03) << 6) | d);
				if (space_idx < numBytes)
					dst[space_idx++] = c;
				phase = 0;
				break;
			}
			prev_d = d;
		}
	}
	return space_idx;
}

void ServerSystem::Run() {
	HttpServer server(12334, 4);

	server.resource["^/signup$"]["POST"] = [](HttpServer::Response& response, shared_ptr<HttpServer::Request> request) {
		try {
			MYSQLConnector conn;
			boost::property_tree::ptree pt;
			read_json(request->content, pt);

			string id = pt.get<string>("userid");
			string password = pt.get<string>("password");
			int check = conn.signup(id, password);
			if (check == 1) {
				string returnStr = "ID already exist";
				response << "HTTP/1.1 200 OK\r\nAccess-Control-Allow-Origin: *\r\nContent-Length: " << returnStr.length() << "\r\n\r\n" << returnStr;
			}
			else
				response << "HTTP/1.1 200 OK\r\nAccess-Control-Allow-Origin: *\r\nContent-Length: " << 7 << "\r\n\r\n" << "Success";
		}
		catch (...) {
			response << "HTTP/1.1 400 Bad Request\r\n\r\n";
		}
	};

	server.resource["^/login$"]["POST"] = [](HttpServer::Response& response, shared_ptr<HttpServer::Request> request) {
		try {
			MYSQLConnector conn;
			boost::property_tree::ptree pt;
			read_json(request->content, pt);

			string id = pt.get<string>("userid");
			string password = pt.get<string>("password");
			int check = conn.login(id, password);
			if (check == 2) {
				Customer customer;
				customer.setId(id);
				customer.setPassword(password);
				customerList.push_back(customer);
				response << "HTTP/1.1 200 OK\r\nAccess-Control-Allow-Origin: *\r\nContent-Length: " << 7 << "\r\n\r\n" << "Success";
			}
			else if (check == 1) {
				string returnStr = "Password is incorrect";
				response << "HTTP/1.1 200 OK\r\nAccess-Control-Allow-Origin: *\r\nContent-Length: " << returnStr.length() << "\r\n\r\n" << returnStr;
			}
			else if (check == 0) {
				string returnStr = "ID do not exist";
				response << "HTTP/1.1 200 OK\r\nAccess-Control-Allow-Origin: *\r\nContent-Length: " << returnStr.length() << "\r\n\r\n" << returnStr;
			}
		}
		catch (...) {
			response << "HTTP/1.1 400 Bad Request\r\n\r\n";
		}
	};

	server.resource["^/analyze$"]["POST"] = [](HttpServer::Response& response, shared_ptr<HttpServer::Request> request) {
		//Retrieve string from istream (request->content)
		MYSQLConnector conn;
		PillAnalyzer analyzer;
		boost::property_tree::ptree pt;
		read_json(request->content, pt);
		string test = pt.get<string>("image");
		string id = "";
		string isExist = pt.get<string>("isExist");
		if (isExist.compare("true") == 0)
			id = pt.get<string>("userid");

		string::size_type pos = test.find('=', 0);
		int size = ((test.size() * 3) / 4) - (test.find('=') != string::npos ? (test.size() - pos) : 0);
		unsigned char* imgBuf = new unsigned char[size];

		base64_decodetest((char*)test.c_str(), imgBuf, test.size());
		Pill pill = analyzer.analyzePill(imgBuf, size);
		if (pill.getColor().compare("") != 0 && pill.getShape().compare("") != 0) {
			string url = conn.getMatchedURL(pill, id);
			delete imgBuf;
			response << "HTTP/1.1 200 OK\r\nAccess-Control-Allow-Origin: *\r\nContent-Length: " << url.size() << "\r\n\r\n" << url;
		}
		else
			response << "HTTP/1.1 200 OK\r\nAccess-Control-Allow-Origin: *\r\nContent-Length: " << 4 << "\r\n\r\n" << "fail";
	};

	server.resource["^/page$"]["POST"] = [](HttpServer::Response& response, shared_ptr<HttpServer::Request> request) {
		boost::property_tree::ptree pt;
		read_json(request->content, pt);

		string shape = pt.get<string>("shape");
		string color = pt.get<string>("color");
		string dosageForm = pt.get<string>("dosageForm");
		string divisionLine = pt.get<string>("divisionLine");
		string page = pt.get<string>("page");

		string jsonData = getJsonData(shape, color, dosageForm, divisionLine, page);
		response << "HTTP/1.1 200 OK\r\nAccess-Control-Allow-Origin: *\r\nContent-Length: " << jsonData.length() << "\r\n\r\n" << jsonData;
	};

	server.resource["^/search$"]["POST"] = [](HttpServer::Response& response, shared_ptr<HttpServer::Request> request) {
		MYSQLConnector conn;
		boost::property_tree::ptree pt;
		read_json(request->content, pt);

		string userId = pt.get<string>("userId");
		string pillName = pt.get<string>("pillName");
		string pillSrc = pt.get<string>("pillSrc");
		string imgSrc = pt.get<string>("imgSrc");
		conn.registerSearch(userId, pillName, pillSrc, imgSrc);
	};

	server.resource["^/searchedList$"]["POST"] = [](HttpServer::Response& response, shared_ptr<HttpServer::Request> request) {
		MYSQLConnector conn;
		boost::property_tree::ptree pt;
		stringstream ss;
		request->content >> ss.rdbuf();
		read_json(ss, pt);

		string jsonData = conn.getSearchList(pt.get<string>("userid"));
		response << "HTTP/1.1 200 OK\r\nAccess-Control-Allow-Origin: * \r\nContent-Type: text/plain\r\nContent-Length: " << jsonData.size() << "\r\n\r\n" << jsonData;
	};

	thread server_thread([&server](){
		//Start server
		server.start();
	});

	//Wait for server to start so that the client can connect
	this_thread::sleep_for(chrono::seconds(1));
	server_thread.join();
}