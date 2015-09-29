package support.utils.xml;

import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SaxUtil<T>{
	private String xmluri;
	
	public SaxUtil(){}
	
	public SaxUtil(String xmluri){
		this.xmluri = xmluri;
	}
	
	//测试方法，解析一个url
	public void parse(T t)	{
		//得到一个sax解析器工厂
		SAXParserFactory saxfactory = SAXParserFactory.newInstance();
		try {
			//得到sax解析器，并在parse解析方法中传入handler对象辅助解析
			SAXParser parser = saxfactory.newSAXParser();
			parser.parse(xmluri, new SaxHandler(t.getClass()));//t.getClass()慎重
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//从流中解析xml文件
	public List<T> parse(InputStream is,T t)
	{
		//得到一个sax解析器工厂
		SAXParserFactory saxfactory = SAXParserFactory.newInstance();
		try {
			//得到sax解析器，并在parse解析方法中传入handler对象辅助解析
			SAXParser parser = saxfactory.newSAXParser();
			//实例化一个解析器辅助handler，解析事件处理方法都包含在其中
			SaxHandler handler = new SaxHandler(t.getClass());
			parser.parse(is, handler);
			is.close();
			//返回handler解析后的list集合
			return handler.getobjs();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param cls   实体类中方法的形参参数类型
	 * @param obj	实体类set方法传入的参数对象
	 * @return      根据参数类型转换参数对象值
	 */
	public static Object changeType(Class<?> cls, Object obj){
		//根据cls名称获取对应类型枚举值，没有返回EMPTY
		TypeEnum te = TypeEnum.valueOf(cls.getSimpleName());
		String value = obj.toString();
		switch (te) {
			case BOOLEAN:
				return Boolean.valueOf(value);
			case BYTE:
				return Byte.valueOf(value);
			case CHAR:
				return value.charAt(0);
			case DOUBLE:
				return Double.valueOf(value);
			case SHORT:
				return Short.valueOf(value);
			case FLOAT:
				return Float.valueOf(value);
			case INT:
				return Integer.valueOf(value);
			case LONG:
				return Long.valueOf(value);
			case STRING:
				return value;
			default:
				//不对应以上java自带基本类型，返回原始obj对象
				return obj;
		}
	}
}
