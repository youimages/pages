package support.utils.iAno.test;

import support.utils.iAno.FieldMeta.FieldMeta;


/**
 * @CopyRight：http://www.netrust.cn/
 *
 * @Description:   测试实体类
 * @Author: lazite 
 * @CreateTime: 2015年9月23日 上午10:09:27   
 * @ModifyBy: lazite 
 * @ModeifyTime: 2015年9月23日 上午10:09:27   
 * @ModifyDescription:
 * @Version:   V1.0
 */
@FieldMeta(name="t_anno")
public class Anno {
	@FieldMeta(id=true,name="序列号",order=1,description="描述新倒萨倒萨大")
	private int id;
	@FieldMeta(name="姓名",order=3)
	private String name;
	@FieldMeta(name="年龄",order=2)
	private int age;
	
	
	@FieldMeta(description="描述",order=4)
	public String desc(){
		return "java反射获取annotation的测试";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
}
