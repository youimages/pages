package support.utils.db;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.List;

public class IBaseCrud extends IBaseConn{
	
	public IBaseCrud() {
		super();
	}
	
	/**
	 * 更新数据库操作（包括增删改操作）
	 * @param sql  待执行sql语句
	 * @param objs 用于设置预编译语句中带入的参数
	 * @return null
	 * @throws Exception 
	 */
	public void execUpdate(String sql,List<Object> objs) throws Exception{
		ct.prepareStatement(sql);

		if(objs != null && objs.size() > 0){
			for(int i = 0; i < objs.size(); i++){
				ps.setObject(i+1, objs.get(i));
			}
		}
		
		ps.executeUpdate();
	}
	
	public <T> void batchInsert(String sql,List<List<Object>> list,Class<T> cal) throws Exception{
		ps = super.getNeedConnection().prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.TYPE_FORWARD_ONLY);
		
		//this.connMySql("mysql").setAutoCommit(false);
		T object = cal.newInstance(); 
		// 得到结果集的字段集合
		Field[] fields = object.getClass().getDeclaredFields();
		
		for (Field field : fields) {
			//System.out.println(field.getName());
		}
		//System.out.println(list.size());
		
		for(int j = 0; j < list.size(); j++){
			if(list.get(j)==null){
				ps.setObject(j+1,"");					
			}else{
				ps.setObject(j+1,list.get(j));										
		}
			
			ps.addBatch();   
		}

//		ps.executeBatch();   
//		this.connMySql("mysql").commit(); 
	}
	
	
	public <T> void batchInsert(String sql,List<List<Object>> list) throws Exception{
		ps = super.getNeedConnection().prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.TYPE_FORWARD_ONLY);
		
		for(int i = 0; i < list.size(); i++){
			for (int j = 0; j < list.get(i).size(); j++) {
				ps.setObject(j+1,list.get(i).get(j));														
			}
			ps.addBatch();   
		}	
	}
	

	/**
	 * 数据库查询操作
	 * @param sql 待执行sql语句
	 * @param objs 用于设置预编译语句中带入的参数
	 * @return 类T的List数据类型，即返回查询到的所有数据信息
	 * @throws Exception 
	 */
	public <T> List<T> execQuery(String sql,RowMapper<T> mapper,List<Object> objs) throws Exception{
		ps=ct.prepareStatement(sql);
		
		if(objs != null && objs.size() > 0){
			for(int i = 0; i < objs.size(); i++){
				System.out.println( objs.get(i));
				ps.setObject(i+1, objs.get(i));
			}
		}

		// 执行更新语句
		rs = ps.executeQuery();
		// 执行关系到对象的映射
		List<T> result = mapper.mappingRows(rs);
		
		super.closeCSR(null, ps, rs);
		// 断开连接，释放资源
		return result;
	}

	
}
