package dev.rage.lootchests.util.time;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TimeUtil {
	
    public String HOUR_FORMAT = "%02d:%02d:%02d";
    public String MINUTE_FORMAT = "%02d:%02d";
    
    public String millisToTimer(long millis) {
        long seconds = millis / 1000L;
        if (seconds > 3600L) {
            return String.format("%02d:%02d:%02d", seconds / 3600L, seconds % 3600L / 60L, seconds % 60L);
        }
        return String.format("%02d:%02d", seconds / 60L, seconds % 60L);
    }
    
    public String millisToSeconds(long millis) {
        return new DecimalFormat("#0.0").format(millis / 1000.0f);
    }
    
    public String dateToString(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setTimeZone(TimeZone.getTimeZone("America/Mexico_City"));
        return calendar.getTime().toString();
    }
    
    public String dateToString(Date date, String secondaryColor) {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy " + (secondaryColor == null ? "" : secondaryColor) + "(hh:mm aa)");
    	dateFormat.setTimeZone(TimeZone.getTimeZone("America/Mexico_City"));
    	
		return dateFormat.format(date);
	}
    
    public Timestamp addDuration(long duration) {
        return truncateTimestamp(new Timestamp(System.currentTimeMillis() + duration));
    }
    
    public Timestamp truncateTimestamp(Timestamp timestamp) {
        if (timestamp.toLocalDateTime().getYear() > 2037) {
            timestamp.setYear(2037);
        }
        
        return timestamp;
    }
    
    public Timestamp addDuration(Timestamp timestamp) {
        return truncateTimestamp(new Timestamp(System.currentTimeMillis() + timestamp.getTime()));
    }
    
    public Timestamp fromMillis(long millis) {
        return new Timestamp(millis);
    }
    
    public Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
    
    public String readableTime(long millis) {
    	return readableTime(millis, null);
    }
    
    /**
     * Formats time into a detailed format. Example: 600000 millies (10 minutes) displays as '10 minutes'
     *
     * @param secs The input time, in seconds.
     * @return The formatted time.
     */
    public String readableTime(long millis, String infiniteWord) {
    	if (millis == Integer.MAX_VALUE && infiniteWord != null) {
    		return infiniteWord;
    	}
    	
    	int secs = (int) (millis / 1000);
    	
        if (secs == 0) {
            return "0 seconds";
        }
        int remainder = secs % 86400;

        int days = secs / 86400;
        int hours = remainder / 3600;
        int minutes = (remainder / 60) - (hours * 60);
        int seconds = (remainder % 3600) - (minutes * 60);

        String fDays = (days > 0 ? " " + days + " day" + (days > 1 ? "s" : "") : "");
        String fHours = (hours > 0 ? " " + hours + " hour" + (hours > 1 ? "s" : "") : "");
        String fMinutes = (minutes > 0 ? " " + minutes + " minute" + (minutes > 1 ? "s" : "") : "");
        String fSeconds = (seconds > 0 ? " " + seconds + " second" + (seconds > 1 ? "s" : "") : "");

        return ((fDays + fHours + fMinutes + fSeconds).trim());
    }
    
    public String shortReadableTime(long millis) {
    	return shortReadableTime(millis, null);
    }
    
    public String shortReadableTime(long millis, String infiniteWord) {
    	if (millis == Integer.MAX_VALUE && infiniteWord != null) {
    		return infiniteWord;
    	}
    	
    	int secs = (int) (millis / 1000);
    	
        if (secs == 0) {
            return "0 s";
        }
        int remainder = secs % 86400;

        int days = secs / 86400;
        int hours = remainder / 3600;
        int minutes = (remainder / 60) - (hours * 60);
        int seconds = (remainder % 3600) - (minutes * 60);

        String fDays = (days > 0 ? " " + days + "d" : "");
        String fHours = (hours > 0 ? " " + hours + "h" : "");
        String fMinutes = (minutes > 0 ? " " + minutes + "m" : "");
        String fSeconds = (seconds > 0 ? " " + seconds + "s" : "");

        return ((fDays + fHours + fMinutes + fSeconds).trim());
    }
    
    public String millisToRoundedTime(long millis) {
    	return millisToRoundedTime(millis, null);
    }
    
    public String millisToRoundedTime(long millis, String infiniteWord) {
    	if (millis == Integer.MAX_VALUE && infiniteWord != null) {
    		return infiniteWord;
    	}
    	
        ++millis;
        
        long seconds = millis / 1000L, minutes = seconds / 60L, hours = minutes / 60L, days = hours / 24L, weeks = days / 7L, months = weeks / 4L, years = months / 12L;
        if (years > 0L) {
            return years + " year" + ((years == 1L) ? "" : "s");
        }
        
        if (months > 0L) {
            return months + " month" + ((months == 1L) ? "" : "s");
        }
        
        if (weeks > 0L) {
            return weeks + " week" + ((weeks == 1L) ? "" : "s");
        }
        
        if (days > 0L) {
            return days + " day" + ((days == 1L) ? "" : "s");
        }
        
        if (hours > 0L) {
            return hours + " hour" + ((hours == 1L) ? "" : "s");
        }
        
        if (minutes > 0L) {
            return minutes + " minute" + ((minutes == 1L) ? "" : "s");
        }
        
        return seconds + " second" + ((seconds == 1L) ? "" : "s");
    }
    
    public long parseTime(String source) {
    	if (source.equalsIgnoreCase("perm") || source.equalsIgnoreCase("permanent")) {
            return 2147483647L;
        }
    	
        long totalTime = 0L;
        boolean found = false;
        Matcher matcher = Pattern.compile("\\d+\\D+").matcher(source);
        while (matcher.find()) {
            String s = matcher.group();
            Long value = Long.parseLong(s.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[0]);
            String type = s.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[1];
            switch (type) {
                case "s": {
                    totalTime += value;
                    found = true;
                    continue;
                } case "m": {
                    totalTime += value * 60L;
                    found = true;
                    continue;
                } case "h": {
                    totalTime += value * 60L * 60L;
                    found = true;
                    continue;
                } case "d": {
                    totalTime += value * 60L * 60L * 24L;
                    found = true;
                    continue;
                } case "w": {
                    totalTime += value * 60L * 60L * 24L * 7L;
                    found = true;
                    continue;
                } case "M": {
                    totalTime += value * 60L * 60L * 24L * 30L;
                    found = true;
                    continue;
                } case "y": {
                    totalTime += value * 60L * 60L * 24L * 365L;
                    found = true;
                    continue;
                }
            }
        }
        
        return found ? (totalTime * 1000L) : -1L;
    }
}