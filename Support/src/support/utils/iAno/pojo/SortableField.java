package support.utils.iAno.pojo;

import java.lang.reflect.Field;

import support.utils.iAno.FieldMeta.FieldMeta;

/**
 * @CopyRight：http://www.netrust.cn/
 *
 * @Description:   获取到注解的帮助类：
 * @Author: lazite 
 * @CreateTime: 2015年9月23日 上午10:09:17   
 * @ModifyBy: lazite 
 * @ModeifyTime: 2015年9月23日 上午10:09:17   
 * @ModifyDescription:
 * @Version:   V1.0
 */
public class SortableField {

	public SortableField(){}
	
	public SortableField(FieldMeta meta, Field field) {
		super();
		this.meta = meta;
		this.field = field;
		this.name=field.getName();
		this.type=field.getType();
	}
	
	public SortableField(FieldMeta meta, String name, Class<?> type) {
		super();
		this.meta = meta;
		this.name = name;
		this.type = type;
	}

	private FieldMeta meta;
	private Field field;
	private String name;
	private Class<?> type;
	
	public FieldMeta getMeta() {
		return meta;
	}
	public void setMeta(FieldMeta meta) {
		this.meta = meta;
	}
	public Field getField() {
		return field;
	}
	public void setField(Field field) {
		this.field = field;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "SortableField [meta=" + meta + ", field=" + field + ", name="
				+ name + ", type=" + type + "]";
	}
	
	
}