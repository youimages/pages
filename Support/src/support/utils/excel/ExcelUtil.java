package support.utils.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Excel文件操作工具类，包括读、写、合并等功能
 * 
 * @author  : 龙轩
 * @group   : tgb8
 * @Version : 1.00
 * @Date    : 2014-10-29 上午12:40:44
 * @modifior: lazite
 * @modifior_decription:简化原代码,追加泛型操作,追加行\列获取,追加workbook操作,追加日期判定
 */
/**
 * @description:you need prepare these jars file for use this ExcelUtil.class
 * dom4j.jar 
 * log4j-1.2.13.jar
 * poi-3.10.1-20140818.jar
 * poi-ooxml-3.10.1-20140818.jar
 * poi-ooxml-schemas-3.10.1-20140818.jar
 * 
 * @description:when you operate 2007up  you always need 
 * poi.jar version gt 3.7
 * xmlbeans.jar
 * poi-scratchpad-3.10.1-20140818.jar
 * 
 * @apiUrl: http://www.yiibai.com/apache_poi/apache_poi_cells.html
 * @demoUrl:http://blog.csdn.net/xiaoxian8023/article/details/40597835
 */
public class ExcelUtil {
	private String excelPath = "";//Excel文件路径
	
	private int startReadPos = 0;// 设定开始读取的位置，默认为0

	private int endReadPos = 0;//设定结束读取的位置，默认为0，用负数来表示倒数第n行
	
	private int comparePos = 0;//设定开始比较的列位置，默认为0

	private boolean isOverWrite = true;//设定汇总的文件是否需要替换，默认为true
	
	private boolean isNeedCompare = true;//设定是否需要比较，默认为true
										 //(仅当不覆写目标内容是有效，即isOverWrite=false时有效)
	
	private boolean onlyReadOneSheet = true;// 设定是否只操作第一个sheet,第一个为true
	
	private int selectedSheetIdx =0;//设定操作的sheet在索引值,
									//默认读取第一个sheet中（只有当onlyReadOneSheet = true时有效）
	
	private String selectedSheetName = "";//设定操作的sheet的名称
	
	private int startSheetIdx = 0;//设定开始读取的sheet，默认为0

	private int endSheetIdx = 0;//设定结束读取的sheet，默认为0，用负数来表示倒数第n行
	
	private boolean printMsg = true;//设定是否打印消息
	
	private String extType="xls";//xls,xlxs,默认为
	
	@SuppressWarnings("rawtypes")
	private Class objClass=null; //封装的类对象
	
	private String[] decilfields=null; //封装实体类对象时的属性参数,需与excel一行的cell对象
	
	//%%%%%%%%-------字段部分结束----------%%%%%%%%%
	
	//构造方法
	public ExcelUtil(){
		super();
	}
	//带入excelPath参数的构造方法
	public ExcelUtil(String excelPath){
		this.excelPath = excelPath;
	}
	
	/**
	 * 还原设定（其实是重新new一个新的对象并返回）
	 * @return
	 */
	public ExcelUtil RestoreSettings(){
		ExcelUtil instance = new  ExcelUtil(this.excelPath);
		return instance;
	}
	
	/**
	 * 自动根据文件扩展名，调用对应的读取方法
	 * 
	 * @Title: readExcel
	 * @Date : 2014-9-11 下午01:50:38
	 * @param xlsPath
	 * @throws IOException
	 */
	public List<Row> readExcel() throws IOException{
		return readExcel(this.excelPath);
	}
	
