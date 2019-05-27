package misc;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class TurnInto {
	public static Timestamp timeStamp(String s){
		int year = Integer.valueOf(s.substring(0, 4))-1900;
		int Month = Integer.valueOf(s.substring(5, 7))-1;
		int Day = Integer.valueOf(s.substring(8, 10));
		int Hours = Integer.valueOf(s.substring(11, 13));
		int Minutes = Integer.valueOf(s.substring(14, 16));
		int seconds = Integer.valueOf(s.substring(17, 19));
		int mili = Integer.valueOf(s.substring(20, 23))*1000000;
		return new Timestamp(year,Month,Day,Hours,Minutes,seconds,mili);
	}
	
	public static Calendar date(String s) {
		int year = Integer.valueOf(s.substring(0, 4));
		int month = Integer.valueOf(s.substring(5, 7));
		int day = Integer.valueOf(s.substring(8, 10));
		int hours = Integer.valueOf(s.substring(11, 13));
		int minutes = Integer.valueOf(s.substring(14, 16));
		int seconds = Integer.valueOf(s.substring(17, 19));
		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(year, month, day, hours, minutes, seconds);
		return c;
	}
}
