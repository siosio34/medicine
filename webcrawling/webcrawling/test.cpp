//#include "client_http.hpp"
//#include "server_http.hpp"
//#include<iostream>
//#include <iomanip>
//#include <fstream>
//#include "json\json.h"
//#include <boost/property_tree/ptree.hpp>
//#include <boost/property_tree/json_parser.hpp>
//#include <boost/algorithm/string/replace.hpp>
////#include <opencv/cv.h>
////#include <opencv/highgui.h>
//using namespace std;
//using namespace cv;
//using namespace boost::property_tree;
//
//typedef SimpleWeb::Server<SimpleWeb::HTTP> HttpServer;
//typedef SimpleWeb::Client<SimpleWeb::HTTP> HttpClient;
//
//static double angle(Point pt1, Point pt2, Point pt0)
//{
//	double dx1 = pt1.x - pt0.x;
//	double dy1 = pt1.y - pt0.y;
//	double dx2 = pt2.x - pt0.x;
//	double dy2 = pt2.y - pt0.y;
//	return (dx1*dx2 + dy1*dy2) / sqrt((dx1*dx1 + dy1*dy1)*(dx2*dx2 + dy2*dy2) + 1e-10);
//}
//
//bool DetectFill(unsigned char* imgBuf, string& shape, string& color, string& dosageForm, string& divisionLine) {
//}
//
//string getJsonData(string shape, string color, string dosageForm, string divisionLine, string page) {
//	HttpClient client("terms.naver.com");
//	string data = "mode=exteriorSearch&shape=" + shape + "&color=" + color + "&dosageForm=" + dosageForm + "&divisionLine=" + divisionLine + "&identifier=" + "&page=" + page;
//	stringstream ss(data);
//	auto r1 = client.request("GET", "/medicineSearch.nhn", ss);
//	char* contents;
//
//	if (r1->content) {
//		std::string s((istreambuf_iterator<char>(r1->content.rdbuf())),
//			istreambuf_iterator<char>());
//
//		std::vector<string> imgSrc;
//		std::vector<string> pillSrc;
//		std::vector<string> pillName;
//		const char* ptr = s.c_str();
//		const char* startPtr;
//		const char* endPtr = ptr;
//		while (startPtr = strstr(endPtr, "http://dthumb.phinf.naver.net/")) {
//			endPtr = strstr(startPtr, "\"");
//			imgSrc.push_back(s.substr(startPtr - s.c_str(), endPtr - startPtr));
//			startPtr = strstr(endPtr, "/entry.nhn");
//			endPtr = strstr(startPtr, "\"");
//			pillSrc.push_back(s.substr(startPtr - s.c_str(), endPtr - startPtr));
//			startPtr = strstr(endPtr, "<strong>");
//			endPtr = strstr(startPtr, "[");
//			pillName.push_back(s.substr(startPtr - s.c_str() + 8, endPtr - startPtr - 9));
//		}
//		startPtr = strstr(ptr, "paginate");
//		startPtr = strstr(startPtr - 10, "<div");
//		endPtr = strstr(startPtr, "</div");
//		string pageData = s.substr(startPtr - s.c_str(), endPtr -startPtr + 6);
//		boost::replace_all(pageData, "\"", "'");
//		boost::replace_all(pageData, "	", "");
//		boost::replace_all(pageData, "\r", "");
//		boost::replace_all(pageData, "\n", "");
//
//		cout << pageData << endl;
//		//pageData.replace(pageData.begin(), pageData.end(), '\"', '\'');
//
//		string jsonData = "{";
//		jsonData.append("\"pageData\":\"" + pageData + "\",");
//		jsonData.append("\"shape\":\"" + shape + "\",");
//		jsonData.append("\"color\":\"" + color + "\",");
//		jsonData.append("\"dosageForm\":\"" + dosageForm + "\",");
//		jsonData.append("\"divisionLine\":\"" + divisionLine + "\",");
//		for (int i = 0; i < imgSrc.size(); i++) {
//			jsonData.append("\"" + to_string(i) + "\": {\n");
//			jsonData.append("\"imgSrc\":\"" + imgSrc[i] + "\",");
//			jsonData.append("\"pillSrc\":\"" + pillSrc[i] + "\",");
//			jsonData.append("\"pillName\":\"" + pillName[i] + "\"}");
//			if (i != imgSrc.size() - 1)
//				jsonData.append(",");
//		}
//		jsonData.append("}");
////		cout << jsonData << endl;
//		//test2 = strstr(test2 - 500, "</table");
//		//cout << (int)(test2 - s.c_str()) << endl;
//		//cout << (int)(test3 - s.c_str()) << endl;
//
//		//		string css = "<link rel=\"stylesheet\" type=\"text/css\" href=\"http://static.naver.net/terms/tlist/css/tlist_add_card_150916.css\">\r\n";
//		//		string css2 = "<link rel=\"stylesheet\" type=\"text/css\" href=\"http://static.naver.net/terms/terms/css/terms_150924_v2.css\">\r\n";
//
//		//		ofstream outf;
//		//		outf.open("test.html");
//		//		outf.write(css.c_str(), css.size());
//		//		outf.write(css2.c_str(), css2.size());
//		//		outf.write(test1, test2 - test1 + 7);
//		//		outf.write(test3, test4 - test3 + 5);
//		//		outf.close();	
//		return jsonData;
//	}
//}
//
//string getLocationData(double x, double y) {
//	
//}
//
//int main() {
//	/*
////	HttpClient client("http://www.naver.com"); => http붙이면 터짐
//	HttpClient client("www.pharm.or.kr");
//
//	string data = "s_anal=&s_anal_flag=0&s_mark_code=&s_drug_form_etc=&s_drug_shape_etc=&s_drug_name=&s_upso_name=&s_upso_name2=&new_sb_name1=&new_sb_name2=";
//	stringstream ss(data);
//	auto r1 = client.request("POST", "/search/drugidfy/list.asp", ss);
//	char* contents;
//	
//	if (r1->content) {
//		std::string s((istreambuf_iterator<char>(r1->content.rdbuf())),
//			istreambuf_iterator<char>());
//		
//		const char* test = strstr(s.c_str(), "#cecece");
//		test = strstr(test-100, "<table");
//		const char* test2 = strstr(test, "\"page\"");
//		test2 = strstr(test2 - 500, "</table");
//		cout << (int)(test - s.c_str()) << endl;
//		cout << (int)(test2 - s.c_str()) << endl;
//
//		string css = "<link rel=\"stylesheet\" type=\"text/css\" href=\"/common.css\">\r\n";
//		string css2 = "<link rel=\"stylesheet\" type=\"text/css\" href=\"/highslide.css\">\r\n";
//
//		ofstream outf;
//		outf.open("test.html");
//		outf.write(css.c_str(), css.size());
//		outf.write(css2.c_str(), css2.size());
//		outf.write(test, test2-test+8);
//		outf.close();
//	}
//	*/
//	//	HttpClient client("http://www.naver.com"); => http붙이면 터짐
//	HttpServer server(8089, 4);
//	//content를 이용해 영상처리해서 shape, color, dosageForm, divisonLine얻어야함
//	server.resource["^/string$"]["POST"] = [](HttpServer::Response& response, shared_ptr<HttpServer::Request> request) {
//		//Retrieve string from istream (request->content)
//		stringstream ss;
//		request->content >> ss.rdbuf();
//		string content = ss.str();
//		string shape;
//		string color;
//		string dosageForm;
//		string divisionLine;
//
//		string jsonData = getJsonData(shape, color, dosageForm, divisionLine, "1");		
//		response << "HTTP/1.1 200 OK\r\nAccess-Control-Allow-Origin: *\r\nContent-Length: " << jsonData.length() << "\r\n\r\n" << jsonData;
//	};
//
//	server.resource["^/page$"]["POST"] = [](HttpServer::Response& response, shared_ptr<HttpServer::Request> request) {
//		ptree pt;
//		read_json(request->content, pt);
//		
//		string shape = pt.get<string>("shape");
//		string color = pt.get<string>("color");
//		string dosageForm = pt.get<string>("dosageForm");
//		string divisionLine = pt.get<string>("divisionLine");
//		string page = pt.get<string>("page");
//
//		string jsonData = getJsonData(shape, color, dosageForm, divisionLine, page);
//		response << "HTTP/1.1 200 OK\r\nContent-Length: " << jsonData.length() << "\r\n\r\n" << jsonData;
//	};
//	thread server_thread([&server](){
//		//Start server
//		server.start();
//	});
//
//	//Wait for server to start so that the client can connect
//	this_thread::sleep_for(chrono::seconds(1));
//
//	server_thread.join();
//
//	return 0;
//}
//
