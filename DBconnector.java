package CServer;

import java.sql.*;
import java.util.Random;

public class DBconnector {
		
		ArrayOfObject ao;
		Connection con = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		String url = "jdbc:mysql://127.0.0.1:3306/community?serverTimezone=Asia/Seoul";
		String user = "root";
		String passwd = "1111";
		
		//logininfomation(9) : ID, password, Nickname, name, sex, age, Area, userNumber(Key), permissions
		//noticeboard(6) : title, contents, tag, boardNumber(Key), userNumber(Key), views
		//comments(4) :  comment, commentNumber(Key), userNumber(Key), boardNumber(Key)
		
		String login_I_sql = "insert into logininfomation(ID, password, Nickname, name, sex, age, Area, userNumber, permissions) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		String login_S_sql = "SELECT * FROM logininfomation";
		String login_U_sql = "update logininfomation set password=?, Nickname=?, name=?, sex=?, age=?, Area=? where userNumber=?";
		String login_D_sql = "delete from logininformation where userNumber=?";
		
		String pop_S_sql = "SELECT * FROM freeboard order by views desc";
		
		String free_I_sql = "insert into freeboard(title, contents, tag, boardNumber, userNumber, views) values (?, ?, ?, ?, ?, ?)";
		String free_S_sql = "SELECT * FROM freeboard";
		String free_S_u_sql = "SELECT * FROM freeboard WHERE userNumber=?";
		String free_U_view_sql = "update freeboard set views=? where boardNumber=?";
		
		String notice_I_sql = "insert into noticeboard(title, contents, tag, boardNumber, userNumber, views) values (?, ?, ?, ?, ?, ?)";
		String notice_S_sql = "SELECT * FROM noticeboard";
		String notice_S_u_sql = "SELECT * FROM noticeboard WHERE userNumber=?";
		String notice_U_sql = "update noticeboard set title=?, contents=?, tag=?";
		String notice_U_view_sql = "update noticeboard set views=? where boardNumber=?";
		String notice_D_sql = "delete from noticeboard where boardNumber=?";
		
