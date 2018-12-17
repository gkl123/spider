package com.jskj.reptile.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jskj.reptile.domain.DataInfo;

public class HashMapUtil {
	
	@SuppressWarnings("rawtypes")
	public static List<DataInfo> transferMap(HashMap<String, Integer> map) {
		Iterator<Entry<String, Integer>> iter = map.entrySet().iterator();
		List<DataInfo> dataLists = new ArrayList<DataInfo>();
		while (iter.hasNext()) {
			DataInfo data = new DataInfo();
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			data.setProvince(key);
			Object val = entry.getValue();
			data.setNum((Integer)val);
			dataLists.add(data);
		}
		
		return dataLists;
	}

}
