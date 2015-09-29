package support.utils.xml;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxHandler<T> extends DefaultHandler{
	/** 当前索引的xml标签值  */
	private String tagname = null;
	/** 实体类class */
	private Class<T> cls;
	/** 保存实体类对象list集合  */
	private List<T> objs;
	/** 存放对应实体类和起复合成员变量对象的method[]数组的 hash表 */
	private Map<Class<?>, Method[]> methodmap;
	/** 用于保存当前正在操作实体类的栈，里面保存有实体类和其复合成员字段的实体类  */
	private Stack<Object> stack;
	
	/**
	 * 
	 * @return   返回解析后的实体类集合
	 */
	public List<T> getobjs(){
		return objs;
	}
	
	/**
	 * 初始化类
	 * @param cls
	 */
	public SaxHandler(Class<T> cls){
		this.cls = cls;
		this.stack = new Stack<Object>();
		this.methodmap = new HashMap<Class<?>, Method[]>();
		this.methodmap.put(cls, cls.getDeclaredMethods());
	}
	
	@Override
	public void startDocument() throws SAXException {
		// 开始解析 new一个新集合
		objs = new ArrayList<T>();
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		//根据栈是否为空来获取默认实体类或者字段所对应qName名称的方法
		Method method = stack.empty() ? 
				  getMethod(cls, qName) : getMethod(stack.peek().getClass(), qName);		
		try {
			if (method != null) {
				//获取方法形参类型，这里定义set方法只有一个参数
				Class<?> paramtype = method.getParameterTypes()[0];
				//获取对应参数类型的枚举值，并设置标签属性值
				TypeEnum te = TypeEnum.valueOfTE(paramtype.getSimpleName());
				if (te == TypeEnum.EMPTY) {
					setAttr(paramtype, attributes);
				}
			} else if (cls.getSimpleName().equals(qName)) {
				//当前标签为实体类标签时，设置实体类对应的xml标签属性值
				setAttr(cls, attributes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//设置当前索引到的标签
		tagname = qName;
	}
	
	/**
	 * 
	 * @param paramtype     形参类型
	 * @param attributes    属性值对象
	 * @throws Exception
	 */
	private void setAttr(Class<?> paramtype, Attributes attributes) throws Exception{
		//实例化形参class对应的对象，并入栈
		stack.push(paramtype.newInstance());
		if (!methodmap.containsKey(paramtype)) {
			//如果method的hash表不包含此class的方法，则保存方法数组到hash表
			methodmap.put(paramtype, paramtype.getDeclaredMethods());
		}
		//遍历设置属性值
		for (int i=0; i < attributes.getLength(); i++) {
			invokeSetMethod(stack.peek(), 
                            attributes.getLocalName(i), attributes.getValue(i));
		}
	}
	
	@Override
	public void endDocument() throws SAXException {
		// 解析结束
		super.endDocument();
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		//当前标签值是实体类标签，stack出栈，并添加到实体类集合中
		if (cls.getSimpleName().equals(qName)) {
			objs.add((T) stack.pop());
		} else if (!stack.empty()) {
			try {
				//当栈不为空时，获取栈顶元素的class名称，判断是否对应qName标签值
				String clsname = stack.peek().getClass().getSimpleName();
				if (clsname.equals(qName)) {
					/*
					 * 第一次弹出栈的obj为实体类对应的set方法的形参对象，
					 * 出栈后，栈顶元素就为调用改set方法的对象。
					 */
					Object parent = stack.pop();
					invokeSetMethod(stack.peek(), qName, parent);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		tagname = null;
	}
	
	@Override
	public void characters(char[] ch, int start, int length)throws SAXException {
		//解析字符内容
		if (tagname == null) return;
		
		String name = null;
		Method[] methods = stack.empty() ?
				     methodmap.get(cls) : methodmap.get(stack.peek().getClass());
		
		//遍历method数组，如果当前tagname标签对应其中一个method，则执行该method
		for (Method method: methods) {
			name = setMethodName(tagname);
			if (name.equals(method.getName())) {
				try {
					/*
					 * 当前栈顶元素为调用改method方法的对象
					 * new String(ch, start, length) 为标签值
					 */
					invokeSetMethod(stack.peek(), 
                                    tagname, new String(ch, start, length));
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/*
	 * 此方法待合并
	 * @param    name 符合javabean规范的类的方法名
	 * @return   返回getXXX形式无参数方法名
	 */
	private String setMethodName(String name){
		StringBuilder sb = new StringBuilder(name);
		//替换首字符为大写
		char first = sb.charAt(0);
		sb.setCharAt(0, Character.toUpperCase(first));
		sb.insert(0, "set");
		return sb.toString();
	}
	
	/**
	 * 
	 * @param obj        执行方法的对象
	 * @param mname      方法名称
	 * @param args       方法参数列表
	 * @throws Exception
	 */
	private void invokeSetMethod(Object obj, String mname, Object...args) throws Exception{
		//根据对象class和方法名称获取method，这里指定set方法不重复，并且只有一个形参
		Method method = getMethod(obj.getClass(), mname);
		//得到该方法的形参class列表
		Class<?>[] types = method.getParameterTypes();
		method.invoke(obj, SaxUtil.changeType(types[0], args[0]));
	}
	
	/**
	 * 
	 * @param cls     获取method的class类
	 * @param name    方法名称
	 * @return        返回获取的method，没有则返回null
	 */
	private Method getMethod(Class<?> cls, String name){
		name = setMethodName(name);
		//在已有的method数组hash表中获取已经保存的方法数组
		Method[] methods = methodmap.get(cls);
		for (Method method: methods) {
			if (name.equals(method.getName())) {
				return method;
			}
		}
		return null;
	}
	
}