	/**
	 * 自动根据文件扩展名，调用对应的读取方法
	 * 
	 * @Title: writeExcel
	 * @Date : 2014-9-11 下午01:50:38
	 * @param xlsPath
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> readExcel(String xlsPath) throws IOException{			
		File file=checkFileIsExist(xlsPath);		
		//获取扩展名
		String ext = xlsPath.substring(xlsPath.lastIndexOf(".")+1);
		if(!"xls".equals(ext)&&!"xlsx".equals(ext)){
			out("读取结果失败!\n请您确保您的文件是Excel文件，并且无损，然后再试",true);
			return null;
		}else{
			this.extType=ext;
			List<T> rowList = new ArrayList<T>();
			FileInputStream fis =null;
			try {
				fis = new FileInputStream(file);
				Workbook wb=null;
				if("xls".equals(extType)){
					wb = new HSSFWorkbook(fis);//读取Excel 2003版，xlsx格式
				}else if("xlsx".equals(extType)){
					wb = new XSSFWorkbook(fis);//读取Excel 2007版，xlsx格式					
				}
				
				if(objClass==null||objClass.getSimpleName().equals("Row"))
					rowList=(List<T>) readExcel(wb);
				else if(objClass.getSimpleName().equals("Map"))	
					rowList=(List<T>) readExcel_Map(wb);
				else
					rowList=(List<T>) readExcel_Class(wb);
				
			} catch (IOException e) {
				out("读取结果失败!\n请您确保文件无损",true);
				e.printStackTrace();
			}finally{
				if(fis!=null)
					fis.close();
			}
			return rowList;	
		}	
	}
	
	/**
	 * 读取Excel 2007版，xlsx格式
	 * @Title: readExcel_xlsx
	 * @Date : 2014-9-11 上午11:43:11
	 * @return
	 * @throws IOException
	 */
	public List<Row> readExcel_xlsx() throws IOException {
		return readExcel_xlsx(excelPath);
	}

	/**
	 * //读取Excel 2007版，xlsx格式
	 * 
	 * @Title: readExcel_xlsx
	 * @Date : 2014-9-11 上午11:43:11
	 * @return
	 * @throws Exception
	 */
	public List<Row> readExcel_xlsx(String xlsPath) throws IOException {
		File file = new File(xlsPath);// 判断文件是否存在
		if (!file.exists()) {
			throw new IOException("文件名为" + file.getName() + "Excel文件不存在！");
		}
		XSSFWorkbook wb = null;
		List<Row> rowList = new ArrayList<Row>();
		FileInputStream fis=null;
		
		try {
			fis = new FileInputStream(file);	
			wb = new XSSFWorkbook(fis);// 去读Excel
			// 读取Excel 2007版，xlsx格式
			rowList = readExcel(wb);

		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(fis!=null)
				fis.close();
		}
		return rowList;
	}

	/***
	 * 读取Excel(97-03版，xls格式)
	 * 
	 * @throws IOException
	 * 
	 * @Title: readExcel
	 * @Date : 2014-9-11 上午09:53:21
	 */
	public List<Row> readExcel_xls() throws IOException {
		return readExcel_xls(excelPath);
	}

	/***
	 * 读取Excel(97-03版，xls格式)
	 * 
	 * @throws Exception
	 * 
	 * @Title: readExcel
	 * @Date : 2014-9-11 上午09:53:21
	 */
	public List<Row> readExcel_xls(String xlsPath) throws IOException {
		// 判断文件是否存在
		File file = new File(xlsPath);
		if (!file.exists()) {
			throw new IOException("文件名为" + file.getName() + "Excel文件不存在！");
		}
		HSSFWorkbook wb = null;// 用于Workbook级的操作，创建、删除Excel
		List<Row> rowList = new ArrayList<Row>();
		FileInputStream fis=null;
		
		try {
			fis=new FileInputStream(file);
			wb = new HSSFWorkbook(fis);// 读取Excel	
			rowList = readExcel(wb);// 读取Excel 97-03版，xls格式
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(fis!=null)
				fis.close();
		}
		
		return rowList;
	}
	
	
	/**
	 * 通用读取Excel
	 * 
	 * @Title: readExcel
	 * @Date : 2014-9-11 上午11:26:53
	 * @param wb
	 * @return
	 */
	private List<Row> readExcel(Workbook wb) {
		List<Row> rowList = new ArrayList<Row>();
		
		int sheetCount = 1;//需要操作的sheet数量
		
		Sheet sheet = null;
		if(onlyReadOneSheet){	//只操作一个sheet
			// 获取设定操作的sheet(如果设定了名称，按名称查，否则按索引值查)
			sheet =selectedSheetName.equals("")? wb.getSheetAt(selectedSheetIdx):wb.getSheet(selectedSheetName);
		}else{							//操作多个sheet
			sheetCount = wb.getNumberOfSheets();//获取可以操作的总数量
		}		
		// 获取sheet数目
		for(int t=startSheetIdx; t<sheetCount+endSheetIdx;t++){
			// 获取设定操作的sheet
			if(!onlyReadOneSheet) {
				sheet =wb.getSheetAt(t);
			}			
			//获取最后行号
			int lastRowNum = sheet.getLastRowNum();
			
			if(lastRowNum>0){	//如果>0，表示有数据
				out("\n开始读取名为【"+sheet.getSheetName()+"】的内容：");
			}			
			Row row = null;
			// 循环读取
			for (int i = startReadPos; i <= lastRowNum + endReadPos; i++) {
				row = sheet.getRow(i);
				if (row != null) {
					rowList.add(row);
					out("第"+(i+1)+"行：",false);
					 // 获取每一单元格的值
					 for (int j = 0; j < row.getLastCellNum(); j++) {
						 String value = getCellValue(row.getCell(j));
						 if (!value.equals("")) {
							 out(value + " | ",false);
						 }
					 }
					 out("");
				}
			}
		}
		return rowList;
	}
	
