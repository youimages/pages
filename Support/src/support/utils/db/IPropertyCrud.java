package support.utils.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import support.utils.reflect.ReflectUtils;

public class IPropertyCrud extends IBaseCrud{
	
	public <T> void execProperty(String sql,T t,String[] up) throws SQLException{
		ct.prepareStatement(sql);
		for(int i = 0; i < up.length; i++){
			ps.setObject(i+1,ReflectUtils.getFieldValue(up[i],t));
		}
		ps.executeUpdate();
	}
	
	public <T> void execBatchAddProperty(String sql,List<T> list,String[] up) throws SQLException{
		ps = ct.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.TYPE_FORWARD_ONLY);
		for(int i = 0; i < list.size(); i++){
			for(int j = 0; j < up.length; j++){
				ps.setObject(j+1,ReflectUtils.getFieldValue(up[j],list.get(i)));
			}
			ps.addBatch();
		}
		ps.executeBatch();
		closeCSR(null, ps, null);
	}
	
	/**
	 * @Title: execBatchUpdateProperty
	 * @Description: TODO
	 * @param sql 执行的sql
	 * @param list 需要插入的实体集合
	 * @param up sql中预编译需要的字段
	 * @param objs 更新的条件
	 * @throws SQLException 
	 * @throws: TODO
	 */
	public <T> void execBatchUpdateProperty(String sql,List<T> list,String[] up,Object[] objs) throws SQLException{
		ps = ct.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.TYPE_FORWARD_ONLY);
		for(int i = 0; i < list.size(); i++){
			for(int j = 0; j < up.length; j++){
				ps.setObject(j+1,ReflectUtils.getFieldValue(up[j],list.get(i)));
			}
			
			for (int j = 0; j < objs.length; j++) {
				ps.setObject(j+up.length+1,objs[j]);
			}
			
			ps.addBatch();
		}
		ps.executeBatch();
		this.closeCSR(null, ps, null);
	}
	
	/**
	 * @Title: execBatchUpdateProperty
	 * @Description: TODO
	 * @param sql 执行的sql
	 * @param list 需要插入的实体集合
	 * @param up sql中预编译需要的字段
	 * @param comObjs 公共的更新条件
	 * @param objs 单独对象的私有更新条件
	 * @throws SQLException 
	 * @throws: TODO
	 */
	public <T> void execBatchUpdateProperty(String sql,List<T> list,String[] up,Object[] comObjs,List<Object[]> objs) throws SQLException{
		ps = ct.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.TYPE_FORWARD_ONLY);

//		System.out.println(sql);
//		System.out.println(objs.size()+"----");
//		System.out.println(list.size()+"----");
//		System.out.println(up.length+"++++");
		
		for(int i = 0; i < list.size(); i++){
			for(int j = 0; j < up.length; j++){
				//System.out.println(up[j]);
				ps.setObject(j+1,ReflectUtils.getFieldValue(up[j],list.get(i)));
			}
			
			if(comObjs!=null){
				for (int j = 0; j < comObjs.length; j++) {
					ps.setObject(j+up.length+1,comObjs[j]);
				}
			}
			
			for (int j = 0; j < objs.get(i).length; j++) {
				ps.setObject(j+(up.length+1)+(((comObjs==null)?0:comObjs.length)),objs.get(i)[j]);
			}

			ps.addBatch();
		}
		ps.executeUpdate();
	}
}
