package support.utils.excel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

public class TestExcelUtil {
	@Test
	public void testRead() {
		try {			
			OldExcelUtil eu = new OldExcelUtil();
			eu.setExcelPath("src/com/hadoop/utils/excel/excel1.xlsx");
			
			System.out.println("=======测试Excel 默认 读取========");
			eu.readExcel();
			
			System.out.println("\n=======测试Excel 从第四行读取，倒数第二行结束========");
			eu = eu.RestoreSettings();//还原设定
			eu.setStartReadPos(3);
			eu.setEndReadPos(-1);
			eu.readExcel();
			
			System.out.println("\n=======测试Excel 读取第二个sheet========");
			eu = eu.RestoreSettings();//还原设定
			eu.setSelectedSheetIdx(1);
			eu.readExcel();
			
			System.out.println("\n=======测试Excel 读取所有的sheet========");
			eu = eu.RestoreSettings();//还原设定
			eu.setOnlyReadOneSheet(false);
			eu.readExcel();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @Title: testWriteExcel
	 * @Description: 写excel
	 * @throws: TODO
	 */
	@Test
	public void testWriteExcel() {
		  String excelPath = "src/com/hadoop/utils/excel/excel2.xls";

		  Workbook workbook = null;
		  try {
		    // XSSFWorkbook used for .xslx (>= 2007), HSSWorkbook for 03 .xsl
		    workbook = new HSSFWorkbook();// XSSFWorkbook();//WorkbookFactory.create(inputStream);
		  } catch (Exception e) {
		    System.out.println("创建Excel失败: ");
		    e.printStackTrace();
		  }
		  if (workbook != null) {
		    Sheet sheet = workbook.createSheet("测试数据");
		    Row row0 = sheet.createRow(0);
		    for (int i = 0; i < 6; i++) {
		      Cell cell = row0.createCell(i, Cell.CELL_TYPE_STRING);
		      cell.setCellValue("列标题" + i );
		      //sheet.autoSizeColumn(i);//自动调整宽度
		    }
		    for (int rowNum = 1; rowNum < 10; rowNum++) {
		      Row row = sheet.createRow(rowNum);
		      for (int i = 0; i < 6; i++) {
		        Cell cell = row.createCell(i, Cell.CELL_TYPE_STRING);
		        cell.setCellValue("单元格" + String.valueOf(rowNum + 1)
		            + String.valueOf(i + 1));
		      }
		    }
		    try {
		      FileOutputStream outputStream = new FileOutputStream(excelPath);
		      workbook.write(outputStream);
		      outputStream.flush();
		      outputStream.close();
		    } catch (Exception e) {
		      System.out .println("写入Excel失败: ");
		      e.printStackTrace();
		    }
		  }
	}
	
	/**
	 * @Title: testMerge
	 * @Description: 合并2个单元格
	 * @throws: TODO
	 */
	@Test
	public void testMerge(){
		try {
			OldExcelUtil eu1 = new OldExcelUtil();//用来读取源xls
			OldExcelUtil eu2 = new OldExcelUtil();//用来读取目标xls，用于演示合并结果
			eu1.setExcelPath("d:\\2.xls");
			eu2.setExcelPath("d:\\1.xls");
			
			System.out.println("\n=======修改前，1.xls中的内容========");
			eu2.readExcel();
			
			System.out.println("\n=======读取源文件2.xls中的内容========");
			eu1.setStartReadPos(3);
			//eu1.setOverWrite(false);//是否覆写目标文件（默认覆写）
			//eu1.setComparePos(1);//设定比较哪一列内容（默认为0，比较第一列内容）
			//eu1.setNeedCompare(false);//设定是否比较（默认值是true）。只有当不覆盖目标文件时，设置检查重复才有效。
			
			eu1.writeExcel(eu1.readExcel(), "d:\\1.xls");//将读取到的2.xls中的数据合并到1.xls中
			System.out.println("\n=======修改后，1.xls中的内容========");
			eu2.readExcel();//读取合并后的1.xls的数据
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @Title: testGetRowColumn
	 * @Description: 获取某一行/某一列的数据
	 * @throws IOException
	 * @throws: TODO
	 */
	@Test
	public void testGetRowColumn() throws IOException{
		ExcelUtil eu = new ExcelUtil();//用来读取源xls
		//eu.readExcel("src/com/hadoop/utils/excel/excel1.xlsx");
		List<String> s=eu.getColumnData("src/com/hadoop/utils/excel/excel1.xlsx",4);
		System.out.println(s.toString());
		
		String[] ss=eu.getRowData("src/com/hadoop/utils/excel/excel1.xlsx",4);
		System.out.println(Arrays.asList(ss).toString());
	}
	
	/**
	 * @Title: testGetExcelClass
	 * @Description: 封装MAP
	 * @throws IOException
	 * @throws: TODO
	 */
	@Test
	public void testGetMapClass() throws IOException{
		ExcelUtil eu = new ExcelUtil();//用来读取源xls
		eu.setObjClass(Map.class);
		eu.setPrintMsg(false);
		List<Map> list=eu.readExcel("src/com/hadoop/utils/excel/excel2.xls");
		
		for (Map map : list) {
			System.out.println(map.keySet().toString());
			System.out.println(map.values().toString());
		}
	}
	
	/**
	 * @Title: testGetExcelClass
	 * @Description: 封装对象
	 * @throws IOException
	 * @throws: TODO
	 */
	@Test
	public void testGetExcelClass() throws IOException{
		ExcelUtil eu = new ExcelUtil();//用来读取源xls
		String[] decilfields={"name","cid","sid","sex","birthday","prvo","city"};
		eu.setPrintMsg(false);
		
		eu.setObjClass(PExcel1.class);
		eu.setDecilfields(decilfields);
		eu.setStartReadPos(3);
		
		List<PExcel1> list=eu.readExcel("src/com/hadoop/utils/excel/excel1.xlsx");
		
		
		for (PExcel1 pe : list) {
			System.out.println(pe.toString());
		}
	}
	
}
