package com.fhzm.common;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.hssf.util.PaneInformation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

@SuppressWarnings("all")
public class POIUtils {
	
    static SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    static short[] yyyyMMdd = {14, 31, 57, 58, 179, 184, 185, 186, 187, 188};
    
    static short[] HHmmss = {20, 32, 190, 191, 192};
    
    static List<short[]> yyyyMMddList = Arrays.asList(yyyyMMdd);
    
    static List<short[]> hhMMssList = Arrays.asList(HHmmss);
	
	HttpServletResponse response;
	// 文件名
	private String fileName;
	// 文件保存路径
	private String fileDir;
	// sheet名
	private String sheetName;
	// 表头字体
	private String titleFontType = "Arial Unicode MS";
	// 表头背景色
	private String titleBackColor = "C1FBEE";
	// 表头字号
	private short titleFontSize = 12;
	// 添加自动筛选的列 如 A:M
	private String address = "";
	// 正文字体
	private String contentFontType = "Arial Unicode MS";
	// 正文字号
	private short contentFontSize = 12;

	private HSSFWorkbook workbook = null;

	public POIUtils(String fileDir, String sheetName) {
		this.fileDir = fileDir;
		this.sheetName = sheetName;
		workbook = new HSSFWorkbook();
	}

	public POIUtils(HttpServletResponse response, String fileName, String sheetName) {
		this.response = response;
		this.sheetName = sheetName;
		this.fileName = fileName;
		workbook = new HSSFWorkbook();
	}

	/**
	 * 设置表头字体.
	 * 
	 * @param titleFontType
	 */
	public void setTitleFontType(String titleFontType) {
		this.titleFontType = titleFontType;
	}

	/**
	 * 设置表头背景色.
	 * 
	 * @param titleBackColor
	 *            十六进制
	 */
	public void setTitleBackColor(String titleBackColor) {
		this.titleBackColor = titleBackColor;
	}

	/**
	 * 设置表头字体大小.
	 * 
	 * @param titleFontSize
	 */
	public void setTitleFontSize(short titleFontSize) {
		this.titleFontSize = titleFontSize;
	}

	/**
	 * 设置表头自动筛选栏位,如A:AC.
	 * 
	 * @param address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 设置正文字体.
	 * 
	 * @param contentFontType
	 */
	public void setContentFontType(String contentFontType) {
		this.contentFontType = contentFontType;
	}

	/**
	 * 设置正文字号.
	 * 
	 * @param contentFontSize
	 */
	public void setContentFontSize(short contentFontSize) {
		this.contentFontSize = contentFontSize;
	}

