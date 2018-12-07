package com.jskj.reptile;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.jskj.reptile.htmlparser.DataSpider;

import domain.OrderTypeEnum;
import domain.QueryDateEnum;
import domain.UserLoanInfo;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {
        
    	DataSpider spider = new DataSpider();
    	String defaultTime = QueryDateEnum.OVERDUEONEDAY.code;
    	String defaultType = OrderTypeEnum.OVERDUE.code;
    	
    	if (StringUtils.isNotEmpty(args[0])) {
    		defaultTime = QueryDateEnum.getByType(args[0]).code;
    	}
    	
    	if (StringUtils.isNotEmpty(args[1])) {
    		defaultType = OrderTypeEnum.getByType(args[1]).code;
    	}
    	List<UserLoanInfo> userInfos = spider.getUserInfo(defaultTime, defaultType);
    }
}
