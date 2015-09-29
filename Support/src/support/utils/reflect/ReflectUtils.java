package support.utils.reflect;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

/**
 * @CopyRight：http://www.netrust.cn/
 *
 * @Description:   
 * @Author: lazite 
 * @CreateTime: 2015年7月24日 下午3:46:58   
 * @ModifyBy: lazite 
 * @ModeifyTime: 2015年7月24日 下午3:46:58   
 * @ModifyDescription:
 * @Version:   V1.0
 */
public class ReflectUtils<T> {
	/**
	 * 通过类名获取类对象的一个实例
	 * @param classname 类名
	 * @return
	 */
	public Object getClassInstance(String classname){
		Object myObj=null;
		try {
			Class<?> c = Class.forName(classname);
			myObj= c.newInstance();
		}catch (Exception e) {
			throw new RuntimeException();
		}
		return myObj;
	}
	
	/**
	 * 通过构造函数实例化对象 
	 * @param className       类的全路径名称	
	 * @param parameterTypes  参数类型
	 * @param initargs        参数值
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Object getClassInstance(String className,Class [] parameterTypes,Object[] initargs) { 
		try {
			Constructor<?> constructor = (Constructor<?>) Class
					.forName(className).getDeclaredConstructor(parameterTypes);					    //暴力反射
			constructor.setAccessible(true);
			return constructor.newInstance(initargs);
		} catch (Exception ex) {
			throw new RuntimeException();
		}

	}
	
	/**
	 * 获取bean的所有属性
	 * @param classname
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getFields(String classname) {
		Map fmap=null;
		 try{
			 Class clazz = Class.forName(classname);//根据类名获得其对应的Class对象 写上你想要的类名就是了 注意是全名 如果有包的话要加上 比如java.Lang.String
			 Field[] fields = clazz.getDeclaredFields();//根据Class对象获得属性 私有的也可以获得
			 fmap=new HashMap<String,Object>();
			 for(Field f : fields) {
				 fmap.put(f.getName(),f.getType());
			 }
		 } catch(Exception e) {
			 e.printStackTrace();
			 throw new RuntimeException();
		 }
		 return fmap;
	}
	
	/**
	 * 获取方法
	 * @param objInstance 对象实例
	 * @param methodName 方法名
	 * @param objParam 方法参数类型
	 * @return
	 */
	public static Method getMethod(Object objInstance,String methodName,Class<?>... parameterTypes){
		Method method=null;
		try {
			method = objInstance.getClass().getMethod(methodName,parameterTypes);
		} catch (Exception e) {
			System.out.println("异常3");
			e.printStackTrace();
		}
		return method;
	}
	
	/**
	 * 动态执行方法
	 * @param objInstance 对象实例
	 * @param method 方法
	 * @param args 方法参数
	 */
	public static void executeMethod(Object objInstance,Method method,Object... params){
		try {
			method.invoke(objInstance,params);
		} catch (Exception e) {
			System.out.println("异常2");
			e.printStackTrace();
		} 
	}
	
		
	/**.
	 * 暴力反射获取字段值
	 * @param fieldName 属性名
	 * @param obj       实例对象
	 * @return          属性值
	 */
	public static Object getFieldValue(String propertyName, Object obj) {
		try {
			Field field = obj.getClass().getDeclaredField(propertyName);			
			field.setAccessible(true);
			return field.get(obj);
		} catch (Exception ex) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * 通过BeanUtils工具包获取反射获取字段值,注意此值是以字符串形式存在的,它支持属性连缀操作:如,.对象.属性
	 * @param propertyName 属性名
	 * @param object       实例对象
	 * @return          字段值
	 */
	public static Object getBeanInfoProperty(String propertyName, Object object) {
		try {			
			return BeanUtils.getProperty(object, propertyName);
		} catch (Exception ex) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * 通过BeanUtils工具包获取反射获取字段值,注意此值是以字符串形式存在的
	 * @param object       实例对象
	 * @param propertyName 属性名
	 * @param value        字段值
	 * @return          
	 */
	public static void setBeanInfoProperty(Object object,String propertyName,String value) {
		try {			
			BeanUtils.setProperty(object, propertyName,value);
		} catch (Exception ex) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * 通过BeanUtils工具包获取反射获取字段值,注意此值是以对象属性的实际类型
	 * @param propertyName 属性名
	 * @param object       实例对象
	 * @return          字段值
	 */
	public static Object getPropertyUtilByName(String propertyName, Object object) {
		try {			
			return PropertyUtils.getProperty(object, propertyName);
		} catch (Exception ex) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * 通过BeanUtils工具包获取反射获取字段值,注意此值是以对象属性的实际类型,这是PropertyUtils与BeanUtils的根本区别
	 * @param object       实例对象
	 * @param propertyName 属性名
	 * @param value        字段值
	 * @return          
	 */
	public static void setPropertyUtilByName(Object object,String propertyName,Object value) {
		try {			
			PropertyUtils.setProperty(object, propertyName,value);
		} catch (Exception ex) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * 设置字段值	
	 * @param obj          实例对象
	 * @param propertyName 属性名
	 * @param value        新的字段值
	 * @return          
	 */
	public static void setProperties(Object object, String propertyName,Object value) throws IntrospectionException,
			IllegalAccessException, InvocationTargetException {
		PropertyDescriptor pd = new PropertyDescriptor(propertyName,object.getClass());
		Method methodSet = pd.getWriteMethod();
		methodSet.invoke(object,value);
	}
	
	
	/**
	 * 设置字段值
	 * @param propertyName 字段名
	 * @param obj          实例对象
	 * @param value        新的字段值
	 * @return          
	 */
	public static void setFieldValue(Object obj,String propertyName,Object value) {
		try {
			Field field = obj.getClass().getDeclaredField(propertyName);		
			field.setAccessible(true);
			field.set(obj, value);
		} catch (Exception ex) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * 设置字段值
	 * @param className        类的全路径名称
	 * @param methodName       调用方法名
	 * @param parameterTypes   参数类型
	 * @param values           参数值
	 * @param object           实例对象
	 * @return          
	 */
	@SuppressWarnings("rawtypes")
	public static Object methodInvoke(String className,String methodName,Class [] parameterTypes,Object [] values,Object object) {
		try {
			Method method = Class.forName(className).getDeclaredMethod(methodName,parameterTypes);
			method.setAccessible(true);
			return method.invoke(object,values);
		} catch (Exception ex) {
			throw new RuntimeException();
		}
	}

}
