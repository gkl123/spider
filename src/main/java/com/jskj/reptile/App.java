package com.jskj.reptile;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.jskj.reptile.domain.OrderTypeEnum;
import com.jskj.reptile.domain.QueryDateEnum;
import com.jskj.reptile.domain.UserLoanInfo;
import com.jskj.reptile.domain.UserOutputVO;
import com.jskj.reptile.htmlparser.DataParser;
import com.jskj.reptile.htmlparser.DataSpider;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {
        
    	DataSpider spider = new DataSpider();
    	String defaultTime = QueryDateEnum.OVERDUEONEDAY.code;
    	String defaultType = null;
    	
    	if (args.length >= 1 && StringUtils.isNotEmpty(args[0])) {
    		defaultTime = QueryDateEnum.getByType(args[0]).code;
    	}
    	
    	if (args.length >= 2 && StringUtils.isNotEmpty(args[1])) {
    		defaultType = OrderTypeEnum.getByType(args[1]).code;
    	}
    	List<UserLoanInfo> userInfos = spider.getAllUserInfo(defaultTime, defaultType);
    	DataParser parser = new DataParser();
    	List<UserOutputVO> userOutputInfo = parser.getUserOutputInfo(userInfos); // 该对象单独打印一个excel文件；
    	
    	HashMap<String, Integer> provinceInfo = parser.parseProvince(userInfos); // 城市次数统计对象 ,单独一个文件;
    	
    	HashMap<String, Integer> ageInfo = parser.parseAge(userInfos); // 年龄段统计, 单独一个文件
    	
    	HashMap<String, Integer> appInfo = parser.appCount(userInfos); // 统计App出现次数
//    	System.out.println(userInfos);
    	
    }
}