	/**
	 * 通用读取Excel
	 * 
	 * @Title: readExcel
	 * @Date : 2014-9-11 上午11:26:53
	 * @param wb
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<Map> readExcel_Map(Workbook wb) {
		List<Map> mapList = new ArrayList<Map>();
		
		int sheetCount = 1;//需要操作的sheet数量
		
		Sheet sheet = null;
		if(onlyReadOneSheet){	//只操作一个sheet
			// 获取设定操作的sheet(如果设定了名称，按名称查，否则按索引值查)
			sheet =selectedSheetName.equals("")? wb.getSheetAt(selectedSheetIdx):wb.getSheet(selectedSheetName);
		}else{							//操作多个sheet
			sheetCount = wb.getNumberOfSheets();//获取可以操作的总数量
		}
		
		// 获取sheet数目
		for(int t=startSheetIdx; t<sheetCount+endSheetIdx;t++){
			// 获取设定操作的sheet
			if(!onlyReadOneSheet) {
				sheet =wb.getSheetAt(t);
			}
			
			//获取最后行号
			int lastRowNum = sheet.getLastRowNum();

			if(lastRowNum>0){	//如果>0，表示有数据
				out("\n开始读取名为【"+sheet.getSheetName()+"】的内容：");
			}
			
			Row row = null;
			// 循环读取
			for (int i = startReadPos; i <= lastRowNum + endReadPos; i++) {
				Map map=new HashMap();

				row = sheet.getRow(i);

				if (row != null) {
					
					out("第"+(i+1)+"行：",false);
					
					 // 获取每一单元格的值
					 for (int j = 0; j < row.getLastCellNum(); j++) {
						 String value = getCellValue(row.getCell(j));
						 //以行列号组合作为key值
						 map.put(i+"_"+j, value);
						 
						 if (!value.equals("")) {
							 out(value + " | ",false);
						 }
						 
					 }
					 
					 mapList.add(map);
					 out("");
				}
			}
		}
		return mapList;
	}
	
	/**
	 * 通用读取Excel
	 * 
	 * @Title: readExcel
	 * @Date : 2014-9-11 上午11:26:53
	 * @param wb
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T> List<T> readExcel_Class(Workbook wb) {
		List<T> tList = new ArrayList<T>();
		
		int sheetCount = 1;//需要操作的sheet数量
		Sheet sheet = null;
		if(onlyReadOneSheet){	//只操作一个sheet
			// 获取设定操作的sheet(如果设定了名称，按名称查，否则按索引值查)
			sheet =selectedSheetName.equals("")? wb.getSheetAt(selectedSheetIdx):wb.getSheet(selectedSheetName);
		}else{//操作多个sheet
			sheetCount = wb.getNumberOfSheets();//获取可以操作的总数量
		}
		
		// 获取sheet数目
		for(int t=startSheetIdx; t<sheetCount+endSheetIdx;t++){
			if(!onlyReadOneSheet) {
				sheet =wb.getSheetAt(t);// 获取设定操作的sheet
			}				
			int lastRowNum = sheet.getLastRowNum();//获取最后行号
			if(lastRowNum>0){	//如果>0，表示有数据
				out("\n开始读取名为【"+sheet.getSheetName()+"】的内容：");
			}			
			Row row = null;			
			for (int i = startReadPos; i <= lastRowNum + endReadPos; i++) {// 循环读取
				T tob=null;
				try {
					tob = (T) objClass.newInstance();
				} catch (Exception e) {
					out("创建"+objClass+"实例对象失败",true);
					e.printStackTrace();
				}
				
				row = sheet.getRow(i);
				if (row != null) {					
					out("第"+(i+1)+"行：",false);
					 // 获取每一单元格的值
					 for (int j = 0; j < row.getLastCellNum(); j++) {
						 String value = getCellValue(row.getCell(j));						 
						 if(decilfields!=null&&decilfields.length!=0){
							 for (int k=0;k<decilfields.length;k++) {
								 try {
									if(j==k){
										Field field = tob.getClass().getDeclaredField(decilfields[k]);		
										field.setAccessible(true);
										field.set(tob, value);
									}
								 } catch (Exception ex) {
									 out("反射对象异常,请检测属性名称",true);
									 throw new RuntimeException();
								 }
							 }
						 }else{
							 out("set对象属性为空,已按照普通对象属性予以赋值",true);
							 Field[] fields = tob.getClass().getDeclaredFields();
							 
							 for (int k=0;k<fields.length;k++) {
								 try {
									if(j==k){
										fields[k].setAccessible(true);
										fields[k].set(tob, value);
									}
								 } catch (Exception ex) {
									 out("反射对象异常,请检测属性名称",true);
									 throw new RuntimeException();
								 }
							 }
							 
						 }						 
						 //以行列号组合作为key值
						 if (!value.equals("")) {
							 out(value + " | ",false);
						 }
					 }
					 tList.add(tob);
					 out("");
				}
			}
		}
		return tList;
	}
	
	/**
	 * @Title: getWorkBook
	 * @Description: 获取工作薄对象
	 * @param xlsPath 路径
	 * @return
	 * @throws IOException
	 * @throws: TODO
	 */
	public Workbook getWorkBook(String xlsPath) throws IOException{
		File file=checkFileIsExist(xlsPath);		
		//获取扩展名
		String ext = xlsPath.substring(xlsPath.lastIndexOf(".")+1);
		if(!"xls".equals(ext)&&!"xlsx".equals(ext)){
			out("读取结果失败!\n请您确保您的文件是Excel文件，并且无损，然后再试",true);
			return null;
		}else{
			Workbook wb=null;
			this.extType=ext;
			
			FileInputStream fis=null;
			try {
				fis = new FileInputStream(file);
				if("xls".equals(extType)){
					wb = new HSSFWorkbook(fis);//读取Excel 2003版，xlsx格式
				}else if("xlsx".equals(extType)){
					wb = new XSSFWorkbook(fis);//读取Excel 2007版，xlsx格式					
				}
			}catch(Exception e){
				out("读取结果失败!\n请您确保您的文件是Excel文件，并且无损，然后再试",true);
			}finally{
				if(fis!=null)
					fis.close();
			}
			return wb;
		}
	}
	