	/**
	 * 写excel.
	 * 
	 * @param titleColumn
	 *            对应bean的属性名
	 * @param titleName
	 *            excel要导出的表名
	 * @param titleSize
	 *            列宽
	 * @param dataList
	 *            数据
	 */
	public void wirteExcel(String titleColumn[], String titleName[], int titleSize[], List<?> dataList) {
		// 添加Worksheet（不添加sheet时生成的xls文件打开时会报错)
		Sheet sheet = workbook.createSheet(this.sheetName);
		// 新建文件
		OutputStream out = null;
		try {
			if (fileDir != null) {
				// 有文件路径
				out = new FileOutputStream(fileDir);
			} else {
				// 否则，直接写到输出流中
				out = response.getOutputStream();
				fileName = fileName + ".xls";
				response.setContentType("application/x-msdownload");
				response.setHeader("Content-Disposition",
						"attachment; filename=" + new String(fileName.getBytes("gb2312"), "iso8859-1"));
			}

			// 写入excel的表头
			Row titleNameRow = workbook.getSheet(sheetName).createRow(0);
			// 设置样式
			HSSFCellStyle titleStyle = workbook.createCellStyle();
			titleStyle = (HSSFCellStyle) setFontAndBorder(titleStyle, titleFontType, (short) titleFontSize);
			titleStyle = (HSSFCellStyle) setColor(titleStyle, titleBackColor, (short) 10);

			for (int i = 0; i < titleName.length; i++) {
				sheet.setColumnWidth(i, titleSize[i] * 256); // 设置宽度
				Cell cell = titleNameRow.createCell(i);
				cell.setCellStyle(titleStyle);
				cell.setCellValue(titleName[i].toString());
			}

			// 为表头添加自动筛选
			if (!"".equals(address)) {
				CellRangeAddress c = (CellRangeAddress) CellRangeAddress.valueOf(address);
				sheet.setAutoFilter(c);
			}

			// 通过反射获取数据并写入到excel中
			if (dataList != null && dataList.size() > 0) {
				// 设置样式
				HSSFCellStyle dataStyle = workbook.createCellStyle();
				titleStyle = (HSSFCellStyle) setFontAndBorder(titleStyle, contentFontType, (short) contentFontSize);
				if (titleColumn.length > 0) {
					for (int rowIndex = 1; rowIndex <= dataList.size(); rowIndex++) {
						Object obj = dataList.get(rowIndex - 1); // 获得该对象
						Class clsss = obj.getClass(); // 获得该对对象的class实例
						Row dataRow = workbook.getSheet(sheetName).createRow(rowIndex);
						for (int columnIndex = 0; columnIndex < titleColumn.length; columnIndex++) {
							String title = titleColumn[columnIndex].toString().trim();
							// 使首字母大写
							String UTitle = Character.toUpperCase(title.charAt(0)) + title.substring(1, title.length()); // 使其首字母大写;
							String methodName = "get" + UTitle;
							// 设置要执行的方法
							Method method = clsss.getMethod(methodName);
							String data = method.invoke(obj) == null ? "" : method.invoke(obj).toString();
							Cell cell = dataRow.createCell(columnIndex);
							cell.setCellValue(data);
						}
					}
				}
			}
			workbook.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 将16进制的颜色代码写入样式中来设置颜色
	 * 
	 * @param style
	 *            保证style统一
	 * @param color
	 *            颜色：66FFDD
	 * @param index
	 *            索引 8-64 使用时不可重复
	 * @return
	 */
	public CellStyle setColor(CellStyle style, String color, short index) {
		if (color != "" && color != null) {
			// 转为RGB码
			int r = Integer.parseInt((color.substring(0, 2)), 16); // 转为16进制
			int g = Integer.parseInt((color.substring(2, 4)), 16);
			int b = Integer.parseInt((color.substring(4, 6)), 16);
			// 自定义cell颜色
			HSSFPalette palette = workbook.getCustomPalette();
			palette.setColorAtIndex((short) index, (byte) r, (byte) g, (byte) b);

			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			style.setFillForegroundColor(index);
		}
		return style;
	}

	/**
	 * 设置字体并加外边框
	 * 
	 * @param style
	 *            样式
	 * @param style
	 *            字体名
	 * @param style
	 *            大小
	 * @return
	 */
	public CellStyle setFontAndBorder(CellStyle style, String fontName, short size) {
		HSSFFont font = workbook.createFont();
		font.setFontHeightInPoints(size);
		font.setFontName(fontName);
		style.setFont(font);
		style.setBorderBottom(CellStyle.BORDER_THIN); // 下边框
		style.setBorderLeft(CellStyle.BORDER_THIN);// 左边框
		style.setBorderTop(CellStyle.BORDER_THIN);// 上边框
		style.setBorderRight(CellStyle.BORDER_THIN);// 右边框
		return style;
	}
	
	
	/**
	 * ==================================================================解析excel
	 */
	 /**
     * 根据输入流ins获取WorkBook对象
     *
     * @param ins 输入流
     * @return workbook
     * @throws Exception
     */
    public static Workbook getWorkBookByStream(InputStream ins) throws Exception {
        return WorkbookFactory.create(ins);
    }
 
    /**
     * 根据Workbook,sheetIndex获取sheet对象
     *
     * @param book WorkBook对象
     * @param number sheetIndex ,从1开始
     * @return sheet
     * @throws Exception
     */
    public static Sheet getSheetByNum(Workbook book, int number) throws Exception {
        return book.getSheetAt(number - 1);
    }
 
    /**
     * 根据 Workbook对象返回该Workbook对象中所有sheet的Map数组.
     *
     * @param book
     * @return Map<sheetIndex , sheetName>
     * @throws Exception
     */
    public static Map<Integer, String> getSheetNameByBook(Workbook book) throws Exception {
        Map<Integer, String> map = new LinkedHashMap<Integer, String>();
        int sheetNum = book.getNumberOfSheets();
        for (int indexSheet = 1; indexSheet <= sheetNum; indexSheet++) {
            Sheet sheet = getSheetByNum(book, indexSheet);
            map.put(indexSheet, sheet.getSheetName());
        }
        return map;
    }
 
    /**
     * 获取workbook数据Map集合
     *
     * @param book
     * @return Map<Integer, List<List<String>>> @
     * throws Exception
     */
    public static Map<Integer, List<List<String>>> getWorkbookDatas(Workbook book) throws Exception {
        Map<Integer, List<List<String>>> bookdatas = new HashMap<Integer, List<List<String>>>();
        int sheetNum = book.getNumberOfSheets();
        for (int indexSheet = 1; indexSheet <= sheetNum; indexSheet++) {
            Sheet sheet = getSheetByNum(book, indexSheet);
            bookdatas.put(indexSheet, getSheetDataList(sheet));
        }
        return bookdatas;
    }
 
    /**
     * 获取sheet中的数据
     *
     * @param sheet
     * @return List<List<String>> @
     * throws Exception
     */
    public static List<List<String>> getSheetDataList(Sheet sheet) throws Exception {
        List<List<String>> sheetdatas = new ArrayList<List<String>>();
        //需要先合并单元格数据
        mergedRegion(sheet);
        int lastRowNum = getRowNum(sheet);
        int lastCellNum = getColumnNum(sheet);
        for (int i = 0; i < lastRowNum; i++) {
            Row row = sheet.getRow(i);
            sheetdatas.add(getRowDataList(sheet, row, lastCellNum));
        }
        return sheetdatas;
    }
    /**
     * 获取sheet中的数据
     *
     * @param sheet
     * @return List<List<String>> @
     * throws Exception
     */
    public static List<List<String>> getNotRegionSheetDataList(Sheet sheet) throws Exception {
        List<List<String>> sheetdatas = new ArrayList<List<String>>();
        //需要先合并单元格数据
        int lastRowNum = getRowNum(sheet);
        int lastCellNum = getColumnNum(sheet);
        for (int i = 0; i < lastRowNum; i++) {
            Row row = sheet.getRow(i);
            sheetdatas.add(getRowDataList(sheet, row, lastCellNum));
        }
        return sheetdatas;
    }
    
 
    /**
     * 获取的数据对象是符合easyui格式的标准JSON对象数据集[{A:x,B:xx,C:xxx},{A:x,B:xx,C:xxx}]
     *
     * @param sheet
     * @return
     */
    public static List<Map<String, String>> getSheetDataMap(Sheet sheet) {
        List<Map<String, String>> sheetdatas = new ArrayList<Map<String, String>>();
        int lastRowNum = getRowNum(sheet);
        Row row;
        for (int i = 0; i < lastRowNum; i++) {
            row = sheet.getRow(i);
            Map<String, String> map = getRowDataMap(sheet, row);
            if (!map.isEmpty()) {
                sheetdatas.add(map);
            }
        }
        return sheetdatas;
    }
 
    /**
     * 获取的数据对象是符合dHtml格式的非标准JSON对象数据集[{id:1 , data:[x,xx,xxx]},{id:2
     * ,data:[x,xx,xxx]}]
     *
     * @param sheet
     * @return
     */
    public static List<Map<String, Object>> getSheetDataMapAndId(Sheet sheet) throws Exception {
        List<Map<String, Object>> sheetdatas = new ArrayList<Map<String, Object>>();
        List<List<String>> sheetLists = getSheetDataList(sheet);
        for (int i = 0; i < sheetLists.size(); i++) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("id", i);
            dataMap.put("data", sheetLists.get(i));
            sheetdatas.add(dataMap);
        }
        return sheetdatas;
    }
    
    public static List<Map<String, Object>> getNotRegionSheetDataMapAndId(Sheet sheet) throws Exception {
        List<Map<String, Object>> sheetdatas = new ArrayList<Map<String, Object>>();
        List<List<String>> sheetLists = getNotRegionSheetDataList(sheet);
        for (int i = 0; i < sheetLists.size(); i++) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("id", i);
            dataMap.put("data", sheetLists.get(i));
            sheetdatas.add(dataMap);
        }
        return sheetdatas;
    }
    
 
    /**
     * 读取一行的数据,返回的是数据集合List,[x,xx,xxx]
     *
     * @param row
     */
    public static List<String> getRowDataList(Sheet sheet, Row row, int lastCellNum) {
        List<String> rowdatas = new ArrayList<String>();
        if (row == null) {
            for (int i = 0; i < lastCellNum; i++) {
                rowdatas.add("");
            }
        } else {
            for (int i = 0; i < lastCellNum; i++) {
                rowdatas.add(getCellData(row.getCell(i)));
            }
        }
        return rowdatas;
    }
 
