
import java.text.SimpleDateFormat;
import java.util.Calendar;


import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateAndTime {
   
    
    public static final String DATE_FORMAT_NOW = "dd-MM-yyyy HH:mm:ss";
    public static String dt;
 
    public static String DateTime() 
    {
         Calendar cal = Calendar.getInstance();
         SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
         return sdf.format(cal.getTime());
    }
    
}
    

