#include "MYSQLConnector.h"
#define CPPCONN_LIB_BUILD 1

MYSQLConnector::MYSQLConnector() {
	serverIp = "localhost:3306";
	serverId = "root";
	serverPw = "1234";
	serverSchema = "pill";
}

bool MYSQLConnector::setting(sql::Driver*& driver, sql::Connection*& con, sql::Statement*& stmt, sql::ResultSet*& res) {
	driver = get_driver_instance();
	con = driver->connect(serverIp, serverId, serverPw);
	if (con != NULL) {
		con->setSchema(serverSchema);
		stmt = con->createStatement();
		return true;
	}
	sql::SQLString query = "SET NAMES EUC-KR";
	stmt->executeUpdate(query);
	return false;
}

void MYSQLConnector::free(sql::Connection* con, sql::Statement* stmt, sql::ResultSet* res) {
	delete res;
	delete stmt;
	delete con;
}

int MYSQLConnector::signup(string id, string password) {
	sql::Driver *driver = NULL;
	sql::Connection *con = NULL;
	sql::Statement *stmt = NULL;
	sql::ResultSet *res = NULL;

	try {
		setting(driver, con, stmt, res);
		//		string query = "";// WHERE ip LIKE ";
		//query.append(ip);
		//table �̸��� `(~�κ�)�� ���ΰ�, value�� '("�κ�)�� ���ξ� �Ѵ�.
		sql::SQLString str = "SELECT password FROM `member` WHERE userid='";
		str.append(id.c_str());
		res = stmt->executeQuery(str);
		//		cout << res->getInt("mnum");
		//stmt�� column�� ���̹Ƿ�, res2�� 2������ �޾ƿ;���.
		if (res->next()) {
			free(con, stmt, res);
			return 1; //�ߺ��� ���̵� ����
		}
		else {
			sql::SQLString str2 = "INSERT INTO `member` (`userid`, `password`) VALUES ('";
			str2.append(id.c_str());
			str2 += "', '";
			str2.append(password);
			str2 += "');";
			stmt->executeUpdate(str2);
		}

		free(con, stmt, res);
		return 0; //����
	}
	catch (sql::SQLException &e) {
		cout << "# ERR: SQLException in " << __FILE__;
		cout << "(" << __FUNCTION__ << ") on line "
			<< __LINE__ << endl;
		cout << "# ERR: " << e.what() << endl;
		cout << " (MySQL error code: " << e.getErrorCode() << endl;
		//cout << ", SQLState: " << e.getSQLState() << " )" << endl;
		return -1; //���� 
	}
}

int MYSQLConnector::login(string id, string password) {
	sql::Driver *driver = NULL;
	sql::Connection *con = NULL;
	sql::Statement *stmt = NULL;
	sql::ResultSet *res = NULL;

	try {
		setting(driver, con, stmt, res);
		//		string query = "";// WHERE ip LIKE ";
		//query.append(ip);
		//table �̸��� `(~�κ�)�� ���ΰ�, value�� '("�κ�)�� ���ξ� �Ѵ�.
		sql::SQLString str = "SELECT password FROM `member` WHERE userid='";
		str.append(id.c_str());
		str += "';";
		res = stmt->executeQuery(str);
		//		cout << res->getInt("mnum");
		//stmt�� column�� ���̹Ƿ�, res2�� 2������ �޾ƿ;���.
		if (res->next()) {
			string userPw = string(res->getString("password").c_str());
			if (userPw == password) {
				free(con, stmt, res);
				return 2; //�α��� �Ϸ�
			}
			else {
				free(con, stmt, res);
				return 1; //password�� �������� �ʽ��ϴ�.
			}
		}
		
		free(con, stmt, res);
		return 0; //id�� �������� �ʽ��ϴ�.
	}
	catch (sql::SQLException &e) {
		cout << "# ERR: SQLException in " << __FILE__;
		cout << "(" << __FUNCTION__ << ") on line "
			<< __LINE__ << endl;
		cout << "# ERR: " << e.what() << endl;
		cout << " (MySQL error code: " << e.getErrorCode() << endl;
		//cout << ", SQLState: " << e.getSQLState() << " )" << endl;
		return -1; //���� 
	}
}

