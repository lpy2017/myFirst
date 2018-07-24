package com.dc.appengine.appmaster.utils;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import com.dc.appengine.appmaster.entity.AuditEntity;

public class ExcelUtil {

	public static HSSFWorkbook getExeclData(List<AuditEntity> list) {
		
		
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("操作审计");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式

		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("操作用户");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("操作资源类型");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
		cell.setCellValue("操作资源名称");
		cell.setCellStyle(style);
		cell = row.createCell((short) 3);
		cell.setCellValue("操作类型");
		cell.setCellStyle(style);
		cell = row.createCell((short) 4);
		cell.setCellValue("操作结果");
		cell.setCellStyle(style);
		cell = row.createCell((short) 5);
		cell.setCellValue("操作时间");
		cell.setCellStyle(style);
		cell = row.createCell((short) 6);
		cell.setCellValue("详情");
		cell.setCellStyle(style);

		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		//List list = CreateSimpleExcelToDisk.getStudent();

		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow((int) i + 1);
			
			AuditEntity api = list.get(i);
			
			// 第四步，创建单元格，并设置值
			row.createCell((short) 0).setCellValue(api.getUserId());
			row.createCell((short) 1).setCellValue(api.getResourceType());
			row.createCell((short) 2).setCellValue(api.getResourceName());
			row.createCell((short) 3).setCellValue(converseOperateType(api.getOperateType()));
			row.createCell((short) 4).setCellValue(api.getOperateResult()==1?"成功":"失败");
			row.createCell((short) 5).setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(api.getOperateTime()));
			row.createCell((short) 6).setCellValue(api.getDetail());
		}
		// 第六步，将文件存到指定位置
		return wb;
//		try {
//			FileOutputStream fout = new FileOutputStream("E:/students.xls");
//			wb.write(fout);
//			fout.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
	private static String converseOperateType(String operateType){
		switch(operateType){
		case "login":
			return "登录";
		case "logout":
			return "注销";
		case "add":
			return "新增";
		case "update":
		    return "更新";
		case "delete":
			return "删除";
		case "import":
			return "导入";
//		case "export":
//			return "导出";
		case "clone":
			return "克隆";
		}
		return null;
	}

}