		String comments_I_sql = "insert into comments(comment, commentNumber, userNumber, boardNumber) values (?, ?, ?, ?)";
		String comments_S_sql = "SELECT * FROM comments WHERE boardNumber=?";
		String comments_S_u_sql = "SELECT * FROM comments WHERE userNumber=?";
		String comments_U_sql = "update from comments set comment=?";
		String comments_D_sql = "delete from comments where commentNumber";
			
		
		String tag = "";
		String msg = "";
		String saveInfo = "";
		
		
		DBconnector(){
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			} catch(java.lang.ClassNotFoundException e) {
				System.err.print("ClassNotFoundException: "); 
				System.err.println(e.getMessage());
				return;
			}
		}
		
		//////////////// Insert Sector
		
		void login_Insert(String ID, String password, String Nickname, String name, String sex, int age, String Area) {
			//logininfomation(9) : ID, password, Nickname, name, sex, age, Area, userNumber(Key), permissions
			int count = 0;
			try {
				con = DriverManager.getConnection(url, user, passwd);	
				stmt = con.createStatement();
				result = stmt.executeQuery(login_S_sql);  //4학년 2017 1학년 2021
				String totNum;
				String userNumber;
				String permissions = "유저";
				int ranNum = 0;
				
				
				Random random = new Random();
				while(true) {
					boolean numCheck = false;
					while(true) {
						ranNum = random.nextInt(1000000);
						if(ranNum > 100000 && ranNum < 1000000) {
							break;
						}
					}
					totNum = Integer.toString(ranNum);
					while(result.next()) {
						String _userNumber = result.getString("userNumber");
						if(totNum.equals(_userNumber)) {
							numCheck = true;
						}
					}
					if(numCheck == false) {
						break;
					}
				}
				userNumber = totNum;
				//, String userNumber, String permissions  랜덤 지정 및 고정 지정
				
				pstmt = con.prepareStatement(login_I_sql);
				pstmt.setString(1, ID); //nn
				pstmt.setString(2, password); //nn
				pstmt.setString(3, Nickname); //nn
				pstmt.setString(4, name);
				pstmt.setString(5, sex);
				pstmt.setInt(6, age);
				pstmt.setString(7, Area);
				pstmt.setString(8, userNumber); //key nn
				pstmt.setString(9, permissions);//nn
				int check = pstmt.executeUpdate(); 
				
				if(check == 1) {System.out.println("login_I : 항목 추가 완료");}
				else if(check == 0) {System.out.println("login_I : 추가된 항목이 없음");}
				} catch(SQLException ex) {
					System.err.println("SQL Exception: " + ex.getMessage());
				} finally {
	        		EndChecker();
	      	 	}
		}
		
		void notice_Insert(String title, String contents, String tag, String userNumber) {
			//noticeboard(6) : title, contents, tag, boardNumber(Key), userNumber(Key), views
			int count = 0;
			try {
				con = DriverManager.getConnection(url, user, passwd);	
				stmt = con.createStatement();
				result = stmt.executeQuery(notice_S_sql); 
				
				String totNum;
				String boardNumber;
				String permissions = "유저";
				int ranNum = 0;
				
				//, String boardNumber, String userNumber, int views
				Random random = new Random();
				while(true) {
					boolean numCheck = false;
					while(true) {
						ranNum = random.nextInt(1000000);
						if(ranNum > 100000 && ranNum < 1000000) {
							break;
						}
					}
					totNum = Integer.toString(ranNum);
					while(result.next()) {
						String _userNumber = result.getString("boardNumber");
						if(totNum.equals(_userNumber)) {
							numCheck = true;
						}
					}
					if(numCheck == false) {
						break;
					}
				}
				boardNumber = totNum;
				int views = 0;
				
				
				//, String boardNumber, String userNumber, int views
				pstmt = con.prepareStatement(notice_I_sql);
				pstmt.setString(1, title); //nn
				pstmt.setString(2, contents); //nn
				pstmt.setString(3, tag); 
				pstmt.setString(4, boardNumber); //key nn
				pstmt.setString(5, userNumber); //key nn
				pstmt.setInt(6, views);//nn
				int check = pstmt.executeUpdate(); 
				
				if(check == 1) {System.out.println("notice_I : 항목 추가 완료");}
				else if(check == 0) {System.out.println("notice_I : 추가된 항목이 없음");}
				} catch(SQLException ex) {
					System.err.println("SQL Exception: " + ex.getMessage());
				} finally {
	        		EndChecker();
	      	 	}
		}
		
		void free_Insert(String title, String contents, String tag, String userNumber) {
			int count = 0;
			try {
				con = DriverManager.getConnection(url, user, passwd);	
				stmt = con.createStatement();
				result = stmt.executeQuery(free_S_sql); 
				
				String totNum;
				String boardNumber;
				String permissions = "유저";
				int ranNum = 0;
				
				//, String boardNumber, String userNumber, int views
				Random random = new Random();
				while(true) {
					boolean numCheck = false;
					while(true) {
						ranNum = random.nextInt(1000000);
						if(ranNum > 100000 && ranNum < 1000000) {
							break;
						}
					}
					totNum = Integer.toString(ranNum);
					while(result.next()) {
						String _userNumber = result.getString("boardNumber");
						if(totNum.equals(_userNumber)) {
							numCheck = true;
						}
					}
					if(numCheck == false) {
						break;
					}
				}
				boardNumber = totNum;
				int views = 0;
				
				
				pstmt = con.prepareStatement(free_I_sql);
				pstmt.setString(1, title); //nn
				pstmt.setString(2, contents); //nn
				pstmt.setString(3, tag); 
				pstmt.setString(4, boardNumber); //key nn
				pstmt.setString(5, userNumber); //key nn
				pstmt.setInt(6, views);//nn
				int check = pstmt.executeUpdate(); 
				
				if(check == 1) {System.out.println("free_I : 항목 추가 완료");}
				else if(check == 0) {System.out.println("free_I : 추가된 항목이 없음");}
				} catch(SQLException ex) {
					System.err.println("SQL Exception: " + ex.getMessage());
				} finally {
	        		EndChecker();
	      	 	}
		}
		
		void comments_Insert(String comment, String userNumber, String boardNumber) {
			//comments(4) :  comment, commentNumber(Key), userNumber(Key), boardNumber(Key)
			// String commentNumber
			int count = 0;
			try {
				con = DriverManager.getConnection(url, user, passwd);	
				pstmt = con.prepareStatement(comments_S_sql);
				pstmt.setString(1, boardNumber);
				result = pstmt.executeQuery(); 
				
				String totNum;
				String commentNumber;
				int ranNum = 0;
				
				Random random = new Random();
				while(true) {
					boolean numCheck = false;
					while(true) {
						ranNum = random.nextInt(1000000);
						if(ranNum > 100000 && ranNum < 1000000) {
							break;
						}
					}
					totNum = Integer.toString(ranNum);
					while(result.next()) {
						String _commentNumber = result.getString("commentNumber");
						if(totNum.equals(_commentNumber)) {
							numCheck = true;
						}
					}
					if(numCheck == false) {
						break;
					}
				}
				commentNumber = totNum;
				int views = 0;
				
				pstmt = con.prepareStatement(comments_I_sql);
				pstmt.setString(1, comment); //nn
				pstmt.setString(2, commentNumber); //key nn
				pstmt.setString(3, userNumber); // nn
				pstmt.setString(4, boardNumber); // nn
				int check = pstmt.executeUpdate(); 
				
				if(check == 1) {System.out.println("comments_I : 항목 추가 완료");}
				else if(check == 0) {System.out.println("comments_I : 추가된 항목이 없음");}
				} catch(SQLException ex) {
					System.err.println("SQL Exception: " + ex.getMessage());
				} finally {
	        		EndChecker();
	      	 	}
		}
		
		
		/////////////////////    Select Sector
		
		public void login_Select() {
			//logininfomation(9) : ID, password, Nickname, name, sex, age, Area, userNumber(Key), permissions
			//ArrayOfObject[] info;
			try {
				con = DriverManager.getConnection(url, user, passwd);
				stmt = con.createStatement();
	                        // customer 테이블에 있는 모든 레코드 검색 	
				result = stmt.executeQuery(login_S_sql); 
				tag = "login_SELECT";
				saveInfo = "";
				msg = "";
				// result 객체에 저장된 질의 결과로부터 행의 정보 얻음
				int count=0;
				while (result.next()) { 
					String ID = result.getString("ID");
					String password = result.getString("password");
					String Nickname =  result.getString("Nickname");
					String name = result.getString("name");
					String sex = result.getString("sex");
					int age = result.getInt("age");
					String Area = result.getString("Area");
					String userNumber = result.getString("userNumber");
					String permissions = result.getString("permissions");
					msg += ID + "." + password + "." + Nickname + "." + name + "."+ sex + "." + age + "." + Area + "." + userNumber + "." + permissions;
					msg += "-";
					count++;
				}
				saveInfo += tag + "//" + msg;
			} catch(SQLException ex) {
				System.err.println("login_Select 오류: " + ex.getMessage());
			}finally {
				EndChecker();
			}
			//....  SELECT//count//msgb
		}
		
		public void pop_Select() {
			//ArrayOfObject[] info;
			try {
				con = DriverManager.getConnection(url, user, passwd);
				stmt = con.createStatement();
	                        // customer 테이블에 있는 모든 레코드 검색 	
				result = stmt.executeQuery(pop_S_sql); 
				tag = "pop_SELECT";
				saveInfo = "";
				msg = "";
				// result 객체에 저장된 질의 결과로부터 행의 정보 얻음
				int count=0;
				while (result.next()) { 
					String title = result.getString("title");
					String contents = result.getString("contents");
					String tag =  result.getString("tag");
					String boardNumber = result.getString("boardNumber");
					String userNumber = result.getString("userNumber");
					int views = result.getInt("views");
					msg += title + "." + contents + "." + tag + "." + boardNumber + "." + userNumber + "." + views;
					msg += "-";
					count++;
				}
				saveInfo += tag + "//" + msg;
			} catch(SQLException ex) {
				System.err.println("pop_Select 오류: " + ex.getMessage());
			}finally {
				EndChecker();
			}
		}
		
		public void free_Select() {
			//ArrayOfObject[] info;
			try {
				con = DriverManager.getConnection(url, user, passwd);
				stmt = con.createStatement();
	                        // customer 테이블에 있는 모든 레코드 검색 	
				result = stmt.executeQuery(free_S_sql); 
				tag = "free_SELECT";
				saveInfo = "";
				msg = "";
				// result 객체에 저장된 질의 결과로부터 행의 정보 얻음
				int count=0;
				while (result.next()) { 
					String title = result.getString("title");
					String contents = result.getString("contents");
					String tag =  result.getString("tag");
					String boardNumber = result.getString("boardNumber");
					String userNumber = result.getString("userNumber");
					int views = result.getInt("views");
					msg += title + "." + contents + "." + tag + "." + boardNumber + "." + userNumber + "." + views;
					msg += "-";
					count++;
				}
				saveInfo += tag + "//" + msg;
			} catch(SQLException ex) {
				System.err.println("free_Select 오류: " + ex.getMessage());
			}finally {
				EndChecker();
			}
		}
		 
		public void notice_Select() {
			//noticeboard(6) : title, contents, tag, boardNumber(Key), userNumber(Key), views
			try {
				con = DriverManager.getConnection(url, user, passwd);
				stmt = con.createStatement();
	                        // customer 테이블에 있는 모든 레코드 검색 	
				result = stmt.executeQuery(notice_S_sql); 
				tag = "notice_SELECT";
				saveInfo = "";
				msg = "";
				// result 객체에 저장된 질의 결과로부터 행의 정보 얻음
				int count=0;
				while (result.next()) { 
					String title = result.getString("title");
					String contents = result.getString("contents");
					String tag =  result.getString("tag");
					String boardNumber = result.getString("boardNumber");
					String userNumber = result.getString("userNumber");
					int views = result.getInt("views");
					msg += title + "." + contents + "." + tag + "." + boardNumber + "." + userNumber + "." + views;
					msg += "-";
					count++;
				}
				saveInfo += tag + "//" + msg;
			} catch(SQLException ex) {
				System.err.println("notice_Select 오류: " + ex.getMessage());
			}finally {
				EndChecker();
			}
		}
		
		public void comments_Select(String bn) {
			//comments(4) :  comment, commentNumber(Key), userNumber(Key), boardNumber(Key)
			try {
				con = DriverManager.getConnection(url, user, passwd);
				pstmt = con.prepareStatement(comments_S_sql);
	                        // customer 테이블에 있는 모든 레코드 검색
				pstmt.setString(1, bn);
				result = pstmt.executeQuery(); 
				tag = "comment_SELECT";
				saveInfo = "";
				msg = "";
				// result 객체에 저장된 질의 결과로부터 행의 정보 얻음
				int count=0;
				while (result.next()) { 
					String comment = result.getString("comment");
					String commentNumber = result.getString("commentNumber");
					String userNumber =  result.getString("userNumber");
					String boardNumber = result.getString("boardNumber");
					msg += comment + "." + commentNumber + "." + userNumber + "." + boardNumber;
					msg += "-";
					count++;
				}
				saveInfo += tag + "//" + msg;
			} catch(SQLException ex) {
				System.err.println("comments_Select 오류: " + ex.getMessage());
			}finally {
				EndChecker();
			}
		}
		

		///////////////// IfSelect Sector
		
		public void login_ifSelect(String ID, String password) {
			//logininfomation(9) : ID, password, Nickname, name, sex, age, Area, userNumber(Key), permissions
					//ArrayOfObject[] info;
					try {
						con = DriverManager.getConnection(url, user, passwd);
						stmt = con.createStatement();
			                        // customer 테이블에 있는 모든 레코드 검색 	
						result = stmt.executeQuery(login_S_sql); 
						tag = "login_SELECT";
						saveInfo = "";
						msg = "";
						// result 객체에 저장된 질의 결과로부터 행의 정보 얻음
						int check = 0;
						
						String id, pw, Nickname, name, sex, Area, userNumber, permissions;
						int age;

						
						while (result.next()) {
							id = result.getString("ID");
							if(ID.equals(id)) {
								pw = result.getString("password");
								if(pw.equals(password)) {
									Nickname =  result.getString("Nickname");
									name = result.getString("name");
									sex = result.getString("sex");
									age = result.getInt("age");
									Area = result.getString("Area");
									userNumber = result.getString("userNumber");
									permissions = result.getString("permissions");
									msg += ID + "." + password + "." + Nickname + "." + name + "."+ sex + "." + age + "." + Area + "." + userNumber + "." + permissions;
									check = 1;
									break;
								}else {
									check = 2;
								}
							}
						}
						if(check == 1) {
							saveInfo += msg;
						}else if (check == 2) {
							saveInfo += "PWerror";
						}else if (check == 0) {
							saveInfo += "IDnonExist";
						}
						
						
					} catch(SQLException ex) {
						System.err.println("login_ifSelect 오류: " + ex.getMessage());
					}finally {
						EndChecker();
					}
					//....  SELECT//count//msgb
				}
		
		void info_ifSelect(String un) {
			int cc = 0;
			int bc = 0;
			try {
				saveInfo = "";
				tag = "INFO";
				con = DriverManager.getConnection(url, user, passwd);
				pstmt = con.prepareStatement(notice_S_u_sql);
				pstmt.setString(1, un);
				result = pstmt.executeQuery(); 
				while (result.next()) { 
					bc++;
				}
				EndChecker();
				con = DriverManager.getConnection(url, user, passwd);
				pstmt = con.prepareStatement(free_S_u_sql);
				pstmt.setString(1, un);
				result = pstmt.executeQuery(); 
				while (result.next()) { 
					bc++;
				}
				EndChecker();
				con = DriverManager.getConnection(url, user, passwd);
				pstmt = con.prepareStatement(comments_S_u_sql);
				pstmt.setString(1, un);
				result = pstmt.executeQuery(); 
				while (result.next()) { 
					cc++;
				}
				EndChecker();
				
				saveInfo += tag + "//" + Integer.toString(bc) + "//" + Integer.toString(cc);
			} catch(SQLException ex) {
				System.err.println("info_ifSelect 오류: " + ex.getMessage());
			}finally {
				EndChecker();
			}
		}
		
		
		
		///////////////  Update Sector
		
		void login_Update(String ID, String password, String Nickname, String name, String sex, int age, String Area, String un) {
			try {
				//password=?, Nickname=?, name=?, sex=?, age=?, Area=? where userNumber=?
				con = DriverManager.getConnection(url, user, passwd);	
				pstmt = con.prepareStatement(login_U_sql);
				pstmt.setString(1, password);
				pstmt.setString(2, Nickname);
				pstmt.setString(3, name);
				pstmt.setString(4, sex);
				pstmt.setInt(5, age);
				pstmt.setString(6, Area);
				pstmt.setString(7, un);
				int r = pstmt.executeUpdate();

				System.out.println("변경된 row : " + r);
				
				} catch(SQLException ex) {
					System.err.println("SQLException: " + ex.getMessage());
				} finally {
	        		EndChecker();
	      	 	}
		}
		
		void view_Count(String bn, String views, int check){
			int count = 0;
			int view = Integer.parseInt(views);
			view++;

			try {
				con = DriverManager.getConnection(url, user, passwd);	
				if(check == 1 || check == 2) 
				{pstmt = con.prepareStatement(free_U_view_sql);}
				else if(check == 3) 
				{pstmt = con.prepareStatement(notice_U_view_sql);}
				pstmt.setInt(1, view);
				pstmt.setString(2, bn);
				int r = pstmt.executeUpdate();

				System.out.println("변경된 row : " + r);
				
				} catch(SQLException ex) {
					System.err.println("SQLException: " + ex.getMessage());
				} finally {
	        		EndChecker();
	      	 	}
		}
		
		////////////// Delete Sector
		
		void login_Delete(String Snum) {
			try {
				con = DriverManager.getConnection(url, user, passwd);
				pstmt = con.prepareStatement(login_D_sql);
				pstmt.setString(1,Snum);
				int r = pstmt.executeUpdate();
				System.out.println("변경된 row : " + r);
			}catch (SQLException e) {
				System.out.println("SQL Error" + e.getMessage());
//			}catch (ClassNotFoundException e1) {
//				System.out.println("JDBC Connector Driver Error" + e1.getMessage());
			}finally {
				EndChecker();
			}
		}
		void notice_Delete(String Snum) {
			try {
				con = DriverManager.getConnection(url, user, passwd);
				pstmt = con.prepareStatement(notice_D_sql);
				pstmt.setString(1,Snum);
				int r = pstmt.executeUpdate();
				System.out.println("변경된 row : " + r);
			}catch (SQLException e) {
				System.out.println("SQL Error" + e.getMessage());
//			}catch (ClassNotFoundException e1) {
//				System.out.println("JDBC Connector Driver Error" + e1.getMessage());
			}finally {
				EndChecker();
			}
		}
		void comments_Delete(String Snum) {
			try {
				con = DriverManager.getConnection(url, user, passwd);
				pstmt = con.prepareStatement(comments_D_sql);
				pstmt.setString(1,Snum);
				int r = pstmt.executeUpdate();
				System.out.println("변경된 row : " + r);
			}catch (SQLException e) {
				System.out.println("SQL Error" + e.getMessage());
//			}catch (ClassNotFoundException e1) {
//				System.out.println("JDBC Connector Driver Error" + e1.getMessage());
			}finally {
				EndChecker();
			}
		}
		
		
		
		void EndChecker() {
			if(result != null) {
				try {
					result.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				}catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(stmt != null) {
				try {
					stmt.close();
				}catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(con != null) {
				try {
					con.close();
				}catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}


