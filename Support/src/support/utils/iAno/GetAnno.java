package support.utils.iAno;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import support.utils.iAno.FieldMeta.FieldMeta;
import support.utils.iAno.pojo.SortableField;

public class GetAnno{
	private Class<?> clazz;
	
	

	public GetAnno() {
		super();
	}	
	public GetAnno(Class<?> clazz) {
		super();
		this.clazz = clazz;
	}
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	/**
	 * @Title: getClassAnno
	 * @Description: 获取类注解
	 * @param clazz
	 * @return
	 * @throws: TODO
	 */
	public static List<SortableField> getClassAnno(Class<?> clazz){
		List<SortableField> list=new ArrayList<SortableField>();
		Annotation[] classAnnotation = clazz.getAnnotations();
		
	    for(Annotation cAnnotation : classAnnotation){
	    	FieldMeta fm = (FieldMeta)cAnnotation;
	    	SortableField sField=new SortableField(fm,clazz.getSimpleName(),clazz);
	    	list.add(sField);
	    }
	    return list;
	}
	
	/**
	 * @Title: getPorpertyAnno
	 * @Description: 获取指定对象的某一个属性的注解
	 * @param clazz	对象类型
	 * @param porpertyName 对象属性名
	 * @return
	 * @throws: TODO
	 */
	public SortableField getPorpertyAnno(String porpertyName){
		SortableField sField=null;
		try {
			Field f=clazz.getField(porpertyName);
			Annotation annotation = f.getAnnotation(FieldMeta.class);	      
	    	if (annotation!=null) {
				FieldMeta fm = (FieldMeta)annotation;
				sField=new SortableField(fm,f.getName(),clazz);
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sField;
	}
	
	/**
	 * @Title: getPorpertyAnno
	 * @Description: 获取指定对象的某一个属性的注解
	 * @param clazz	对象类型
	 * @param porpertyName 对象属性名
	 * @return
	 * @throws: TODO
	 */
	public static SortableField getPorpertyAnno(Class<?> clazz,String porpertyName){
		SortableField sField=null;
		try {
			Field f=clazz.getField(porpertyName);
			Annotation annotation = f.getAnnotation(FieldMeta.class);	      
			if (annotation!=null) {
				FieldMeta fm = (FieldMeta)annotation;
				sField=new SortableField(fm,f.getName(),clazz);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sField;
	}
	
	/**
	 * @Title: getPorpertyAnno
	 * @Description: 获取指定对象的某一个属性的注解
	 * @param clazz	对象类型
	 * @param porpertyName 对象属性名
	 * @return
	 * @throws: TODO
	 */
	public static List<SortableField> getPorpertyAnno(Class<?> clazz,String... porpertyNames){
		List<SortableField> list=new ArrayList<SortableField>();
		SortableField sField=null;
		try {
			for (String pn : porpertyNames) {
				Field f=clazz.getField(pn);
				Annotation annotation = f.getAnnotation(FieldMeta.class);	      
				if (annotation!=null) {
					FieldMeta fm = (FieldMeta)annotation;
					sField=new SortableField(fm,f.getName(),clazz);
					list.add(sField);
				}				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static List<SortableField> getPorpertyAnno(Class<?> clazz){
		List<SortableField> list=new ArrayList<SortableField>();
		Field[] fields = clazz.getDeclaredFields();
	    for(Field f : fields){
	    	SortableField sField=new SortableField();
	    	String filedName = f.getName();
//	    	System.out.println("属性名称:【"+filedName+"】");
//	    	1、获取属性上的指定类型的注释
	    	Annotation annotation = f.getAnnotation(FieldMeta.class);	      
//	    	有该类型的注释存在
	    	if (annotation!=null) {
				//强制转化为相应的注释	
				FieldMeta xmlElement = (FieldMeta)annotation;
				sField.setMeta(xmlElement);
				sField.setName(filedName);
				sField.setField(f);
				list.add(sField);
//				3、获取属性上的指定类型的注释的指定方法
//				具体是不是默认值可以去查看源代码
//				if (xmlElement.name().equals("##default")) {
//				  System.out.println("属性【"+filedName+"】注释使用的name是默认值: "+xmlElement.name());
//				}else {
//				  System.out.println("属性【"+filedName+"】注释使用的name是自定义的值: "+xmlElement.name());
//				}
	    	}
	    }
	    return list;
	}

	public static List<SortableField> getMethodAnno(Class<?> clazz){
		List<SortableField> list=new ArrayList<SortableField>();
		Method[] methods=clazz.getDeclaredMethods();
		for(Method m : methods){
			SortableField sField=new SortableField();
	    	String filedName = m.getName();
	    	Annotation annotation = m.getAnnotation(FieldMeta.class);	      
	    	if (annotation!=null) {
				FieldMeta fm = (FieldMeta)annotation;
				sField.setMeta(fm);
				sField.setName(filedName);
				sField.setField(null);
				list.add(sField);
	    	}
		}
		return list;
	}
	
}