	/**
	 * @Title: getRow
	 * @Description: 获取Row
	 * @param wb
	 * @param rowNum
	 * @return
	 * @throws: TODO
	 */
	public Row getRow(Workbook wb,int rowNum){
		Sheet sheet = null;	
		if(onlyReadOneSheet){	//只操作一个sheet
			// 获取设定操作的sheet(如果设定了名称，按名称查，否则按索引值查)
			sheet =selectedSheetName.equals("")? wb.getSheetAt(selectedSheetIdx):wb.getSheet(selectedSheetName);
			if(rowNum<sheet.getLastRowNum()+1)
				return sheet.getRow(rowNum);
			else
				out(rowNum+"行超过excel数据行范围",true);
		}
		return null;
	}
	
	/**
	 * @Title: getRowData
	 * @Description:  获取某一行数据
	 * @param xlsPath 文件路径
	 * @param rowNum 行号
	 * @return
	 * @throws IOException 
	 * @throws: TODO
	 */
	public String[] getRowData(String xlsPath,int rowNum) throws IOException{
		return getRowData(getRow(getWorkBook(xlsPath),rowNum));
	} 
	
	/**
	 * @Title: getRowData
	 * @Description:  获取某一行数据
	 * @param Workbook
	 * @param rowNum 行号
	 * @return
	 * @throws: TODO
	 */
	public String[] getRowData(Workbook wb,int rowNum){
		Sheet sheet = null;	
		if(onlyReadOneSheet){	//只操作一个sheet
			// 获取设定操作的sheet(如果设定了名称，按名称查，否则按索引值查)
			sheet =selectedSheetName.equals("")? wb.getSheetAt(selectedSheetIdx):wb.getSheet(selectedSheetName);
			if(rowNum<sheet.getLastRowNum()+1)
				return getRowData(sheet.getRow(rowNum));
			else
				out(rowNum+"行超过excel数据行范围",true);
		}
		return null;
	} 
	
