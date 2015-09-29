package support.utils.db;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * ˵����ʵ�ֽ��pojo�����ӳ��
 */
public class RowMapper<T>{
	private Class<T> objectClass;
	
	public RowMapper(){}

	public RowMapper(Class<T> objectClass) {
		this.objectClass = objectClass;
	}
	
	/**
	 * ʵ�ֵ�����¼�������ӳ��
	 * @param rs ���
	 * @return
	 * @throws SQLException
	 */
	public T mappingRow(ResultSet rs) throws SQLException{
		try {
			T object = objectClass.newInstance(); 
			// �õ������ֶμ���
			ResultSetMetaData metaData = rs.getMetaData();
			int columnNum = metaData.getColumnCount();
			Field[] fields = object.getClass().getDeclaredFields();
			// ���ö������Ե�ֵ���������ڣ�������Ϊnull.
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				int flag = 0;
				for (int j = 1; j <= columnNum; j++) {
					if (metaData.getColumnName(j).toLowerCase().equals(field.getName().toLowerCase())) {
						flag = 1;
						break;
					}
				}
				field.setAccessible(true);
				if (flag == 1) {
					this.typeMapper(field, object, rs);
				}else {
					field.set(object, null);
				}	
				field.setAccessible(false);
			}
			return object;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ʵ�ֶ�����¼�������ӳ��
	 * @param rs ���
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SQLException
	 */
	public List<T> mappingRows(ResultSet rs) throws SQLException {
		List<T> objList = new ArrayList<T>();
		try {
			while(rs.next()){
				objList.add(this.mappingRow(rs));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return objList;
	}
	
	/**
	 * @Title: parseResultSet2Map
	 * @Description: ��ResultSetת��Ϊmap����
	 * @param rs
	 * @return
	 * @throws: TODO
	 */
	public HashMap<String,Object> parseResultSet2Map(ResultSet rs){
		HashMap<String,Object> map=new HashMap<String,Object>();	
		try {
			// �õ������ֶμ���
			ResultSetMetaData metaData = rs.getMetaData();
			//��ȡ���У��ֶε�����
			int columnNum = metaData.getColumnCount();
			for(int i=1;i<columnNum+1;i++){
				//��ȡ������
				String columnName=metaData.getColumnName(i);
				Object obj=typeMappers(columnName,rs);
				map.put(columnName, obj);
			}
			return map;
		} catch (Exception e) {
			System.out.println("check your sql result for method 'parseResultSet2Map'");
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * �������
	 * @param rs
	 * @return
	 */
	public List<HashMap<String,Object>> getListMap(ResultSet rs){
		List<HashMap<String,Object>> objList = new ArrayList<HashMap<String,Object>>();
		try {
			while(rs.next()){
				objList.add(this.parseResultSet2Map(rs));
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("check your sql result for method 'getListMap' or while in 'getListMap'");
			e.printStackTrace();
		}
		return objList;
	}
	
	
	/**
	 * ���͵�ӳ��
	 * @param field
	 * @param obj
	 * @param rs
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	private void typeMapper(Field field, Object obj, ResultSet rs) {
		String typeName = field.getType().getName(); // �õ��ֶ�����
		try {
			if (typeName.equals("java.lang.String")) {
				field.set(obj, rs.getString(field.getName()));
			} else if (typeName.equals("int")
					|| typeName.equals("java.lang.Integer")) {
				field.set(obj, rs.getInt(field.getName()));
			} else if (typeName.equals("long")
					|| typeName.equals("java.lang.Long")) {
				field.set(obj, rs.getLong(field.getName()));
			} else if (typeName.equals("float")
					|| typeName.equals("java.lang.Float")) {
				field.set(obj, rs.getFloat(field.getName()));
			} else if (typeName.equals("double")
					|| typeName.equals("java.lang.Double")) {
				field.set(obj, rs.getDouble(field.getName()));
			} else if (typeName.equals("boolean")
					|| typeName.equals("java.lang.Boolean")) {
				field.set(obj, rs.getBoolean(field.getName()));
			} else if (typeName.equals("java.util.Date")) {
				field.set(obj, rs.getTimestamp(field.getName()));
			} else if (typeName.equals("java.sql.Timestamp")) {
				field.set(obj, rs.getTimestamp(field.getName()));
			}else {
			}
		} catch (IllegalArgumentException e) {			
			e.printStackTrace();
		} catch (IllegalAccessException e) {			
			e.printStackTrace();
		} catch (SQLException e) {			
			e.printStackTrace();
		}
	}
	
	private Object typeMappers(String columnName,ResultSet rs) {
		try {
			return rs.getObject(columnName);
		} catch (IllegalArgumentException e) {			
			e.printStackTrace();
		}  catch (SQLException e) {			
			e.printStackTrace();
		}
		return null;
	}
	
	
	
}