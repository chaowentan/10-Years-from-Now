package chao.android.tenyearsfromnow.CalendarAdapter;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import chao.android.tenyearsfromnow.R;
import chao.android.tenyearsfromnow.R.id;
import chao.android.tenyearsfromnow.R.layout;

import junit.framework.Assert;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class YearlyCalendarAdapter extends CalendarAdapter
{
   static final String[] MONTH_NAMES = (new DateFormatSymbols()).getMonths();

   // TODO: Remove localization-dependent month names.

   private enum ListRowViewType
   {
      YEAR, MONTH_NAMES, WEEKS, END_OF_YEAR
   };

   public YearlyCalendarAdapter(Context context)
   {
      super( context );
   }

   // ////////////////////////////////////////////////////////////////////////

   private class CalendarData
   {
      final ListRowViewType type;
      final int             year;
      final int             biMonth;
      final int             weekInMonth;

      CalendarData(ListRowViewType type, int year, int biMonth, int weekInMonth)
      {
         this.type = type;
         this.year = year;
         this.biMonth = biMonth;
         this.weekInMonth = weekInMonth;
      }

      @Override
      public String toString()
      {
         return type.toString() + " " + Integer.toString( year ) + " "
               + Integer.toString( biMonth ) + " "
               + Integer.toString( weekInMonth );
      }
   }

   // ////////////////////////////////////////////////////////////////////////

   public int getPositionForCurrentTime()
   {
      int position = MID_POSITION;
      int currentYear = CURRENT_CALENDAR.get( Calendar.YEAR );
      int currentMonth = CURRENT_CALENDAR.get( Calendar.MONTH );

      while( true )
      {
         CalendarData calendarData = getCalendarDataFromPosition( position );
         int month1 = calendarData.biMonth * 2;
         int month2 = month1 + 1;

         if( calendarData.type == ListRowViewType.MONTH_NAMES
               && calendarData.year == currentYear
               && (month1 == currentMonth || month2 == currentMonth) )
         {
            break;
         }
         else
         {
            ++position;
         }
      }

      return position;
   }

   // ////////////////////////////////////////////////////////////////////////

   private CalendarData getCalendarDataFromPosition( int position )
   {
      ListRowViewType type = ListRowViewType.YEAR;
      int year = 0;
      int biMonth = 0;
      int weekInMonth = 0;

      // Define constants.

      final int NUM_OF_ROW_IN_A_MONTHLY_CALENDAR = 1 + 6;

      final int NUM_OF_MONTHLY_CALENDARS_IN_A_ROW = 2;
      final int NUM_OF_MONTHS_IN_A_YEAR = 12;
      final int NUM_OF_MONTHLY_CALENDAR_ROWS = NUM_OF_MONTHS_IN_A_YEAR
            / NUM_OF_MONTHLY_CALENDARS_IN_A_ROW;

      final int NUM_OF_ROWS_IN_A_YEARLY_CALENDAR = 1 + 1
            + NUM_OF_ROW_IN_A_MONTHLY_CALENDAR * NUM_OF_MONTHLY_CALENDAR_ROWS;

      // Determine calendar data.

      int index = position - MID_POSITION;

      int offset = index % NUM_OF_ROWS_IN_A_YEARLY_CALENDAR;
      if( offset < 0 )
      {
         offset += NUM_OF_ROWS_IN_A_YEARLY_CALENDAR;
      }

      if( offset == 0 )
      {
         type = ListRowViewType.YEAR;
      }
      else if( offset == NUM_OF_ROWS_IN_A_YEARLY_CALENDAR - 1 )
      {
         type = ListRowViewType.END_OF_YEAR;
      }
      else if( (offset - 1) % NUM_OF_ROW_IN_A_MONTHLY_CALENDAR == 0 )
      {
         type = ListRowViewType.MONTH_NAMES;
      }
      else
      {
         type = ListRowViewType.WEEKS;
      }

      year = index / NUM_OF_ROWS_IN_A_YEARLY_CALENDAR;
      if( index < 0 )
      {
         int negativeIndex = index - NUM_OF_ROWS_IN_A_YEARLY_CALENDAR + 1;

         year = -1
               * ((Math.abs( negativeIndex ) / NUM_OF_ROWS_IN_A_YEARLY_CALENDAR));
      }

      biMonth = (offset - 1) / NUM_OF_ROW_IN_A_MONTHLY_CALENDAR;

      weekInMonth = (offset - 1) % NUM_OF_ROW_IN_A_MONTHLY_CALENDAR;

      return new CalendarData( type, CURRENT_CALENDAR.get( Calendar.YEAR )
            + year, biMonth, weekInMonth );
   }

   // ////////////////////////////////////////////////////////////////////////

   public View getView( int position, View convertView, ViewGroup parent )
   {
      View view;

      CalendarData calendarData = getCalendarDataFromPosition( position );

      switch( calendarData.type )
      {
      case YEAR:
         view = getYearView( position, convertView, parent, calendarData );
         break;
      case MONTH_NAMES:
         view = getMonthNamesView( position, convertView, parent, calendarData );
         break;
      case WEEKS:
         view = getWeeksView( position, convertView, parent, calendarData );
         break;
      case END_OF_YEAR:
         view = getEndOfYearView( convertView, parent );
         break;
      default:
         view = null;
         break;
      }

      return view;
   }

   // ////////////////////////////////////////////////////////////////////////

   public View getYearView(
         int position,
         View convertView,
         ViewGroup parent,
         CalendarData calendarData )
   {
      View view;

      // Creating or recycling.
      if( convertView != null && convertView.getId() == R.id.yearRow )
      {
         view = convertView;
      }
      else
      {
         view = mInflater.inflate(
               R.layout.yearly_calendar_row_year,
               parent,
               false );
      }

      TextView textView = (TextView) view;

      textView.setText( Integer.toString( calendarData.year ) );

      if( calendarData.year < CURRENT_CALENDAR.get( Calendar.YEAR ) )
      {
         setInactiveTextStyle( textView );
      }
      else
      {
         setActiveTextStyle( textView );
      }

      textView.setTypeface( mThinTypeface );

      return view;
   }

   // ////////////////////////////////////////////////////////////////////////

   public View getMonthNamesView(
         int position,
         View convertView,
         ViewGroup parent,
         CalendarData calendarData )
   {
      View view;

      // Creating or recyling.
      if( convertView != null && convertView.getId() == R.id.monthNamesRow )
      {
         view = convertView;
      }
      else
      {
         view = mInflater.inflate(
               R.layout.yearly_calendar_row_month_names,
               parent,
               false );
      }

      // Caching
      if( view.getTag() == null )
      {
         TextView[] monthNameList = new TextView[2];
         monthNameList[ 0 ] = (TextView) view.findViewById( R.id.monthName1 );
         monthNameList[ 1 ] = (TextView) view.findViewById( R.id.monthName2 );

         view.setTag( monthNameList );
      }

      int year = calendarData.year;
      Calendar calendar = (Calendar) CURRENT_CALENDAR.clone();

      TextView[] monthNameList = (TextView[]) view.getTag();
      int[] monthList = new int[] { calendarData.biMonth * 2,
            calendarData.biMonth * 2 + 1 };

      Assert.assertTrue( monthNameList.length == monthList.length );

      for( int i = 0; i < monthNameList.length; i++ )
      {
         int month = monthList[ i ];
         TextView monthNameView = monthNameList[ i ];

         calendar.set( year, month, 1 );
         calendar.set(
               year,
               month,
               calendar.getActualMaximum( Calendar.DAY_OF_MONTH ) );

         monthNameView.setText( MONTH_NAMES[ month ] );

         if( calendar.before( CURRENT_CALENDAR ) )
         {
            setInactiveTextStyle( monthNameView );
         }
         else
         {
            setActiveTextStyle( monthNameView );
         }

         monthNameView.setTypeface( mLightTypeface );

      }

      return view;
   }

   // ////////////////////////////////////////////////////////////////////////

   public View getWeeksView(
         int position,
         View convertView,
         ViewGroup parent,
         CalendarData calendarData )
   {
      View view;

      // Creating or recyling.
      if( convertView != null && convertView.getId() == R.id.weeksRow )
      {
         view = convertView;
      }
      else
      {
         view = mInflater.inflate(
               R.layout.yearly_calendar_row_weeks,
               parent,
               false );
      }

      // Caching
      if( view.getTag() == null )
      {
         ViewGroup[] weekViewList = new ViewGroup[2];
         weekViewList[ 0 ] = (ViewGroup) view.findViewById( R.id.week1 );
         weekViewList[ 1 ] = (ViewGroup) view.findViewById( R.id.week2 );

         view.setTag( weekViewList );
      }

      ViewGroup[] weekViewList = (ViewGroup[]) view.getTag();

      int year = calendarData.year;
      int weekInMonth = calendarData.weekInMonth;
      int month1 = calendarData.biMonth * 2;
      int month2 = month1 + 1;

      setWeekViewData( weekViewList[ 0 ], year, weekInMonth, month1 );
      setWeekViewData( weekViewList[ 1 ], year, weekInMonth, month2 );

      return view;
   }

   // ////////////////////////////////////////////////////////////////////////

   private void setWeekViewData(
         ViewGroup weekView,
         int year,
         int weekInMonth,
         int month )
   {
      int[] week = getWeek( year, month, weekInMonth );

      for( int i = 0; i < weekView.getChildCount(); i++ )
      {
         int day = week[ i ];
         String dayString = (day == 0) ? "" : Integer.toString( day );

         Calendar calendar = (Calendar) CURRENT_CALENDAR.clone();
         calendar.set( year, month, day, 23, 55, 55 );

         TextView dayView = (TextView) weekView.getChildAt( i );
         dayView.setText( dayString );

         if( calendar.before( CURRENT_CALENDAR ) )
         {
            setInactiveTextStyle( dayView );
         }
         else
         {
            setActiveTextStyle( dayView );
         }

      }
   }

   // ////////////////////////////////////////////////////////////////////////

   // Get the 7 calendar days of a specific week, starting from Sunday.
   public int[] getWeek( int year, int month, int weekInMonth )
   {
      int[] week = new int[7];

      Calendar calendar = (Calendar) CURRENT_CALENDAR.clone();
      calendar.set( Calendar.YEAR, year );
      calendar.set( Calendar.MONTH, month );

      if( weekInMonth == 1 )
      {
         calendar.set( Calendar.DAY_OF_MONTH, 1 );

         // Note: Calendar.Sunday == 1, Monday == 2, etc.
         int weekdayOfFirstDay = calendar.get( Calendar.DAY_OF_WEEK ) - 1;

         for( int i = 0; i < weekdayOfFirstDay; i++ )
         {
            week[ i ] = 0;
         }

         for( int i = weekdayOfFirstDay; i < 7; i++ )
         {
            week[ i ] = i - weekdayOfFirstDay + 1;
         }
      }
      else
      {
         // Get the calendar day of the first Sunday in the specified month.
         // E.g. If Sep. 2nd, 2012 is the first Sunday of the month, then
         // firstSunday = 2.

         int firstSunday = 0;
         for( int i = 1; firstSunday == 0; i++ )
         {
            calendar.set( Calendar.DAY_OF_MONTH, i );
            if( calendar.get( Calendar.DAY_OF_WEEK ) == Calendar.SUNDAY )
            {
               firstSunday = i;
            }
         }

         // Get the calendar day of the Sunday of the second week in the
         // specified month.

         int sundayOfSecondWeek = (firstSunday != 1) ? firstSunday
               : firstSunday + 7;

         int firstDayOfSpecifiedWeek = sundayOfSecondWeek + 7
               * (weekInMonth - 2);

         for( int i = 0; i < 7; i++ )
         {
            int day = firstDayOfSpecifiedWeek + i;

            if( day > calendar.getActualMaximum( Calendar.DAY_OF_MONTH ) )
            {
               day = 0;
            }

            week[ i ] = day;
         }
      }

      return week;
   }

   // ////////////////////////////////////////////////////////////////////////

   public View getEndOfYearView( View convertView, ViewGroup parent )
   {
      View view;

      // Creating or recycling.
      if( convertView != null && convertView.getId() == R.id.endOfYearRow )
      {
         view = convertView;
      }
      else
      {
         view = mInflater.inflate(
               R.layout.yearly_calendar_row_end_of_year,
               parent,
               false );
      }

      return view;
   }
}