    /**
     * 获取一行的数据集合,体现的是Map<String , String>{A:x,B:xx,C:xxx}
     *
     * @param row
     * @return
     */
    public static Map<String, String> getRowDataMap(Sheet sheet, Row row) {
        Map<String, String> rowdatas = new LinkedHashMap<String, String>();
        String cellVaue;
        int columnNum = 0;
        int lastCellNum = getColumnNum(sheet);
        for (int j = 0; j < lastCellNum; j++) {
            cellVaue = getCellData(row.getCell(j));
            rowdatas.put(getCharByNum(columnNum) + "", cellVaue);
            columnNum = columnNum + 1;
        }
        return rowdatas;
    }
 
    /**
     * 获取指定Sheet中指定一列的数据.
     *
     * @param sheet 指定的Sheet
     * @param colIndex 指定的列
     * @return
     * @throws Exception
     */
    public static List<String> getColumnDataList(Sheet sheet, int colIndex) throws Exception {
        List<String> coldatas = new ArrayList<String>();
        int lastRowNum = getRowNum(sheet);
        for (int i = 0; i < lastRowNum; i++) {
            coldatas.add(getSheetCellValueWithRowIndexAndColIndex(sheet, i, colIndex));
        }
        return coldatas;
    }
 
    /**
     * 返回指定sheet页的最大行数,例如:25,则表示下标从0...24
     *
     * @param book
     * @param sheetIndex
     * @return
     */
    public static int getRowNum(Sheet sheet) {
        return sheet.getLastRowNum() + 1;
    }
 
