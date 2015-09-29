package support.utils.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class Dom4J_XML {
	private static boolean printMsg = true;//设定是否打印消息
	
	public static Document parse(String filePath){
		if (filePath.equals("")){
			out("文件路径不能为空！",true);
		}else{
			File file = new File(filePath);
			if(!file.exists()){
				out(filePath+"文件不存在！",true);
			}
			SAXReader reader=new SAXReader();
			try {
				return reader.read(file);
			} catch (DocumentException e) {
				out("read document failed in "+filePath ,true);
				e.printStackTrace();
			}
		}
		return null;		
	}

	//将字符串转化为XML
	public static Document parseText(String text){
		try {
			return DocumentHelper.parseText(text);
		} catch (DocumentException e) {
			out("read document failed in text string",true);
			e.printStackTrace();
		}
		return null;
	}
	
	//将文档或节点的XML转化为字符串
	public static String parseRootText(String filePath){		
		if (filePath.equals("")){
			out("文件路径不能为空！",true);
		}else{
			File file = new File(filePath);
			if(!file.exists()){
				out(filePath+"文件不存在！",true);
			}
			SAXReader reader=new SAXReader();
			try {
				Document document = reader.read(new File(filePath));
				Element root=document.getRootElement();
				return root.asXML();
			} catch (DocumentException e) {
				out("read document failed in "+filePath ,true);
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 把document对象写入新的文件
	 * @param document
	 * @param filePath
	 * @throws Exception
	 */
	public static void writer(Document document,String filePath){
		// 紧凑的格式
		// OutputFormat format = OutputFormat.createCompactFormat();
		// 排版缩进的格式
		OutputFormat format = OutputFormat.createPrettyPrint();
		// 设置编码
		format.setEncoding("UTF-8");
		// 创建XMLWriter对象,指定了写出文件及编码格式
		// XMLWriter writer = new XMLWriter(new FileWriter(new File("src//a.xml")),format);
		XMLWriter writer = null;
		try {
			writer = new XMLWriter(new OutputStreamWriter(
					new FileOutputStream(new File(filePath)), "UTF-8"), format);
			// 写入
			writer.write(document);
			// 立即写入
			writer.flush();
		} catch (Exception e) {
			out("写document入"+filePath+"失败" ,true);
			e.printStackTrace();
		} finally{
			try {
				if(writer!=null)
					writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 遍历当前节点元素下面的所有(元素的)子节点
	 * 
	 * @param node
	 */
	public static void listNodes(Element node) {
		// 获取当前节点的所有属性节点
		List<Attribute> list = node.attributes();
		
		// 遍历属性节点
		for (Attribute attr : list) {
			System.out.println(attr.getText() + "-----" + attr.getName()
					+ "---" + attr.getValue());
		}

		if (!(node.getTextTrim().equals(""))) {
			System.out.println("文本内容：：：：" + node.getText());
		}

		// 当前节点下面子节点迭代器
		Iterator<Element> it = node.elementIterator();
		// 遍历
		while (it.hasNext()) {
			// 获取某个子节点对象
			Element e = it.next();
			// 对子节点进行遍历
			listNodes(e);
		}
	}

	/**
	 * 把document对象写入新的文件
	 * 
	 * @param document
	 * @throws Exception
	 */
	public static void writer(Document document) throws Exception {
		// 紧凑的格式
		// OutputFormat format = OutputFormat.createCompactFormat();
		// 排版缩进的格式
		OutputFormat format = OutputFormat.createPrettyPrint();
		// 设置编码
		format.setEncoding("UTF-8");
		// 创建XMLWriter对象,指定了写出文件及编码格式
		// XMLWriter writer = new XMLWriter(new FileWriter(new
		// File("src//a.xml")),format);
		XMLWriter writer = new XMLWriter(new OutputStreamWriter(
				new FileOutputStream(new File("src//a.xml")), "UTF-8"), format);
		// 写入
		writer.write(document);
		// 立即写入
		writer.flush();
		// 关闭操作
		writer.close();
	}
	
	//以下为some methods
	private static void test() throws Exception {	
		// 创建saxReader对象
		SAXReader reader = new SAXReader();
		// 通过read方法读取一个文件 转换成Document对象
		Document document = reader.read(new File("src/dom4j/sida.xml"));
		//获取根节点元素对象
		Element node = document.getRootElement();
		//遍历所有的元素节点
		listNodes(node);
		
		// 获取四大名著元素节点中，子节点名称为红楼梦元素节点。
		Element element = node.element("红楼梦");
		//获取element的id属性节点对象
		Attribute attr = element.attribute("id");
		//删除属性
		element.remove(attr);
		//添加新的属性
		element.addAttribute("name", "作者");
		// 在红楼梦元素节点中添加朝代元素的节点
		Element newElement = element.addElement("朝代");
		newElement.setText("清朝");
		//获取element中的作者元素节点对象
		Element author = element.element("作者");
		//删除元素节点
		boolean flag = element.remove(author);
		//返回true代码删除成功，否则失败
		System.out.println(flag);
		//添加CDATA区域
		element.addCDATA("红楼梦，是一部爱情小说.");
		// 写入到一个新的文件中
		writer(document);
	}
	/**
	 * 打印消息，
	 * @param msg 消息内容
	 * @param tr 换行
	 */
	private static void out(String msg){
		if(printMsg){
			out(msg,true);
		}
	}
	/**
	 * 打印消息，
	 * @param msg 消息内容
	 * @param tr 换行
	 */
	private static void out(String msg,boolean tr){
		if(printMsg){
			System.out.print(msg+(tr?"\n":""));
		}
	}
}
