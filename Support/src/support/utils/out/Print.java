package support.utils.out;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Print {
	private static boolean printMsg=true;
	
	private static boolean ifDefaultDate=true;//是否显示默认日期
	
	private static String YYYYMMDD="yyyy-MM-dd";
	private static String YYYYMMDDHHMMSS="yyyy-MM-dd HH:mm:ss";
	private String DEFAULT_DATE_TYPE=YYYYMMDDHHMMSS;
	
	
	private static boolean ifDefaultSplit=true;//是否显示分隔符
	private String DEFAULT_SPLIT_SYMBOL=SPLIT_SYMBOL_DOTT;//默认分割符
	private static String SPLIT_SYMBOL_DOTT="-------------------------------------";//虚线分隔符
	private static String SPLIT_SYMBOL_ASTERISK="************************************";//虚线分隔符
	
	
		
	public String getDEFAULT_SPLIT_SYMBOL() {
		return DEFAULT_SPLIT_SYMBOL;
	}
	public void setDEFAULT_SPLIT_SYMBOL(String dEFAULT_SPLIT_SYMBOL) {
		DEFAULT_SPLIT_SYMBOL = dEFAULT_SPLIT_SYMBOL;
	}
	public String getDEFAULT_DATE_TYPE() {
		return DEFAULT_DATE_TYPE;
	}
	public void setDEFAULT_DATE_TYPE(String dEFAULT_DATE_TYPE) {
		DEFAULT_DATE_TYPE = dEFAULT_DATE_TYPE;
	}
		
	/**
	 * 打印消息，
	 * @param msg 消息内容
	 */
	public static void out(String msg){
		if(printMsg){
			out(msg,true);
		}
	}
	
	/**
	 * 打印消息，
	 * @param msg 消息内容
	 * @param tr 换行
	 */
	public static void out(String msg,boolean tr){
		if(printMsg){
			System.out.print(msg+(tr?"\n":""));
		}
	}
	
	public static <T> void out(T t){
		if(printMsg){
			if(ifBasicType(t)){
				switch (isBasicType(t)) {
				case 0:
					System.out.println(t);
					break;
				case 1:
					outDate(t);
					break;
				case 2:
					outCollection(t);
					break;
				default:
					out(t.getClass().getSimpleName()+" is not judge type in selected :"+t.toString());
					break;
				}
			}else{
				out(t,null);
			}
		}
	}
	
	/**
	 * @Title: out
	 * @Description: 
	 * @param fsp 开始分割符
	 * @param t 打印对象
	 * @param esp 结束分隔符
	 * @throws: TODO
	 */
	public static <T> void out(String fsp,T t,String esp){
		if(printMsg){
			if(fsp==null)
				fsp="";
			if(esp==null)
				esp="";
			
			String temp="";
			switch (isBasicType(t)) {
				case 0:
				case 1:
					if(ifBasicDate(t)){
						if(ifDefaultDate)
							temp=new SimpleDateFormat(YYYYMMDDHHMMSS).format((Date)t);
						else
							temp=new SimpleDateFormat(new Print().getDEFAULT_DATE_TYPE()).format((Date)t);
					}else{
						temp=String.valueOf(t);
					}
					break;
				case -1:	
				case 2:
					temp=t.toString();
					break;
				default:
					break;
			}

			out(fsp+temp+esp);
		}
	}
	
	public static void outDate(Date t){
		if(printMsg){
			if(ifDefaultDate)
				out(new SimpleDateFormat(YYYYMMDDHHMMSS).format((Date)t));
			else
				out(new SimpleDateFormat(new Print().getDEFAULT_DATE_TYPE()).format((Date)t));
		}
	}
	
	public static void outDate(Timestamp t){
		if(printMsg){
			if(ifDefaultDate)
				out(new SimpleDateFormat(YYYYMMDDHHMMSS).format((Timestamp)t));
			else
				out(new SimpleDateFormat(new Print().getDEFAULT_DATE_TYPE()).format((Timestamp)t));
		}
	}
	
	
	public static <T> void outDate(T t){
		if(printMsg){
			switch (isBasicDate(t)) {
			case 0:
				outDate((Date)t);
			case 1:
				outDate((Timestamp)t);
			default:
				out(t);
				break;
			}
		}
	}

	public static <T> void outCollection(T t){
		if(printMsg){
			if(ifBasicCollections(t)){
				switch (isBasicCollections(t)) {
				case 0:
					outList_Line((List)t);
					break;
				case 1:
					outMap_Line((Map)t);
					break;
				default:
					break;
				}
			}else{
				out(t);
			}
		}
	}
	
	public static void outList_Line(List list){
		if(printMsg){
			out(list.toString());
		}
	}
	
	public static <T> void outList_Iter(List<T> list){
		if(printMsg){
			if(list!=null&&list.size()!=0){
				for (T t : list) {
					out(t);					
	 			}
			}
		}
	}
	
	public static <T> void outList_Iter(List<T> list,String... ss){
		if(printMsg){
			if(list!=null&&list.size()!=0){
				for (T t : list) {
					out(t,ss);					
				}
			}
		}
	}
	
	public static <T> void outMap_Line(Map map){
		if(printMsg){
			out("键:{"+map.keySet().toString()+"}");
			out("值:{"+map.values().toString()+"}");
		}
	}
	
	public static <K,V> void outMap_Iter(Map<K,V> map){
		if(printMsg){
			if(map!=null){
			   int k=1;
			   for (Map.Entry<K, V> entry : map.entrySet()) {
				   out("map的第"+k+"个键值对"+((ifDefaultSplit)?new Print().getDEFAULT_SPLIT_SYMBOL():""));
				   out(entry.getKey());
				   if(entry.getValue()!=null)
					   out(entry.getValue());
				   else
					   out("NULL");
				   k++;
			   }
			}	
		}
	}
	
	/**
	 * @Title: outT
	 * @Description: 输出普通对象的toString
	 * @param t
	 * @param String[] ss 属性名称数组
	 * @throws: TODO
	 */
	public static <T> void out(T t,String... ss){
		if(printMsg){
			if(!ifBasicType(t)){
				String ts=t.getClass().getSimpleName()+":[";
				if(ss!=null){
					for (int i=0;i<ss.length;i++) {
						try {
							Field field = t.getClass().getDeclaredField(ss[i]);			
							field.setAccessible(true);
							ts=ts+"\""+ss[i]+"\":"+field.get(t).toString()+",";
						} catch (Exception e) {
							e.printStackTrace();
						}
					} 
					ts=ts.substring(0, ts.length()-2)+"]";
					out(ts);
				}else{					
					out(t.toString());
				}
			}else{
				out(t);				
			}
		}
		
	}
	
	
	
	private static <T> int isBasicType(T t){
		String typeName = t.getClass().getName();
		if(typeName.equals("int")||typeName.equals("long")||typeName.equals("java.lang.String")
				||typeName.equals("float")||typeName.equals("double")||typeName.equals("boolean"))
			return 0;
		if(t instanceof java.lang.Integer||t instanceof java.lang.Long|| t instanceof java.lang.String
				||t instanceof java.lang.Float||t instanceof java.lang.Double||t instanceof java.lang.Boolean)
			return 0;
		if(t instanceof java.util.Date||t instanceof java.sql.Timestamp)
			return 1;
		if(ifBasicCollections(t))
			return 2;
		return -1;
	}
	
	private static <T> boolean ifBasicType(T t){
		String typeName = t.getClass().getName();
		if (typeName.equals("java.lang.String")) 
			return true;
		if (typeName.equals("int")|| typeName.equals("java.lang.Integer")) 
			return true;
		if (typeName.equals("long")|| typeName.equals("java.lang.Long")) 
			return true;
		if (typeName.equals("float")|| typeName.equals("java.lang.Float")) 
			return true;
		if (typeName.equals("double")|| typeName.equals("java.lang.Double")) 
			return true;
		if (typeName.equals("boolean")|| typeName.equals("java.lang.Boolean")) 
			return true;
		if (typeName.equals("java.util.Date")||typeName.equals("java.sql.Timestamp"))	
			return true;
		if (ifBasicCollections(t))
			return true;
		return false;
	}
	
	
	private static <T> int isBasicDate(T t){
		String typeName = t.getClass().getName();
		if (typeName.equals("java.util.Date"))	
			return 0;
		if (typeName.equals("java.sql.Timestamp"))	
			return 1;
		return -1;		
	}
	
	private static <T> boolean ifBasicDate(T t){
		String typeName = t.getClass().getName();
		if (typeName.equals("java.util.Date")||typeName.equals("java.sql.Timestamp"))	
			return true;
		return false;		
	}
	
	private static <T> int isBasicCollections(T t){
		if(ifBasicList(t))
			return 0;
		if(ifBasicMap(t))
			return 1;
		return -1;
	}
	
	private static <T> boolean ifBasicCollections(T t){
		if(ifBasicList(t)||ifBasicMap(t))
			return true;
		return false;	
	}
	
	private static <T> boolean ifBasicList(T t){
		if(t instanceof java.util.List)
			return true;
		return false;		
	}
	
	private static <T> boolean ifBasicMap(T t){
		if(t instanceof java.util.Map)
			return true;
		return false;		
	}
	
	
	
	//测试
	public static void main(String[] ss) {
		String s1="测试字符串打印";
//		Print.out(s1);
		
		Integer s2=12;
		int s3=13;
//		Print.out(s2);		
//		Print.out(s3);
		
		List<String> s4=Arrays.asList(new String[]{"测试list集合打印1","测试list集合打印2"});
//		Print.out(s4);
//		Print.outCollection(s4);
//		Print.outList_Iter(s4);
		
//		List<User> s5=new ArrayList<User>();
//		s5.add(new User(1, "111", "111", 1, new Date(), null, 14, 1, null, null));
//		s5.add(new User(1, "222", "222", 2, new Date(), null, 222, 2, null, null));
//		
//		Print.outList_Line(s5);
//		Print.outList_Iter(s5);
//		Print.outList_Iter(s5,"account","loginTime");
//		
//		Print.out(new User(1, "对象测试", "111", 1, new Date(), null, 14, 1, null, null));
		
		
		
	}
}