    /**
     * 返回指定sheet页的最大列数,例如:25,则表示下标从0...24
     *
     * @param book
     * @param sheetIndex
     * @return 列数
     */
    public static int getColumnNum(Sheet sheet) {
        int maxclNum = 0;
        int lastRowNum = getRowNum(sheet);
        for (int i = 0; i < lastRowNum; i++) {
            if (sheet.getRow(i) != null) {
                int tempNum = sheet.getRow(i).getLastCellNum();
                if (tempNum > maxclNum) {
                    maxclNum = tempNum;
                }
            }
        }
        return maxclNum;
    }
 
    /**
     * 获取单元格的名称 按照excel常见的名称 例如A1
     *
     * @param int rowInt 行数 从0开始
     * @param int columnInt 列数 从0开始
     * @return String
     */
    public static String getCellName(int rowInt, int columnInt) {
        CellReference cellReference = new CellReference(rowInt, columnInt);
        return cellReference.formatAsString();
    }
 
    /**
     * 获取指定单元格的行坐标
     *
     * @param cellName 例如A1
     * @return 2
     */
    public static int getCellRowIndex(String cellName) {
        CellReference cellReference = new CellReference(cellName);
        return cellReference.getRow();
    }
 
    /**
     * 获取指定单元格的列坐标
     *
     * @param cellName 例如A1
     * @return 0
     */
    public static int getCellColIndex(String cellName) {
        CellReference cellReference = new CellReference(cellName);
        return cellReference.getCol();
    }
 
