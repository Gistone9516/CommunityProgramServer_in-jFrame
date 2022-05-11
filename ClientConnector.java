package CServer;

import java.io.*;
import java.net.*;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;


class ClientConnector extends Thread{
	Socket socket;
	OutputStream outStream;    	DataOutputStream dataOutStream;
	InputStream inStream;       	DataInputStream dataInStream;
	String msg;
	String uName;
	Server server;
	final String LOGIN_TAG = "LOGIN";
	final String JOIN_TAG = "JOIN";
	final String UPDATE_TAG = "UPDATE";
	final String INFO_TAG = "INFO";
	final String pop_DB_SELECT = "pop_SELECT";
	final String free_DB_SELECT = "free_SELECT";
	final String notice_DB_SELECT = "notice_SELECT";
	final String comment_DB_SELECT = "comment_SELECT";
	final String create_DB_INSERT = "create_INSERT";
	final String DB_ADD = "ADD";
	final String DB_DEL = "DEL";
	final String DB_IFSELECT = "IFSELECT";
	final String view_DB_COUNT = "view_COUNT";
	final String commnet_DB_INSERT = "comment_INSERT";
	
	DBconnector dbc;
	
	ClientConnector(Socket _s, Server _ss){
		this.socket = _s;	
		this.server = _ss;
		this.dbc = server.dbc;
	}
	
	public void run() {
		try {
			System.out.println("Server > "+ this.socket.getInetAddress()+"에서의 접속이 연결되었습니다.");
			outStream =  this.socket.getOutputStream();
			dataOutStream = new DataOutputStream(outStream);
			inStream =  this.socket.getInputStream();
			dataInStream = new DataInputStream(inStream);
			while(true) {
				msg = dataInStream.readUTF();
				StringTokenizer stk = new StringTokenizer(msg, "//");
				String tag = stk.nextToken();
				if(tag.equals(LOGIN_TAG)) {
					String id = stk.nextToken();
					String pass = stk.nextToken();
					dbc.login_ifSelect(id, pass);
					dataOutStream.writeUTF(dbc.saveInfo);
				}
				else if(tag.equals(JOIN_TAG)) {
					String id = stk.nextToken();
					String pw = stk.nextToken();
					String nickname = stk.nextToken();
					String name = stk.nextToken();
					String sex = stk.nextToken();
					int age =  Integer.parseInt(stk.nextToken());
					String area = stk.nextToken();
					dbc.login_Insert(id, pw, nickname, name, sex, age, area);
				}
				else if(tag.equals(UPDATE_TAG)) {
					String id = stk.nextToken();
					String pw = stk.nextToken();
					String nickname = stk.nextToken();
					String name = stk.nextToken();
					String sex = stk.nextToken();
					int age =  Integer.parseInt(stk.nextToken());
					String area = stk.nextToken();
					String un = stk.nextToken();
					dbc.login_Update(id, pw, nickname, name, sex, age, area, un);
				}
				else if(tag.equals(INFO_TAG)) {
					String un = stk.nextToken();
					dbc.info_ifSelect(un);
					dataOutStream.writeUTF(dbc.saveInfo);
				}
				else if(tag.equals(pop_DB_SELECT)) {
					dbc.pop_Select();
					dataOutStream.writeUTF(dbc.saveInfo);
					dbc.saveInfo = "";
				}
				else if(tag.equals(free_DB_SELECT)) {
					dbc.free_Select();
					dataOutStream.writeUTF(dbc.saveInfo);
					dbc.saveInfo = "";
				}
				else if(tag.equals(notice_DB_SELECT)) {
					dbc.notice_Select();
					dataOutStream.writeUTF(dbc.saveInfo);
					dbc.saveInfo = "";
				}
				else if(tag.equals(create_DB_INSERT)) {
					String title = stk.nextToken();
					String kinds = stk.nextToken();
					String content = stk.nextToken();
					String _tag = stk.nextToken();
					String userNumber = stk.nextToken();
					if(kinds.equals("자유")) {dbc.free_Insert(title, content, _tag, userNumber);}
					else if(kinds.equals("공지")) {dbc.notice_Insert(title, content, _tag, userNumber);}
					
//					dataOutStream.writeUTF(dbc.saveInfo);
//					dbc.saveInfo = "";
				}
				else if(tag.equals(view_DB_COUNT)) {
					String bn = stk.nextToken();
					String views = stk.nextToken();
					int check = Integer.parseInt(stk.nextToken());
					dbc.view_Count(bn, views, check);
				}
				else if(tag.equals(commnet_DB_INSERT)) {
					String comment = stk.nextToken();
					String un = stk.nextToken();
					String bn = stk.nextToken();
					dbc.comments_Insert(comment, un, bn);
				}
				else if(tag.equals(comment_DB_SELECT)) {
					String bn = stk.nextToken();
					dbc.comments_Select(bn);
					dataOutStream.writeUTF(dbc.saveInfo);
					dbc.saveInfo = "";
				}
			}
		}catch(IOException e) {
			System.out.println("Server > 입출력 예외 발생");
		}
	}
}
