package support.utils.excel;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.ss.util.CellRangeAddress;

public class OldOperateExcel{
	/**
	 * 加载文件
	 * @param fileDirectory
	 * @return
	 * @throws 文件找不到异常
	 */
	public Sheet getExcelFile(String fileDirectory,int sheetIndex) throws Exception{
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(fileDirectory));
			Sheet sheet=workbook.getSheetAt(sheetIndex);
			return sheet;
	}
	
	/** 
	 * 返回Excel最大行index值，实际行数要加1 
	 * @return 
	 */  
	private int getRowNum(Sheet sheet){  
		return sheet.getLastRowNum()+1;  
	}
	
	/**
	 * 获取excel具体数据实际占用行数,可扩展
	 * @param sheet
	 * @param titleSize
	 * @return
	 */
	public int getDateRowNum(Sheet sheet,int titleSize){
		return this.getRowNum(sheet)-titleSize;
	}
	
	
	/**
	 * @Title: getColumnNum
	 * @Description: 返回数据的列数 
	 * @param sheet
	 * @param rowIndex 判断列数所使用的行号
	 * @return
	 * @throws: TODO
	 */
	private int getColumnNum(Sheet sheet,int rowIndex){  
	    Row row = sheet.getRow(rowIndex);  
	    if(row!=null&&row.getLastCellNum()>0){  
	       return row.getLastCellNum();  
	    }else{
	       return 0;  	    	
	    }
	}  
	
	  
	/** 
	* 获取某一行数据 
	* @param rowIndex 计数从0开始，rowIndex为0哪一行 
	* @return 
	*/
	public String[] getRowData(Sheet sheet,int rowIndex){
		//2为判断列数所使用的行号
		int columnNums=this.getColumnNum(sheet,2);
		
		int rowNum=this.getRowNum(sheet);
		
		String[] dataArray = null;  
		if(rowIndex>rowNum){  
			return dataArray;  
		}else{ 
			Row row=sheet.getRow(rowIndex);
			dataArray = new String[columnNums];
			Cell cell=null;
			for (int i = 0; i < columnNums; i++) {
				cell=row.getCell(i);
				dataArray[i]=transCellValue(cell);
				//System.out.println(transCellValue(cell)+"==========行"+rowIndex+"列"+i);
			}
		}    
		return dataArray;
	} 
  
	/** 
	 * 获取某一列数据 
	 * @param colIndex(列序列号)   
	 * @param countIndex(统计列数采用的列号)
	 * @param titleSize(标题栏所占用的行数)
	 * @return 
	 */  	
	public List<String> getColumnData(Sheet sheet,int colIndex,int countIndex,int titleSize){ 
	  int rowNum=this.getDateRowNum(sheet, titleSize);	//行数,应该为实际的数据行数,-titleSize为去除标题行
	  int columNum=this.getColumnNum(sheet,countIndex);//列数
	  
	  List<String> dataArray = null;
	  if(colIndex<=columNum){  
		  dataArray=new ArrayList<String>();
		  for(int i=titleSize;i<rowNum+titleSize;i++){
			Cell cell=sheet.getRow(i).getCell(colIndex);

			String cellValue=this.transCellValue(cell);
			if(StringUtils.isNotBlank(cellValue)){
				dataArray.add(cellValue);
			}
		  }   	  
	  }
	  return dataArray;     
	}  
	
	/**
	 * 获取合并单元格,不包含序列号列 及值
	 * @param sheet
	 * @param MergedRow 合并单元格所处的列号
	 * @return 
	 */
	private List<CellRangeAddress> getMergedRegionCell(Sheet sheet,int MergedRow) { 
		List<CellRangeAddress> fCellList=new ArrayList<CellRangeAddress>();
		int sheetMergeCount = sheet.getNumMergedRegions();
		
		for (int i = 0; i < sheetMergeCount; i++) { 
			CellRangeAddress ca = sheet.getMergedRegion(i); 
			int firstColumn = ca.getFirstColumn(); 
			//int firstRow = ca.getFirstRow(); 
			
			if(firstColumn==MergedRow){
				//Cell fCell = sheet.getRow(firstRow).getCell(firstColumn);
				fCellList.add(ca);				
			}
		}
		return fCellList; 
	}
	
	
	/**
	* 判断单元格是否为合并单元格
	* 
	* @param listCombineCell
	*            存放合并单元格的list
	* @param cell
	*            需要判断的单元格
	* @param sheet
	*            sheet
	* @return
	*/
	public static Boolean isCombineCell(List<CellRangeAddress> fCellList,Cell cell,Sheet sheet) {
			int firstC = 0;
			int lastC = 0;
			int firstR = 0;
			int lastR = 0;
			for (CellRangeAddress ca : fCellList) {
				// 获得合并单元格的起始行, 结束行, 起始列, 结束列
				firstC = ca.getFirstColumn();
				lastC = ca.getLastColumn();
				firstR = ca.getFirstRow();
				lastR = ca.getLastRow();
					if (cell.getColumnIndex() <= lastC&& cell.getColumnIndex()>= firstC) {
						if (cell.getRowIndex() <= lastR && cell.getRowIndex() >= firstR) {
						return true;
					}
				}
			}
		return false;
	}
	
	
	/**
	 * 获取某一列合并单元格的值,不包含序列号列 
	 * @param sheet
	 * @param MergedRow 合并单元格所处的列号
	 * @return
	 */
	public List<String> getMergedRegionValue(Sheet sheet,int MergedRow) { 
		List<String> fCellValues=new ArrayList<String>();
		int sheetMergeCount = sheet.getNumMergedRegions();
		
		for (int i = 0; i < sheetMergeCount; i++) { 
			CellRangeAddress ca = sheet.getMergedRegion(i); 
			int firstColumn = ca.getFirstColumn(); 
			
			if(ca.getLastColumn()==MergedRow){
				Cell fCell = sheet.getRow(MergedRow).getCell(firstColumn);
				String fCellValue=transCellValue(fCell);
				fCellValues.add(fCellValue);				
			}
		}
		return fCellValues; 
	}
	
	/** 
	 * @Title: getMergedRegion
	 * @Description: 获取某一行所处合并单元格的值
	 * @param sheet
	 * @param mergedRow
	 * @param stayRow 
	 * @return
	 * @throws: TODO
	 */
	public String getMergedRegion(Sheet sheet,int mergedRow,int stayRow){
		int sheetMergeCount = sheet.getNumMergedRegions();
		String fCellValue=null;
		
		for (int i = 0; i < sheetMergeCount; i++) { 
			CellRangeAddress ca = sheet.getMergedRegion(i); 
			int firstRow=ca.getFirstRow();
			if(stayRow>firstRow-1&&stayRow<ca.getLastRow()+1){
				Cell fCell = sheet.getRow(firstRow).getCell(ca.getFirstColumn());
				fCellValue=transCellValue(fCell);				
			}
		}
		
		return fCellValue;
	}
	
		

	/**
	 * 判断excel单元格内容的格式，并对其进行转换，以便插入数据库
	 * @param cell
	 * @return
	 */
	private String transCellValue(Cell cell){
		String cellValue = null;
		if(cell!=null){
			 switch(cell.getCellType()){  
		         case Cell.CELL_TYPE_NUMERIC : cellValue = String.valueOf((int)cell.getNumericCellValue()); break;  
		         case Cell.CELL_TYPE_STRING : cellValue = cell.getStringCellValue(); break;  
		         //这个值不确定
		         case Cell.CELL_TYPE_FORMULA : cellValue = String.valueOf(cell.getDateCellValue()); break;  
		         case Cell.CELL_TYPE_BLANK : cellValue = ""; break;  
		         case Cell.CELL_TYPE_BOOLEAN : cellValue = String.valueOf(cell.getBooleanCellValue()); break;  
		         case Cell.CELL_TYPE_ERROR : cellValue = String.valueOf(cell.getErrorCellValue()); break;
		         default:
		         	cellValue=null;break;	
			 }
		}
		return cellValue;
	}
	

	
	

}
