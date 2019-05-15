package misc;

import java.sql.Timestamp;

public class TurnInto {
	public static Timestamp timeStamp(String s){
		int year = Integer.valueOf(s.substring(0, 4))-1900;
		int Month = Integer.valueOf(s.substring(5, 7));
		int Day = Integer.valueOf(s.substring(8, 10));
		int Hours = Integer.valueOf(s.substring(11, 13));
		int Minutes = Integer.valueOf(s.substring(14, 16));
		int seconds = Integer.valueOf(s.substring(17, 19));
		int mili = Integer.valueOf(s.substring(20, 23));
		return new Timestamp(year,Month,Day,Hours,Minutes,seconds,mili);
	}
}