	/**
	 * @Title: getRowData
	 * @Description:  获取某一行数据
	 * @param row 行
	 * @return
	 * @throws: TODO
	 */
	public String[] getRowData(Row row){
		String[] dataArray = null;  
		dataArray = new String[row.getLastCellNum()];
		Cell cell=null;
		for (int i = 0; i < row.getLastCellNum(); i++) {
			cell=row.getCell(i);
			dataArray[i]=getCellValue(cell);			
		}    
		return dataArray;
	} 
	
	/**
	 * @Title: getRowData
	 * @Description:  获取某一列数据
	 * @param xlsPath 文件路径
	 * @param columnNum 列号(从0开始)
	 * @return
	 * @throws IOException 
	 * @throws: TODO
	 */
	public List<String> getColumnData(String xlsPath,int columnNum) throws IOException{
		return getColumnData(getWorkBook(xlsPath),columnNum);
	} 
	
	/**
	 *   	
	 * @Title: getColumnData
	 * @Description: 获取某一列数据
	 * @param wb
	 * @param columnNum 列号(从0开始)
	 * @return
	 * @throws: TODO
	 */
	public List<String> getColumnData(Workbook wb,int columnNum){ 
		List<String> dataArray = new ArrayList<String>();
		Sheet sheet = null;
		
		if(onlyReadOneSheet){	//只操作一个sheet
			// 获取设定操作的sheet(如果设定了名称，按名称查，否则按索引值查)
			sheet =selectedSheetName.equals("")? wb.getSheetAt(selectedSheetIdx):wb.getSheet(selectedSheetName);
						
			//以最后一个数据行作为列数的获取行
			if(columnNum<sheet.getRow(sheet.getLastRowNum()).getLastCellNum()+1){
				for (int i = 0; i < sheet.getLastRowNum(); i++) {
					if(sheet.getRow(i)!=null){
						dataArray.add(getCellValue(sheet.getRow(i).getCell(columnNum)));
					}else{
						out(i+"行为空数据行,请按标准格式录入",true);
					}		
				}
			}else{
				out(columnNum+"列超过excel数据列范围",true);
				return null;
			}
		}else{
			out("暂不支持读取多个sheet列",true);
		}
		return dataArray;     
	}  
	
	
	
	/**
	 * 复制一个单元格样式到目的单元格样式
	 * 
	 * @param fromStyle
	 * @param toStyle
	 */
	public static void copyCellStyle(CellStyle fromStyle, CellStyle toStyle) {
		toStyle.setAlignment(fromStyle.getAlignment());
		// 边框和边框颜色
		toStyle.setBorderBottom(fromStyle.getBorderBottom());
		toStyle.setBorderLeft(fromStyle.getBorderLeft());
		toStyle.setBorderRight(fromStyle.getBorderRight());
		toStyle.setBorderTop(fromStyle.getBorderTop());
		toStyle.setTopBorderColor(fromStyle.getTopBorderColor());
		toStyle.setBottomBorderColor(fromStyle.getBottomBorderColor());
		toStyle.setRightBorderColor(fromStyle.getRightBorderColor());
		toStyle.setLeftBorderColor(fromStyle.getLeftBorderColor());

		// 背景和前景
		toStyle.setFillBackgroundColor(fromStyle.getFillBackgroundColor());
		toStyle.setFillForegroundColor(fromStyle.getFillForegroundColor());

		// 数据格式
		toStyle.setDataFormat(fromStyle.getDataFormat());
		toStyle.setFillPattern(fromStyle.getFillPattern());
		// toStyle.setFont(fromStyle.getFont(null));
		toStyle.setHidden(fromStyle.getHidden());
		toStyle.setIndention(fromStyle.getIndention());// 首行缩进
		toStyle.setLocked(fromStyle.getLocked());
		toStyle.setRotation(fromStyle.getRotation());// 旋转
		toStyle.setVerticalAlignment(fromStyle.getVerticalAlignment());
		toStyle.setWrapText(fromStyle.getWrapText());

	}

