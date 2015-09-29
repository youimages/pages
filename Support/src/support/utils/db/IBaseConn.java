package support.utils.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ResourceBundle;

public class IBaseConn {
	private  ResourceBundle rb=null;
	protected Connection ct;
	protected ResultSet rs;	
	protected PreparedStatement ps;
	
	String dataName=null;
	
	public IBaseConn() {
		super();
		rb = ResourceBundle.getBundle("DBConfig");
	}
	
	//创建数据库连接
	public Connection connection(String driver,String url,String username,String password){
		try{
			Class.forName(driver);
			ct = DriverManager.getConnection(url, username, password);	
		} catch (ClassNotFoundException e){
			System.out.println("please check your driver for "+driver);
			e.printStackTrace();
		} catch (SQLException e){
			System.out.println("error in connect "+url+" by username="+username+" and password="+password);
			e.printStackTrace();
		}
		return ct;
	}
	
	//连接指定数据库
	public Connection getNeedConnection() {
		//System.out.println(dataName);
		if(dataName.equals("mysql")){
			return this.connection(rb.getString("driver_mysql"),
								   rb.getString("url_mysql"),
								   rb.getString("username_mysql"),
								   rb.getString("password_mysql"));
		}else if(dataName.equals("sqlserver")){
			return this.connection(rb.getString("driver_sqlserver"),
								   rb.getString("url_sqlserver"),
								   rb.getString("username_sqlserver"),
								   rb.getString("password_sqlserver"));
		}
		return null;
	}
	
	/**
	 * @Title: closeCSR
	 * @Description: 关闭连接资源
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

	/**
	 * @Title: closeCSR
	 * @Description: 关闭连接资源
	 * @param ct Connection
	 * @param st Statement
	 * @param rs ResultSet
	 * @throws: TODO
	 */
	public void closeCSRs(List<Connection> cts,List<Statement> sts,List<ResultSet> rss){
		try{
			if(rss != null){
				for (ResultSet mrs : rss) {
					mrs.close();
					mrs = null;					
				}
			}
		} catch (SQLException e){
			System.out.println("close ResultSet fail in"+rss);
			e.printStackTrace();
		}
		
		try{
			if(sts != null){
				for (Statement mst : sts) {
					mst.close();
					mst = null;					
				}
			}
		} catch (SQLException e){
			System.out.println("close Statement fail in"+sts);
			e.printStackTrace();
		}
		
		try{
			if(cts != null){
				for (Connection mct : cts) {
					mct.close();
					mct = null;					
				}
			}
		} catch (SQLException e){
			System.out.println("close Connection fail in"+rss);
			e.printStackTrace();
		}
	}

	
	
	public Connection getCt() {
		return ct;
	}
	public void setCt(Connection ct) {
		this.ct = ct;
	}
	public ResultSet getRs() {
		return rs;
	}
	public void setRs(ResultSet rs) {
		this.rs = rs;
	}
	public PreparedStatement getPs() {
		return ps;
	}
	public void setPs(PreparedStatement ps) {
		this.ps = ps;
	}
	public String getDataName() {
		return dataName;
	}
	public void setDataName(String dataName) throws Exception {
		this.dataName = dataName;
		getNeedConnection();
	}
}