void MYSQLConnector::registerSearch(string userId, string pillName, string pillSrc, string imgSrc) {
	sql::Driver *driver = NULL;
	sql::Connection *con = NULL;
	sql::Statement *stmt = NULL;
	sql::ResultSet *res = NULL;

	try {
		setting(driver, con, stmt, res);
		//		string query = "";// WHERE ip LIKE ";
		//query.append(ip);
		//table �̸��� `(~�κ�)�� ���ΰ�, value�� '("�κ�)�� ���ξ� �Ѵ�.
		sql::SQLString str = "INSERT INTO `search` ('userid','pillname','pillsrc','imgsrc') VALUES ('";
		str.append(userId.c_str()); str += "','";
		str.append(pillName.c_str()); str += "','";
		str.append(pillSrc.c_str()); str += "','";
		str.append(imgSrc.c_str()); str += "');";

		stmt->executeUpdate(str);
		free(con, stmt, res);
	}
	catch (sql::SQLException &e) {
		cout << "# ERR: SQLException in " << __FILE__;
		cout << "(" << __FUNCTION__ << ") on line "
			<< __LINE__ << endl;
		cout << "# ERR: " << e.what() << endl;
		cout << " (MySQL error code: " << e.getErrorCode() << endl;
		//cout << ", SQLState: " << e.getSQLState() << " )" << endl;
	}
}

string MYSQLConnector::getSearchList(string userId) {
	sql::Driver *driver = NULL;
	sql::Connection *con = NULL;
	sql::Statement *stmt = NULL;
	sql::ResultSet *res = NULL;

	try {
		setting(driver, con, stmt, res);
		//		setting(driver, con, stmt, res);
		//		string query = "";// WHERE ip LIKE ";
		//query.append(ip);
		//table �̸��� `(~�κ�)�� ���ΰ�, value�� '("�κ�)�� ���ξ� �Ѵ�.
		sql::SQLString str = "SELECT pillname,pillsrc,imgsrc FROM `search`";
		str.append("JOIN pillinfo ON pillinfo.number=search.number WHERE search.userid='");
		str.append(userId.c_str());
		str += "';";
		res = stmt->executeQuery(str);
		//		cout << res->getInt("mnum");
		//stmt�� column�� ���̹Ƿ�, res2�� 2������ �޾ƿ;���.
		vector<string> list;
		string jsonData = "[";
		while (res->next()) {
			jsonData.append("{\"pillname\":\"" + string(res->getString("pillname").c_str()) + "\",");
			jsonData.append("\"pillsrc\":\"" + string(res->getString("pillsrc").c_str()) + "\",");
			jsonData.append("\"imgsrc\":\"" + string(res->getString("imgsrc").c_str()) + "\"},");
		}
		jsonData = jsonData.substr(0, jsonData.size() - 1);
		jsonData.append("]");
		cout << jsonData << endl;
		free(con, stmt, res);
		return jsonData;
	}
	catch (sql::SQLException &e) {
		cout << "# ERR: SQLException in " << __FILE__;
		cout << "(" << __FUNCTION__ << ") on line "
			<< __LINE__ << endl;
		cout << "# ERR: " << e.what() << endl;
		cout << " (MySQL error code: " << e.getErrorCode() << endl;
		//cout << ", SQLState: " << e.getSQLState() << " )" << endl;
	}
}

string MYSQLConnector::getMatchedURL(Pill pill,string userid) {
	sql::Driver *driver = NULL;
	sql::Connection *con = NULL;
	sql::Statement *stmt = NULL;
	sql::ResultSet *res = NULL;

	try {
		setting(driver, con, stmt, res);
		//		string query = "";// WHERE ip LIKE ";
		//query.append(ip);
		//table �̸��� `(~�κ�)�� ���ΰ�, value�� '("�κ�)�� ���ξ� �Ѵ�.
		sql::SQLString str = "SELECT pillsrc,number FROM `pillinfo` WHERE color='";
		str.append(pill.getColor().c_str()); 
		str += "' and shape='";
		str.append(pill.getShape().c_str());
		str += "';";

		res = stmt->executeQuery(str);
		if (res->next()) {
			sql::SQLString url = res->getString("pillsrc");
			if (userid.compare("") != 0) {
				sql::SQLString str2 = "INSERT INTO `search` VALUES ('";
				str2.append(to_string(res->getInt("number")).c_str());
				str2 += "','";
				str2.append(userid.c_str());
				str2 += "');";

				stmt->executeUpdate(str2);
			}
			free(con, stmt, res);
			return string(url.c_str());
		}
		free(con, stmt, res);
		return "fail";
	}
	catch (sql::SQLException &e) {
		cout << "# ERR: SQLException in " << __FILE__;
		cout << "(" << __FUNCTION__ << ") on line "
			<< __LINE__ << endl;
		cout << "# ERR: " << e.what() << endl;
		cout << " (MySQL error code: " << e.getErrorCode() << endl;
		//cout << ", SQLState: " << e.getSQLState() << " )" << endl;
	}
}