	/**
	 * 获取合并单元格的值
	 * 
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 */
	public void setMergedRegion(Sheet sheet) {
		int sheetMergeCount = sheet.getNumMergedRegions();

		for (int i = 0; i < sheetMergeCount; i++) {
			// 获取合并单元格位置
			CellRangeAddress ca = sheet.getMergedRegion(i);
			int firstRow = ca.getFirstRow();
			if (startReadPos - 1 > firstRow) {// 如果第一个合并单元格格式在正式数据的上面，则跳过。
				continue;
			}
			int lastRow = ca.getLastRow();
			int mergeRows = lastRow - firstRow;// 合并的行数
			int firstColumn = ca.getFirstColumn();
			int lastColumn = ca.getLastColumn();
			// 根据合并的单元格位置和大小，调整所有的数据行格式，
			for (int j = lastRow + 1; j <= sheet.getLastRowNum(); j++) {
				// 设定合并单元格
				sheet.addMergedRegion(new CellRangeAddress(j, j + mergeRows, firstColumn, lastColumn));
				j = j + mergeRows;// 跳过已合并的行
			}

		}
	}
	
	/***
	 * 读取单元格的值
	 * 
	 * @Title: getCellValue
	 * @Date : 2014-9-11 上午10:52:07
	 * @param cell
	 * @return
	 */
	private String getCellValue(Cell cell) {
		Object result = "";
		if (cell != null) {
			switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:result = cell.getStringCellValue();break;
				case Cell.CELL_TYPE_NUMERIC:result = parseNUMERIC(cell);break;
				case Cell.CELL_TYPE_BOOLEAN:result = cell.getBooleanCellValue();break;
				case Cell.CELL_TYPE_FORMULA:result = cell.getCellFormula();	break;
				case Cell.CELL_TYPE_ERROR:result = cell.getErrorCellValue();break;
				case Cell.CELL_TYPE_BLANK:break;
				default:break;
			}
			
		}
		return result.toString();
	}
	
	
	/**
	 * 通用万能所有日期格式都可以通过getDataFormat()值来判断
	 * yyyy-MM-dd-----	14
	 * yyyy年m月d日---	31
	 * yyyy年m月-------	57
	 * m月d日  ----------	58
	 * HH:mm-----------	20
	 * h时mm分  -------	32
	 */
