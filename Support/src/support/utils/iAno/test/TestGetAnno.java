package support.utils.iAno.test;

import java.util.List;

import support.utils.iAno.GetAnno;
import support.utils.iAno.pojo.SortableField;
import support.utils.out.Print;

public class TestGetAnno {
    public static void main(String[] args) {  
//        Parent c = new Child();  
//        List<SortableField> list = c.init();//获取泛型中类里面的注解  
//        //输出结果  
//        for(SortableField l : list){  
//            System.out.println("字段名称："+l.getName()+"\t字段类型："+l.getType()+  
//                    "\t注解名称："+l.getMeta().name()+"\t注解描述："+l.getMeta().description());  
//        }
		List<SortableField> list=GetAnno.getMethodAnno(Anno.class);
		
		Print.out(list.get(0).getMeta().order());
		
		List<SortableField> list2=GetAnno.getClassAnno(Anno.class);
		Print.out(list2.get(0).getMeta().name());
    }
}
