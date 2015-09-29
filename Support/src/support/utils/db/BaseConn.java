package support.utils.db;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BaseConn{
	Connection ct;
	PreparedStatement ps;
	CallableStatement cs;
	ResultSet rs;
	
	public Connection connection(String driver,String url,String username,String password){
		try{
			Class.forName(driver);
			ct = DriverManager.getConnection(url, username, password);	
		} catch (ClassNotFoundException e){
			System.out.println("please check your drive for "+driver);
			e.printStackTrace();
		} catch (SQLException e){
			System.out.println("error in connect "+url+"by username="+username+" and password="+password);
			e.printStackTrace();
		}
		return ct;
	}
	
	/**
	 * @Title: closeCSR
	 * @Description: �ر�������Դ
	 * @param ct Connection
	 * @param st Statement
	 * @param rs ResultSet
	 * @throws: TODO
	 */
	public void closeCSR(Connection ct,Statement st,ResultSet rs){
		try{
			if(rs != null){
				rs.close();
				rs = null;
			}
		} catch (SQLException e){
			System.out.println("close ResultSet fail in"+rs);
			e.printStackTrace();
		}
		
		try{
			if(st != null){
				st.close();
				st = null;
			}
		} catch (SQLException e){
			System.out.println("close Statement fail in"+st);
			e.printStackTrace();
		}
		
		try{
			if(ct != null){
				ct.close();
				ct = null;
			}
		} catch (SQLException e){
			System.out.println("close Connection fail in"+ct);
			e.printStackTrace();
		}
	}
}
