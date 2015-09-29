package support.utils.properties;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import support.utils.out.Print;

/**
 * @CopyRight：http://www.netrust.cn/
 *
 * @Description: 读取修改配置文件  
 * @Author: lazite 
 * @CreateTime: 2015年9月6日 下午3:56:55   
 * @ModifyBy: lazite 
 * @ModeifyTime: 2015年9月6日 下午3:56:55   
 * @ModifyDescription:
 * @Version:   V1.0
 */
public class ReProperties {	
	//测试路径
	//private static final String PROPERTY_FILE = "src/RestoreRead.properties";
	//服务器启动路径
	private static final String PROPERTY_FILE = ReProperties.class.getClassLoader().getResource("/RestoreRead.properties").getPath();
	
	public static String readData(String key) throws IOException {
		  Properties props = new Properties();
		  InputStream in = new FileInputStream(PROPERTY_FILE);
		  props.load(in);
		  in.close();
		  String value = props.getProperty(key);
		  return value;
	 }
	
	public static Map<String,String> readDatas() throws IOException {
		Properties props = new Properties();
		InputStream in = new FileInputStream(PROPERTY_FILE);
		props.load(in);
		Map<String,String> map=new HashMap<String, String>();
		Iterator<String> it=props.stringPropertyNames().iterator();
		
		while(it.hasNext()){
			String keys=it.next();
			String value = props.getProperty(keys);
			map.put(keys, value);
		}	
		
		in.close();
		return map;
	}
	
	 @SuppressWarnings("deprecation")
	public static void writeData(String key,String value) {
		  Properties prop = new Properties();
		  try {  
			  InputStream fis = new FileInputStream(PROPERTY_FILE);
			  prop.load(fis);
			  fis.close();
//			  Iterator<String> it=prop.stringPropertyNames().iterator();
//			  while(it.hasNext()){
//				  String keys=it.next();
//				  if(keys.equals(key))
					  prop.setProperty(key, value);
//	          }
			  
			  OutputStream fos = new FileOutputStream(PROPERTY_FILE);
			  prop.store(fos,"Update '" + key + "' value");
			  
			  //prop.clear();
			  fos.flush();
			  fos.close();  
		  } catch (IOException e) {
			  e.printStackTrace();
		  }
	  }
	 
	public static void writeData_2(Map<String,String> map){
		 Properties prop = new Properties();
		  try {  
			  InputStream fis = new FileInputStream(PROPERTY_FILE);
			  //InputStream fis = loadProp.getClass().getResourceAsStream(PROPERTY_FILE);
			  prop.load(fis);
			  fis.close();

			  for (Map.Entry<String, String> entry : map.entrySet()) {
				  prop.setProperty(entry.getKey(), entry.getValue());
			  }

			  OutputStream fos = new FileOutputStream(PROPERTY_FILE);
			  prop.store(fos,"Update value");  

			  fos.flush();
			  fos.close();  
		  } catch (IOException e) {
			  e.printStackTrace();
		  }
	 }
	 
	 public static void writeData_2(String key,String value){
		//getResource方法使用了utf-8对路径信息进行了编码，当路径中存在中文和空格时，他会对这些字符进行转换，这样，
		//得到的往往不是我们想要的真实路径，在此，调用了URLDecoder的decode方法进行解码，以便得到原始的中文及空格路径。
		String filePath = ReProperties.class.getClassLoader().getResource("RestoreRead.properties").getFile();
		Properties props = null;
		BufferedWriter bw = null;
		try {
			filePath = URLDecoder.decode(filePath,"utf-8");    
			props = PropertiesLoaderUtils.loadProperties(new ClassPathResource("RestoreRead.properties"));
			// 写入属性文件
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath)));
			props.clear();// 清空旧的文件
			props.setProperty(key,value);
			props.store(bw, "");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	 }
	 
	 /**
	 * 传递键值对的Map，更新properties文件
	 * @param fileName
	 *    文件名(放在resource源包目录下)，需要后缀
	 * @param keyValueMap
	 *            键值对Map
	 */
	public static void writeData(Map<String, String> map) {
		//getResource方法使用了utf-8对路径信息进行了编码，当路径中存在中文和空格时，他会对这些字符进行转换，这样，
		//得到的往往不是我们想要的真实路径，在此，调用了URLDecoder的decode方法进行解码，以便得到原始的中文及空格路径。
		String filePath = ReProperties.class.getClassLoader().getResource("RestoreRead.properties").getFile();
		Properties props = null;
		BufferedWriter bw = null;
		try {
			filePath = URLDecoder.decode(filePath,"utf-8");    
			props = PropertiesLoaderUtils.loadProperties(new ClassPathResource("RestoreRead.properties"));
			// 写入属性文件
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath)));
			props.clear();// 清空旧的文件
			for (String key : map.keySet())
				props.setProperty(key, map.get(key));
			props.store(bw, "");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	 
	 
	 
	 public static void main(String[] args) throws Exception {
		Print.out(readData("last_updateCost_completeTime"));		
		Print.out("==================");
		writeData("last_updateCost_completeTime","**********");
		writeData("last_updateCost_completeTime","2015-08-24");
		Print.out(readData("last_updateCost_completeTime"));		
	 }
}