    /**
     * 获取指定sheet中指定rowNum和cellNum的值
     *
     * @param sheet
     * @param rowNum
     * @param cellNum
     * @return
     * @throws Exception
     */
    public static String getSheetCellValueWithRowIndexAndColIndex(Sheet sheet, int rowNum, int cellNum) throws Exception {
        Row row = sheet.getRow(rowNum);
        Cell cell = row.getCell(cellNum);
        return getCellData(cell);
    }
 
    /**
     * 获取给定SHEET中指定单元格的值
     *
     * @param sheet 指定SHEET
     * @param cellName 格式为：A1,B3
     * @return
     */
    public static String getSheetCellValueWithCellName(Sheet sheet, String cellName) {
        CellReference cellReference = new CellReference(cellName);
        Row row = sheet.getRow(cellReference.getRow());
        Cell cell = row.getCell(cellReference.getCol());
        return getCellData(cell);
    }
 
    /**
     * 获得cell单元格的TypeNumber,范围是0~5
     *
     * @param cell
     * @return
     */
    public static int getColumnTypeNumber(Cell cell) {
        if (cell != null) {
            int type = cell.getCellType();
            return type;
        }
        return -1;
    }
 
    /**
     * 获取指定Sheet页 所有合并单元格数据信息
     *
     * @param sheet
     * @return List<Map<String, String>>
     */
    public static List<Map<String, String>> getSheetRegion(Sheet sheet) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        //合并的单元格数量
        int merged = sheet.getNumMergedRegions();
        //预读合并的单元格
        for (int i = 0; i < merged; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            Map<String, String> map = new LinkedHashMap<String, String>();
            int colstart = range.getFirstColumn();
            int colend = range.getLastColumn();
            int rowstart = range.getFirstRow();
            int rowend = range.getLastRow();
            map.put("colstart", colstart + "");
            map.put("colend", colend + "");
            map.put("rowstart", rowstart + "");
            map.put("rowend", rowend + "");
            map.put("field", getCharByNum(colstart));
            map.put("colspan", (colend - colstart + 1) + "");
            map.put("rowspan", (rowend - rowstart + 1) + "");
            map.put("index", rowstart + "");
            list.add(map);
        }
        return list;
    }
 
    /**
     * 获取sheet中指定column的列宽度,这里的宽度是近似宽度,不是很精确
     *
     * @param sheet
     * @param cloumI
     * @return
     */
    public static int getColumnWidth(Sheet sheet, int cloumI) {
        return new BigDecimal(sheet.getColumnWidth(cloumI) * 37 / 1200).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    }
 
    /**
     * 获取sheet中指定column的列宽度集合,这里的宽度是近似宽度,不是很精确
     *
     * @param sheet
     * @return
     */
    public static List<Integer> getColumnWidths(Sheet sheet) {
        List<Integer> columnWidths = new ArrayList<Integer>();
        int lastCellNum = getColumnNum(sheet);
        for (int i = 0; i < lastCellNum; i++) {
            columnWidths.add(new BigDecimal(sheet.getColumnWidth(i) * 37 / 1200).setScale(0, BigDecimal.ROUND_HALF_UP).intValue());
        }
        return columnWidths;
    }
 
    /**
     * 获取一个Sheet的冻结信息,包括冻结列和冻结行
     *
     * @param sheet
     * @return
     * @throws Exception
     */
    public static Map<String, Short> getSheetFrazenColAndRow(Sheet sheet) throws Exception {
        Map<String, Short> frazenMap = new HashMap<String, Short>();
        PaneInformation paneInformation = sheet.getPaneInformation();
        if (paneInformation != null) {
            //有多少列是冻结的
            frazenMap.put("freezeCol", paneInformation.getVerticalSplitLeftColumn());
            //有多少行是冻结
            frazenMap.put("freezeRow", paneInformation.getHorizontalSplitTopRow());
        }
        return frazenMap;
    }
 
    /**
     * 获取单元中值(字符串类型)
     *
     * @param cell
     * @return
     */
    public static String getCellData(Cell cell) {
        String cellValue = "";
        if (cell != null) {
            try {
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_BLANK://空白
                        cellValue = "";
                        break;
                    case Cell.CELL_TYPE_NUMERIC: //数值型 0----日期类型也是数值型的一种
                        if (DateUtil.isCellDateFormatted(cell)) {
                            short format = cell.getCellStyle().getDataFormat();
 
                            if (yyyyMMddList.contains(format)) {
                                sFormat = new SimpleDateFormat("yyyy-MM-dd");
                            } else if (hhMMssList.contains(format)) {
                                sFormat = new SimpleDateFormat("HH:mm:ss");
                            }
                            Date date = cell.getDateCellValue();
                            cellValue = sFormat.format(date);
                        } else {
                            Double numberDate = new BigDecimal(cell.getNumericCellValue()).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
                            cellValue = numberDate + "";
                        }
                        break;
                    case Cell.CELL_TYPE_STRING: //字符串型 1
                        cellValue = replaceBlank(cell.getStringCellValue());
                        break;
                    case Cell.CELL_TYPE_FORMULA: //公式型 2
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        cellValue = replaceBlank(cell.getStringCellValue());
                        break;
                    case Cell.CELL_TYPE_BOOLEAN: //布尔型 4
                        cellValue = String.valueOf(cell.getBooleanCellValue());
                        break;
                    case Cell.CELL_TYPE_ERROR: //错误 5
                        cellValue = "!#REF!";
                        break;
                }
            } catch (Exception e) {
                System.out.println("读取Excel单元格数据出错：" + e.getMessage());
                return cellValue;
            }
        }
        return cellValue;
    }
 
    public static String replaceBlank(String source) {
        String dest = "";
        if (source != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(source);
            dest = m.replaceAll("");
        }
        return dest;
    }
 
    /**
     * 给SHEET某一个单元格赋值
     *
     * @param sheet 指定单元格
     * @param rowNum 行号
     * @param cellNum 列号
     * @param value 值
     */
    public static void setCellValue(Sheet sheet, int rowNum, int cellNum, String value) {
        Row row = sheet.getRow(rowNum);
        Cell cell = row.getCell(cellNum);
        if (cell == null) {
            row.createCell(cellNum).setCellValue(value);
        } else {
            cell.setCellValue(value);
        }
    }
 
    public static void mergedRegion(Sheet sheet) throws Exception {
        //合并的单元格数量
        int merged = sheet.getNumMergedRegions();
        //预读合并的单元格
        for (int i = 0; i < merged; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int y0 = range.getFirstRow();
            int x0 = range.getFirstColumn();
            int y1 = range.getLastRow();
            int x1 = range.getLastColumn();
 
            String value = getSheetCellValueWithRowIndexAndColIndex(sheet, y0, x0);
 
            for (int m = y0; m <= y1; m++) {
                for (int n = x0; n <= x1; n++) {
                    setCellValue(sheet, m, n, value);
                }
            }
        }
    }
 
    /**
     * 生成表头名称,A,B,C,D...
     *
     * @param number
     * @return
     */
    public static String getCharByNum(int number) {
        int index = number / 26 - 1;
        if (index < 0) {
            return (char) (65 + number % 26) + "";
        } else if (index >= 0) {
            return (char) (65 + index) + "" + (char) (65 + number % 26) + "";
        }
        return "@";
    }
 
    /**
     * 补全String字符串,
     *
     * @param str 字符窜
     * @param len 长度
     * @param pre 补全字符
     * @return 补全之后的字符串
     */
    public static String preFillString(String str, int len, char pre) {
        int length = len - str.length();
        for (int i = 0; i < length; i++) {
            str = pre + str;
        }
        return str;
    }
 
    /**
     * 获取颜色的HTML表示方式,
     *
     * @param str getHexString()
     * @return
     */
    public static String getColorByHex(String str) {
        String[] hexString = str.split(":");
        String colorRGB = "";
        for (int i = 0; i < hexString.length; i++) {
            hexString[i] = preFillString(hexString[i], 4, '0');
            colorRGB += hexString[i].substring(0, 2);
        }
        if ("000000".equals(colorRGB)) {
            colorRGB = "";
        }
        return colorRGB;
    }
 
    /**
     * 获取颜色
     *
     * @param shortColor
     * @return
     */
    public static String getColorByShortColor(short shortColor) {
        String returnColor = "";
        for (IndexedColors color : IndexedColors.values()) {
            if (shortColor == color.getIndex()) {
                returnColor = color.toString();
            }
        }
        if ("AUTOMATIC".equals(returnColor)) {
            returnColor = "";
        }
        return returnColor;
    }
 
    /**
     * 获取Sheet中所有单元格样式合集
     *
     * @param sheet
     * @return
     * @throws Exception
     */
    public static List<Map<String, Object>> getSheetCellStyleMaps(Sheet sheet) throws Exception {
        List<Map<String, Object>> sheetCellStyles = new ArrayList<Map<String, Object>>();
        int lastRowNum = getRowNum(sheet);
        Row row;
        for (int i = 0; i < lastRowNum; i++) {
            row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            int columnNumMax = getColumnNum(sheet);
            for (int j = 0; j < columnNumMax; j++) {
                Cell cell = row.getCell(j);
                if (cell == null) {
                    continue;
                }
                Map<String, Object> cellMap = getCellStyleMap(sheet, cell);
                cellMap.put("y", i);
                cellMap.put("x", j);
                sheetCellStyles.add(cellMap);
            }
        }
        return sheetCellStyles;
    }
 
    /**
     * 获取Sheet中,某一个Cell的样式,Cell的背景颜色单独去取,借助于HSSFSheet和XSSFSheet
     *
     * @param sheet
     * @param cell
     * @return
     */
    public static Map<String, Object> getCellStyleMap(Sheet sheet, Cell cell) {
        Map<String, Object> cellStyleMap = new HashMap<String, Object>();
 
        Short alignShort = cell.getCellStyle().getAlignment();
        String alignment = "c";
        if (alignShort == 1) {
            alignment = "l";
        } else if (alignShort == 3) {
            alignment = "r";
        }
 
        CellStyle cellStyle = cell.getCellStyle();
        Workbook workbook = sheet.getWorkbook();
        Font font = workbook.getFontAt(cellStyle.getFontIndex());
        cellStyleMap.put("fontColor", getColorByShortColor(font.getColor()));
        cellStyleMap.put("fontBold", font.getBoldweight());
        cellStyleMap.put("fontSize", font.getFontHeightInPoints());
        cellStyleMap.put("alignment", alignment);
        try {
            HSSFCellStyle hSSFCellStyle = (HSSFCellStyle) cell.getCellStyle();
            cellStyleMap.put("cellColor", getColorByHex(hSSFCellStyle.getFillForegroundColorColor().getHexString()));
        } catch (Exception e) {
            XSSFCellStyle xSSFCellStyle = (XSSFCellStyle) cell.getCellStyle();
            String xssfCellColor = "";
            if (xSSFCellStyle.getFillBackgroundColorColor() != null) {
                xssfCellColor = xSSFCellStyle.getFillForegroundColorColor().getARGBHex().substring(2);
            }
            xssfCellColor = "000000".equals(xssfCellColor) ? "" : xssfCellColor;
            cellStyleMap.put("cellColor", xssfCellColor);
        }
        return cellStyleMap;
    }
    public static void main(String[] args) throws Exception {
        Workbook workbook = getWorkBookByStream(new FileInputStream("C:\\Users\\Administrator\\Desktop\\666.xls"));
        Sheet sheet = getSheetByNum(workbook, 1);
        List<Map<String, Object>> list = getSheetDataMapAndId(sheet);
    }
}
