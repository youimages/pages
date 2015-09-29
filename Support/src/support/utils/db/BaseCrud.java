package support.utils.db;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;


public class BaseCrud extends BaseConn{
	private  ResourceBundle rb=null;
	
	@SuppressWarnings("rawtypes")
	RowMapper rowMapper =null;
	
	@SuppressWarnings("rawtypes")
	public BaseCrud(){
		rb = ResourceBundle.getBundle("DBConfig");
		rowMapper = new RowMapper();	
	}
	
	public HashMap<String,String> getBundle(String driver,String url,String username,String password){
		HashMap<String ,String> map=new HashMap<String,String>();
		
		map.put("driver", rb.getString(driver));
		map.put("url", rb.getString(url));
		map.put("username", rb.getString(username));
		map.put("password", rb.getString(password));
		
		return map;
	}
	
	//����MySql��ݿ�
	public Connection connMySql(String dataName){
		HashMap<String,String> map=null;
		String driver=null;
		String url=null;
		String username=null;
		String password=null;
		if(dataName.equals(rb.getString("mysql"))){
			 driver="driver_mysql";
			 url="url_mysql";
			 username="username_mysql";
			 password="password_mysql";
			 map=getBundle(driver,url,username,password);
			 return this.connection(map.get("driver"),map.get("url"),map.get("username"),map.get("password"));
		}
		
		if(dataName.equals(rb.getString("sqlserver"))){
			driver="driver_sqlserver";
			url="url_sqlserver";
			username="username_sqlserver";
			password="password_sqlserver";
			map=getBundle(driver,url,username,password);
			return this.connection(map.get("driver"),map.get("url"),map.get("username"),map.get("password"));
		}
		
		return null;
	}
	
//	public Statement execUpdate() throws Exception{
//		// ��ȡԤ���뻷��
//		ps = this.connMySql(dataName).prepareStatement(sql);
//		if(objs != null && objs.length > 0){
//			for(int i = 0; i < objs.length; i++){
//				ps.setObject(i+1, objs[i]);
//			}
//		}
//		
//		return ps
//	}
	
	
			
	/**
	 * ������ݿ������������ɾ�Ĳ�����
	 * @param sql  ��ִ��sql���
	 * @param objs ��������Ԥ��������д���Ĳ���
	 * @return null
	 * @throws Exception 
	 */
	public void execUpdate(String sql,String dataName, Object ...objs) throws Exception{
		// ��ȡԤ���뻷��
		ps = this.connMySql(dataName).prepareStatement(sql);
		if(objs != null && objs.length > 0){
			for(int i = 0; i < objs.length; i++){
				ps.setObject(i+1, objs[i]);
			}
		}
		// ִ�и������
		 ps.executeUpdate();
		// �Ͽ����ӣ��ͷ���Դ
		this.closeCSR(ct, ps, rs);
	}
		
	/**
	 * ��ݿ��ѯ����
	 * @param sql ��ִ��sql���
	 * @param objs ��������Ԥ��������д���Ĳ���
	 * @return ��T��List������ͣ������ز�ѯ�������������Ϣ
	 * @throws Exception 
	 */
	public <T> List<T> execQuery(String sql, RowMapper<T> mapper, String dataName,Object[] objs) throws Exception{
		// ��ȡԤ���뻷��
		ps = this.connMySql(dataName).prepareStatement(sql);
		if(objs != null && objs.length > 0){
			for(int i = 0; i < objs.length; i++){
				ps.setObject(i+1, objs[i]);
			}
		}
		// ִ�и������
		rs = ps.executeQuery();
		// ִ�й�ϵ�������ӳ��
		List<T> result = mapper.mappingRows(rs);
		// �Ͽ����ӣ��ͷ���Դ
		this.closeCSR(ct, ps, rs);
		return result;
	}	
		
		
	public List<HashMap<String,Object>> query(String sql, String dataName,Object ...objs) throws Exception{
		// ��ȡԤ���뻷��
		ps = this.connMySql(dataName).prepareStatement(sql);
		if(objs != null && objs.length > 0){
			for(int i = 0; i < objs.length; i++){
				ps.setObject(i+1, objs[i]);
			}
		}
		// ִ�и������
		rs = ps.executeQuery();
		// ִ�й�ϵ�������ӳ��
		List<HashMap<String,Object>> result = rowMapper.getListMap(rs);
		// �Ͽ����ӣ��ͷ���Դ
		this.closeCSR(ct, ps, rs);
		return result;
	}	
		

	/**
	 * ��ȡ�������ֵ
	 * @param sql
	 * @param dataName
	 * @param updateColumn
	 * @return
	 * @throws SQLException
	 */
	public Timestamp getMaxUpdateTime(String sql,String dataName,String updateColumn) throws SQLException{
		
		ps = this.connMySql(dataName).prepareStatement(sql);
		
		rs = ps.executeQuery();
		
		while (rs.next()){
			return rs.getTimestamp(updateColumn);
		}
		
		this.closeCSR(ct, ps, rs);
		return null;
	}
		
	
	
	/**
	 * 
	 * @param insertsql  	����sql���
	 * @param updatesql  	�޸�sql���
	 * @param dataName   	��ݿ���
	 * @param insertlist    Ҫ���뱾����ݿ�����
	 * @param updatelist    Ҫ�޸ı�����ݿ�����
	 * @param cal
	 * @throws SQLException
	 */
	public  <T> void batchInsert(String insertsql,String updatesql,String dataName,List<Object[]> insertlist,List<Object[]> updatelist,Class<T> cal) throws SQLException{
		
		try {
			this.connMySql("mysql").setAutoCommit(false);
			T object = cal.newInstance(); 
			// �õ������ֶμ���
			Field[] fields = object.getClass().getDeclaredFields();
			
			//��������
			ps = this.connMySql(dataName).prepareStatement(insertsql,rs.TYPE_SCROLL_SENSITIVE,rs.TYPE_FORWARD_ONLY);
			
			for(int i = 0; i < insertlist.size(); i++){
				for(int j = 0; j < fields.length; j++){
					
					ps.setObject(j+1, insertlist.get(i)[j]);
					
				}
				ps.addBatch();   
			}
			//�����޸�
			ps = this.connMySql(dataName).prepareStatement(insertsql,rs.TYPE_SCROLL_SENSITIVE,rs.TYPE_FORWARD_ONLY);
			
			for(int i = 0; i < updatelist.size(); i++){
				for(int j = 0; j < fields.length; j++){
					
					ps.setObject(j+1, updatelist.get(i)[j]);
					
				}
				ps.addBatch();   
			}
			
			
			ps.executeBatch();   
			this.connMySql("mysql").commit();   
			 
		} catch (Exception e) {
			this.connMySql("mysql").rollback();
			e.printStackTrace();
		}finally{
			this.closeCSR(ct, ps, rs);
		}
	}
		
		
		
		
		
}
