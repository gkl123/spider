package com.jskj.reptile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.jskj.reptile.domain.DataInfo;
import com.jskj.reptile.domain.OrderTypeEnum;
import com.jskj.reptile.domain.QueryDateEnum;
import com.jskj.reptile.domain.UserLoanInfo;
import com.jskj.reptile.domain.UserOutputVO;
import com.jskj.reptile.htmlparser.DataParser;
import com.jskj.reptile.htmlparser.DataSpider;
import com.jskj.reptile.utils.ExcelUtil;
import com.jskj.reptile.utils.HashMapUtil;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {
        
    	DataSpider spider = new DataSpider();
    	String defaultTime = QueryDateEnum.OVERDUEONEDAY.code;
    	String defaultType = null;
    	String path = args[0]; 
    	String dateName = new SimpleDateFormat("yyyyMMdd").format(new Date());
    	
    	if (args.length >= 2 && StringUtils.isNotEmpty(args[1])) {
    		defaultTime = QueryDateEnum.getByType(args[1]).code;
    	}
    	
    	if (args.length >= 3 && StringUtils.isNotEmpty(args[2])) {
    		defaultType = OrderTypeEnum.getByType(args[2]).code;
    	}
    	List<UserLoanInfo> userInfos = spider.getAllUserInfo(defaultTime, defaultType);
    	DataParser parser = new DataParser();
    	List<UserOutputVO> userOutputInfo = parser.getUserOutputInfo(userInfos); // 该对象单独打印一个excel文件；
    	
    	HashMap<String, Integer> provinceInfo = parser.parseProvince(userInfos); // 城市次数统计对象 ,单独一个文件;
    	
    	HashMap<String, Integer> ageInfo = parser.parseAge(userInfos); // 年龄段统计, 单独一个文件
    	
    	HashMap<String, Integer> appInfo = parser.appCount(userInfos); // 统计App出现次数
    	
    	ExcelUtil<UserOutputVO> util = new ExcelUtil<UserOutputVO>();
    	ExcelUtil<DataInfo> util4HashMap = new ExcelUtil<DataInfo>();
    	try {
    		File userFile = new File(path + "\\" + dateName + "用户信息列表.xlsx");
    		File provinceFile = new File(path + "\\" + dateName + "省份统计列表.xlsx");
    		File ageFile = new File(path + "\\" + dateName + "年龄统计列表.xlsx");
    		File appFile = new File(path + "\\" + dateName + "App统计列表.xlsx");
    		
    		String[] userInfoHeaders = {"用户", "手机", "紧急联系人是否正确", "是否停机", "是否被催收", "剩余话费", "信用状态", "被是否被轰炸过"};
			util.exportExcel2007("用户信息列表", userInfoHeaders, userOutputInfo, new FileOutputStream(userFile), ExcelUtil.EXCEL_FILE_2003);
			
			String[] provinceInfoHeaders = {"省份", "数量"};
			List<DataInfo> dataInfo = HashMapUtil.transferMap(provinceInfo);
			util4HashMap.exportExcel2007("用户信息列表", provinceInfoHeaders, dataInfo, 
					new FileOutputStream(provinceFile), ExcelUtil.EXCEL_FILE_2003);
			
			String[] ageInfoHeaders = {"年龄", "数量"};
			util4HashMap.exportExcel2007("用户信息列表", ageInfoHeaders, HashMapUtil.transferMap(ageInfo), 
			new FileOutputStream(ageFile), ExcelUtil.EXCEL_FILE_2003);
			
			String[] appInfoHeaders = {"应用名", "数量"};
			util4HashMap.exportExcel2007("用户信息列表", appInfoHeaders, HashMapUtil.transferMap(appInfo), 
			new FileOutputStream(appFile), ExcelUtil.EXCEL_FILE_2003);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }
}
