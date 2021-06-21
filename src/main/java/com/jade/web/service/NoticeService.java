package com.jade.web.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jade.web.entity.Notice;
import com.jade.web.entity.NoticeView;

public class NoticeService {
	
	public int removeAll(int[] ids) {
		
		return 0;
	}
	public int pubNoticeAll(int[] ids){
		
		return 0;
	}
	
	public int inserNotice(Notice notice) {
		
		int result=0;
		
		
		String sql = "INSERT INTO NOTICE(TITLE, CONTENT, WRITER_ID, PUB, FILES) VALUES(?,?,?,?,?)";
		
		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";
		String uid = "NEWLEC";
		String pwd = "89724";
		String driver = "oracle.jdbc.driver.OracleDriver";

		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url, uid, pwd);
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1,notice.getTitle());
			st.setString(2,notice.getContent());
			st.setString(3,notice.getWriterId());
			st.setBoolean(4,notice.getPub());
			st.setString(5, notice.getFiles());
			
			result = st.executeUpdate(); //insert, update, delete 용 함수
			
			st.close();
			con.close();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	public int deleteNotice(int id){
		return 0;
	}
	public int updateNotice(Notice notice){
		return 0;
	}
	public List<Notice> getNoticeNewestList(){
		
		return null;
	}
	
	
	
	
	
	public List<NoticeView>getNoticeList(){
	
		return getNoticeList("TITLE","",1);
	}
	
	public List<NoticeView>getNoticeList(int page){
		
		return getNoticeList("TITLE", "", page);
	}
	
	public List<NoticeView>getNoticeList(String field, String query, int page){
		
		List<NoticeView> list = new ArrayList<>();
		
		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";
		String uid = "NEWLEC";
		String pwd = "89724";
		String driver = "oracle.jdbc.driver.OracleDriver";
		
		String sql = "SELECT * FROM "
				+ "(SELECT ROWNUM NUM, N.* "
				+ "FROM "
					+ "(SELECT * FROM NOTICE_VIEW_PRAC3 WHERE "+field+" LIKE ? ORDER BY REGDATE DESC) N "
				+ ") "
				+ "WHERE NUM BETWEEN ? AND ? ";
		
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url, uid, pwd);
			PreparedStatement st = con.prepareStatement(sql);
			
			st.setString(1, "%"+query+"%" );
			st.setInt(2, 1+(page-1)*10);
			st.setInt(3, page*10);
			
			ResultSet rs = st.executeQuery();
			
			while(rs.next()){
				int id = rs.getInt("ID");
				String title= rs.getString("TITLE");
				Date regdate= rs.getDate("REGDATE");
				String writerId= rs.getString("WRITER_ID");
				int hit= rs.getInt("HIT"); 
				String files= rs.getString("FILES") ;
				//String content= rs.getString("CONTENT");
				int cmtCount = rs.getInt("CMT_COUNT");
				boolean pub = rs.getBoolean("PUB");
				NoticeView notice = new NoticeView(
						id,
						title,
						regdate,
						writerId,
						hit,
						files,
						pub,
						//content
						cmtCount
						);
				list.add(notice);
			}
			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public int getNoticeCount() {
		
		return getNoticeCount("title", "");
	}
	
	public int getNoticeCount(String field, String query) {
		
		int count=0;
		
		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";
		String uid = "NEWLEC";
		String pwd = "89724";
		String driver = "oracle.jdbc.driver.OracleDriver";
		
		String sql = "SELECT COUNT(ID) COUNT FROM "
				+ "(SELECT ROWNUM NUM, N.* "
				+ "FROM "
				+ "(SELECT * FROM NOTICE WHERE "+field+" LIKE ? ORDER BY REGDATE DESC) N "
				+ ")";

		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url, uid, pwd);
			PreparedStatement st = con.prepareStatement(sql);
			
			st.setString(1, "%"+query+"%" );
			
			ResultSet rs = st.executeQuery();
			
			if(rs.next())
				count = rs.getInt("COUNT");
			
		
			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
	
	public Notice getNotice(int id) {
		
		Notice notice = null;
		
		
		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";
		String uid = "NEWLEC";
		String pwd = "89724";
		String driver = "oracle.jdbc.driver.OracleDriver";
		
		String sql = "SELECT * FROM NOTICE WHERE ID = ?";

		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url, uid, pwd);
			PreparedStatement st = con.prepareStatement(sql);
			
			st.setInt(1, id);
			
			ResultSet rs = st.executeQuery();
			
			if(rs.next()){
				int nid = rs.getInt("ID");
				String title= rs.getString("TITLE");
				Date regdate= rs.getDate("REGDATE");
				String writerId= rs.getString("WRITER_ID");
				int hit= rs.getInt("HIT"); 
				String files= rs.getString("FILES") ;
				String content= rs.getString("CONTENT");
				boolean pub = rs.getBoolean("PUB");
				 		
				notice = new Notice(
						nid,
						title,
						regdate,
						writerId,
						hit,
						files,
						
						content,
						pub
						);
			}
			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return notice;
	}
	
	public Notice getNextNotice(int id) {
		Notice notice = null;
		String sql = "SELECT * FROM NOTICE WHERE ID = ? "
				+ "SELECT ID FROM(SELECT ID FROM NOTICE WHERE REGDATE > (SELECT REGDATE FROM NOTICE WHERE ID =? ) ORDER BY REGDATE ASC) "
				+ "WHERE ROWNUM =1)";
		
		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";
		String uid = "NEWLEC";
		String pwd = "89724";
		String driver = "oracle.jdbc.driver.OracleDriver";

		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url, uid, pwd);
			PreparedStatement st = con.prepareStatement(sql);
			
			st.setInt(1, id);
			
			ResultSet rs = st.executeQuery();
			
			if(rs.next()){
				int nid = rs.getInt("ID");
				String title= rs.getString("TITLE");
				Date regdate= rs.getDate("REGDATE");
				String writerId= rs.getString("WRITER_ID");
				int hit= rs.getInt("HIT"); 
				String files= rs.getString("FILES") ;
				String content= rs.getString("CONTENT");
				boolean pub = rs.getBoolean("PUB");
		 		
				notice = new Notice(
						nid,
						title,
						regdate,
						writerId,
						hit,
						files,
						
						content,
						pub
						);
			}
			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return notice;
	}
	
	public Notice getPrevNotice(int id) {
		Notice notice = null;
		
		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";
		String uid = "NEWLEC";
		String pwd = "89724";
		String driver = "oracle.jdbc.driver.OracleDriver";

		String sql = "SELECT * FROM NOTICE WHERE ID = ? "
				+ "SELECT ID FROM(SELECT ID FROM NOTICE WHERE REGDATE < (SELECT REGDATE FROM NOTICE WHERE ID =? ) ORDER BY REGDATE DESC) "
				+ "WHERE ROWNUM =1)";
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url, uid, pwd);
			PreparedStatement st = con.prepareStatement(sql);
			
			st.setInt(1, id);
			
			ResultSet rs = st.executeQuery();
			
			if(rs.next()){
				int nid = rs.getInt("ID");
				String title= rs.getString("TITLE");
				Date regdate= rs.getDate("REGDATE");
				String writerId= rs.getString("WRITER_ID");
				int hit= rs.getInt("HIT"); 
				String files= rs.getString("FILES") ;
				String content= rs.getString("CONTENT");
				boolean pub = rs.getBoolean("PUB");
		 		
				notice = new Notice(
						nid,
						title,
						regdate,
						writerId,
						hit,
						files,
						
						content,
						pub
						);
			}
			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return notice;
	}
	public int deleteNoticeAll(int[] ids) {

		int result=0;
		
		String params = "";
		
		for(int i=0; i<ids.length; i++) {
			params += ids[i];
			
			if(i != ids.length-1) {
				params+= ", ";
			}
		}
		
		String sql = "DELETE NOTICE WHERE ID IN ("+params+")";
		
		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";
		String uid = "NEWLEC";
		String pwd = "89724";
		String driver = "oracle.jdbc.driver.OracleDriver";

		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url, uid, pwd);
			Statement st = con.createStatement();
			result = st.executeUpdate(sql); //insert, update, delete 용 함수
			
			st.close();
			con.close();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
}
