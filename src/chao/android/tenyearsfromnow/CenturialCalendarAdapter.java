package chao.android.tenyearsfromnow;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CenturialCalendarAdapter extends CalendarAdapter
{
   private enum ListRowViewType
   {
      CENTURY, DECADE
   };

   public CenturialCalendarAdapter(Context context)
   {
      super( context );
   }

   public View getView( int position, View convertView, ViewGroup parent )
   {
      View view;

      CalendarData calendarData = getCalendarDataFromPosition( position );

      switch( calendarData.type )
      {
      case CENTURY:
         view = getCenturyView( position, convertView, parent, calendarData );
         break;
      case DECADE:
         view = getDecadeView( position, convertView, parent, calendarData );
         break;
      default:
         view = null;
         break;
      }

      return view;
   }

   private class CalendarData
   {
      final ListRowViewType type;
      final int             decade;
      final int             century;

      CalendarData(ListRowViewType type, int century, int decade)
      {
         this.type = type;
         this.century = century;
         this.decade = decade;
      }
   }

   private CalendarData getCalendarDataFromPosition( int position )
   {
      ListRowViewType type = ListRowViewType.DECADE;
      int century = 0;
      int decade = 0;

      final int NUM_OF_ROWS_IN_A_CENTURIAL_CALENDAR = 1 + NUM_OF_DECADES_IN_A_CENTURY;

      int index = position - MID_POSITION;

      int offset = index % NUM_OF_ROWS_IN_A_CENTURIAL_CALENDAR;
      if( offset < 0 )
      {
         offset += NUM_OF_ROWS_IN_A_CENTURIAL_CALENDAR;
      }

      if( offset == 0 )
      {
         type = ListRowViewType.CENTURY;
      }
      else
      {
         type = ListRowViewType.DECADE;
      }

      century = index / NUM_OF_ROWS_IN_A_CENTURIAL_CALENDAR;
      if( index < 0 )
      {
         int negativeIndex = index - NUM_OF_ROWS_IN_A_CENTURIAL_CALENDAR + 1;

         century = -1
               * ((Math.abs( negativeIndex ) / NUM_OF_ROWS_IN_A_CENTURIAL_CALENDAR));
      }

      int firstYearInCurrentCentury = CURRENT_CALENDAR.get( Calendar.YEAR )
            / NUM_OF_YEARS_IN_A_CENTURY * NUM_OF_YEARS_IN_A_CENTURY;
      int firstYearInCentury = firstYearInCurrentCentury
            + (century * NUM_OF_YEARS_IN_A_CENTURY);

      century = firstYearInCentury / 100 + 1;
      decade = firstYearInCentury + (offset - 1) * NUM_OF_DECADES_IN_A_CENTURY;

      return new CalendarData( type, century, decade );

   }

   private String convertIntegerToOrdinal( int number )
   {
      String suffix;

      final String[] SUFFIX_LIST = new String[] { "th", "st", "nd", "rd", "th",
            "th", "th", "th", "th", "th" };

      int absNumber = Math.abs( number );
      switch( absNumber % 100 )
      {
      case 11:
      case 12:
      case 13:
         suffix = "th";
         break;
      default:
         suffix = SUFFIX_LIST[ absNumber % 10 ];
         break;
      }

      return Integer.toString( number ) + suffix;
   }

   public View getCenturyView(
         int position,
         View convertView,
         ViewGroup parent,
         CalendarData calendarData )
   {
      View view;

      // Creating or recyling.
      if( convertView != null && convertView.getId() == R.id.centuryRow )
      {
         view = convertView;
      }
      else
      {
         view = mInflater.inflate(
               R.layout.centurial_calendar_row_century,
               parent,
               false );
      }

      int century = calendarData.century;
      int centuryInYears = (century - 1) * NUM_OF_YEARS_IN_A_CENTURY;
      int fontColor = (CURRENT_CALENDAR.get( Calendar.YEAR ) - centuryInYears >= NUM_OF_YEARS_IN_A_CENTURY)
            ? mResources.getColor( R.color.inactive )
            : mResources.getColor( R.color.active );

      TextView centuryView = (TextView) view;
      centuryView.setTextColor( fontColor );
      centuryView.setText( convertIntegerToOrdinal( calendarData.century )
            + " Century" );
      centuryView.setTypeface( mThinTypeface );

      return view;
   }

   public View getDecadeView(
         int position,
         View convertView,
         ViewGroup parent,
         CalendarData calendarData )
   {
      View view;

      // Creating or recyling.
      if( convertView != null && convertView.getId() == R.id.decadeRow )
      {
         view = convertView;
      }
      else
      {
         view = mInflater.inflate(
               R.layout.centurial_calendar_row_decade,
               parent,
               false );
      }

      // Caching
      if( view.getTag() == null )
      {
         ArrayList<TextView> yearViewList = new ArrayList<TextView>();
         ArrayList<TextView> ageViewList = new ArrayList<TextView>();

         ViewGroup yearRow1 = (ViewGroup) view.findViewById( R.id.yearRow1 );
         ViewGroup yearRow2 = (ViewGroup) view.findViewById( R.id.yearRow2 );

         for( int i = 0; i < 5; i++ )
         {
            ViewGroup yearAgeContainer = (ViewGroup) yearRow1.getChildAt( i );
            yearViewList.add( (TextView) yearAgeContainer.findViewById( R.id.year ) );
            ageViewList.add( (TextView) yearAgeContainer.findViewById( R.id.age ) );
         }

         for( int i = 0; i < 5; i++ )
         {
            ViewGroup yearAgeContainer = (ViewGroup) yearRow2.getChildAt( i );
            yearViewList.add( (TextView) yearAgeContainer.findViewById( R.id.year ) );
            ageViewList.add( (TextView) yearAgeContainer.findViewById( R.id.age ) );
         }

         HashMap<String, ArrayList<TextView>> cachedViews = new HashMap<String, ArrayList<TextView>>();

         cachedViews.put( "year", yearViewList );
         cachedViews.put( "age", ageViewList );

         view.setTag( cachedViews );
      }

      HashMap<String, ArrayList<TextView>> cachedViews = (HashMap<String, ArrayList<TextView>>) view.getTag();
      int currentYear = CURRENT_CALENDAR.get( Calendar.YEAR );
      int decade = calendarData.decade;

      // Year

      ArrayList<TextView> yearViewList = cachedViews.get( "year" );
      for( int i = 0; i < yearViewList.size(); i++ )
      {
         int year = decade + i;

         TextView yearView = yearViewList.get( i );
         yearView.setText( Integer.toString( year ) );

         boolean isPast = (year < currentYear);

         yearView.setTypeface( isPast ? mThinTypeface : mLightTypeface );

         yearView.setTextColor( isPast ? mResources.getColor( R.color.inactive )
               : mResources.getColor( R.color.active ) );
      }

      // Age

      ArrayList<TextView> ageViewList = cachedViews.get( "age" );
      for( int i = 0; i < ageViewList.size(); i++ )
      {
         int year = decade + i;
         String age = getAge( year );

         boolean isPast = (year < currentYear);

         // Age
         TextView ageView = ageViewList.get( i );
         ageView.setText( age );
         ageView.setTypeface( isPast ? mThinTypeface : mLightTypeface );

         // Container
         View ageContainerView = (View) ageView.getParent();
         ageContainerView.setVisibility( (age == "") ? View.INVISIBLE
               : View.VISIBLE );

         // Star
         ImageView starView = (ImageView) ageContainerView.findViewById( R.id.ageStar );
         starView.setAlpha( isPast ? 0.5f : 1.0f );
      }

      return view;
   }
}