//  1、判断是否是数值格式
//	if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
//		short format = cell.getCellStyle().getDataFormat();
//		SimpleDateFormat sdf = null;
//		if(format == 14 || format == 31 || format == 57 || format == 58){
//			//日期
//			sdf = new SimpleDateFormat("yyyy-MM-dd");
//		}else if (format == 20 || format == 32) {
//			//时间
//			sdf = new SimpleDateFormat("HH:mm");
//		}
//		double value = cell.getNumericCellValue();
//		Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
//		result = sdf.format(date);
//	}
	
	/**
	 * @Title: parseNUMERIC
	 * @Description: 数据与日期格式的转化
	 * @param cell
	 * @return
	 * @throws: TODO
	 */
	SimpleDateFormat sdf = null;
	private String parseNUMERIC(Cell cell){
		String result=null;
		if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
			if (cell.getCellStyle().getDataFormat() == HSSFDataFormat
					.getBuiltinFormat("h:mm")) {
				sdf = new SimpleDateFormat("HH:mm");
			} else {// 日期
				sdf = new SimpleDateFormat("yyyy-MM-dd");
			}
			Date date = cell.getDateCellValue();
			result = sdf.format(date);
		} else if (cell.getCellStyle().getDataFormat() == 14) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			double value = cell.getNumericCellValue();
			Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
			result = sdf.format(date);
		}else if (cell.getCellStyle().getDataFormat() == 58) {
			// 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			double value = cell.getNumericCellValue();
			Date date = org.apache.poi.ss.usermodel.DateUtil
					.getJavaDate(value);
			result = sdf.format(date);
		}else {
			double value = cell.getNumericCellValue();
			CellStyle style = cell.getCellStyle();
			DecimalFormat format = new DecimalFormat();
			String temp = style.getDataFormatString();
			// 单元格设置成常规
			if (temp.equals("General")) {
				format.applyPattern("#");
			}
			result = format.format(value);
		}
		return result;
	}

	
	/**
	 * @Title: checkFileIsExist
	 * @Description: 
	 * @param filePath
	 * @return
	 * @throws IOException
	 * @throws: TODO
	 */
	private File checkFileIsExist(String filePath) throws IOException {
		if (filePath.equals("")){
			throw new IOException("文件路径不能为空！");
		}else{
			File file = new File(filePath);
			if(!file.exists()){
				throw new IOException(filePath+"文件不存在！");
			}
			return file;
		}
	}

	/**
	 * 打印消息，
	 * @param msg 消息内容
	 * @param tr 换行
	 */
	private void out(String msg){
		if(printMsg){
			out(msg,true);
		}
	}
	/**
	 * 打印消息，
	 * @param msg 消息内容
	 * @param tr 换行
	 */
	private void out(String msg,boolean tr){
		if(printMsg){
			System.out.print(msg+(tr?"\n":""));
		}
	}
	
	
	///////////////////////////////////////////////////
	//以下为get/set
	///////////////////////////////////////////////////
	
	public String getExcelPath() {
		return excelPath;
	}
	public void setExcelPath(String excelPath) {
		this.excelPath = excelPath;
	}
	public int getStartReadPos() {
		return startReadPos;
	}
	public void setStartReadPos(int startReadPos) {
		this.startReadPos = startReadPos;
	}
	public int getEndReadPos() {
		return endReadPos;
	}
	public void setEndReadPos(int endReadPos) {
		this.endReadPos = endReadPos;
	}
	public int getComparePos() {
		return comparePos;
	}
	public void setComparePos(int comparePos) {
		this.comparePos = comparePos;
	}
	public boolean isOverWrite() {
		return isOverWrite;
	}
	public void setOverWrite(boolean isOverWrite) {
		this.isOverWrite = isOverWrite;
	}
	public boolean isNeedCompare() {
		return isNeedCompare;
	}
	public void setNeedCompare(boolean isNeedCompare) {
		this.isNeedCompare = isNeedCompare;
	}
	public boolean isOnlyReadOneSheet() {
		return onlyReadOneSheet;
	}
	public void setOnlyReadOneSheet(boolean onlyReadOneSheet) {
		this.onlyReadOneSheet = onlyReadOneSheet;
	}
	public int getSelectedSheetIdx() {
		return selectedSheetIdx;
	}
	public void setSelectedSheetIdx(int selectedSheetIdx) {
		this.selectedSheetIdx = selectedSheetIdx;
	}
	public String getSelectedSheetName() {
		return selectedSheetName;
	}
	public void setSelectedSheetName(String selectedSheetName) {
		this.selectedSheetName = selectedSheetName;
	}
	public int getStartSheetIdx() {
		return startSheetIdx;
	}
	public void setStartSheetIdx(int startSheetIdx) {
		this.startSheetIdx = startSheetIdx;
	}
	public int getEndSheetIdx() {
		return endSheetIdx;
	}
	public void setEndSheetIdx(int endSheetIdx) {
		this.endSheetIdx = endSheetIdx;
	}
	public boolean isPrintMsg() {
		return printMsg;
	}
	public void setPrintMsg(boolean printMsg) {
		this.printMsg = printMsg;
	}
	public String getExtType() {
		return extType;
	}
	public void setExtType(String extType) {
		this.extType = extType;
	}
	public Class getObjClass() {
		return objClass;
	}
	public void setObjClass(Class objClass) {
		this.objClass = objClass;
	}
	public String[] getDecilfields() {
		return decilfields;
	}
	public void setDecilfields(String[] decilfields) {
		this.decilfields = decilfields;
	}


	
}
