package org.james.javafx.shutdown;

import java.util.HashMap;
import java.util.Map;

public class DateTimeUtils {

	public static Map<String, Long> getCountdownMap(long distTime) {
		Map<String, Long> resultMap = new HashMap<String, Long>();
		long day = ((distTime / 1000) / (3600 * 24));
		long hour = ((distTime / 1000) - day * 86400) / 3600;
		long minutes = ((distTime / 1000) - day * 86400 - hour * 3600) / 60;
		long seconds = (distTime / 1000) - day * 86400 - hour * 3600 - minutes * 60;
		resultMap.put("day", day);
		resultMap.put("hour", hour);
		resultMap.put("minutes", minutes);
		resultMap.put("seconds", seconds);
		return resultMap;
	}
}
