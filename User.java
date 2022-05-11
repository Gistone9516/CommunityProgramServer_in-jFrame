package CServer;

class User{
	String id= null;
	String password = null;
	String Nickname;
	String name;
	String sex;
	int age;
	String Area;
	String userNumber;
	String permissions;
	User(String _ID, String _password, String _Nickname, String _name, String _sex, int _age, String _Area, String _userNumber, String _permissions){
		id = _ID;
		password = _password;
		Nickname = _Nickname;
		name = _name;
		sex = _sex;
		age = _age;
		Area = _Area;
		userNumber = _userNumber;
		permissions = _permissions;
	}
}
