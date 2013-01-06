package chao.android.tenyearsfromnow;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utility
{
   public static final SimpleDateFormat GOAL_DATE_FORMAT = new SimpleDateFormat(
                                                               "MMMMM dd, yyyy" );

   public static String convertTimestampToDateString( long timestamp )
   {
      return GOAL_DATE_FORMAT.format( new Date( timestamp ) );
   }

   public static Calendar convertTimestampToCalendar( long timestamp )
   {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime( new Date( timestamp ) );

      return calendar;
   }

   public static String shortenString( String longText )
   {
      final int MAXIMUM_LENGTH = 60;

      String shortText;

      if( longText.length() < MAXIMUM_LENGTH )
      {
         shortText = longText;
      }
      else
      {
         shortText = longText.substring( 0, MAXIMUM_LENGTH ) + "...";
      }

      return shortText;
   }
}
