package chao.android.tenyearsfromnow.CalendarAdapter;

import java.util.ArrayList;
import java.util.Calendar;

import chao.android.tenyearsfromnow.R;
import chao.android.tenyearsfromnow.R.color;
import chao.android.tenyearsfromnow.R.id;
import chao.android.tenyearsfromnow.R.layout;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class DecadalCalendarAdapter extends CalendarAdapter
{
   private enum ListRowViewType
   {
      DECADE, YEAR
   };

   public DecadalCalendarAdapter(Context context)
   {
      super( context );
   }

   public View getView( int position, View convertView, ViewGroup parent )
   {
      View view;

      CalendarData calendarData = getCalendarDataFromPosition( position );

      switch( calendarData.type )
      {
      case DECADE:
         view = getDecadeView( position, convertView, parent, calendarData );
         break;
      case YEAR:
         view = getYearView( position, convertView, parent, calendarData );
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
      final int             year;

      CalendarData(ListRowViewType type, int decade, int year)
      {
         this.type = type;
         this.decade = decade;
         this.year = year;
      }
   }

   public int getPositionForCurrentTime()
   {
      int position = MID_POSITION;

      // int currentYear = CURRENT_CALENDAR.get( Calendar.YEAR );
      //
      // while( true )
      // {
      // CalendarData calendarData = getCalendarDataFromPosition( position );
      //
      // if( calendarData.type == ListRowViewType.YEAR
      // || calendarData.year == currentYear )
      // {
      // break;
      // }
      // else
      // {
      // ++position;
      // }
      // }

      return position;
   }

   private CalendarData getCalendarDataFromPosition( int position )
   {
      ListRowViewType type = ListRowViewType.YEAR;
      int decade = 0;
      int year = 0;

      final int NUM_OF_ROWS_IN_A_DECADAL_CALENDAR = 1 + NUM_OF_YEARS_IN_A_DECADE;

      int index = position - MID_POSITION;

      int offset = index % NUM_OF_ROWS_IN_A_DECADAL_CALENDAR;
      if( offset < 0 )
      {
         offset += NUM_OF_ROWS_IN_A_DECADAL_CALENDAR;
      }

      if( offset == 0 )
      {
         type = ListRowViewType.DECADE;
      }
      else
      {
         type = ListRowViewType.YEAR;
      }

      decade = index / NUM_OF_ROWS_IN_A_DECADAL_CALENDAR;
      if( index < 0 )
      {
         int negativeIndex = index - NUM_OF_ROWS_IN_A_DECADAL_CALENDAR + 1;

         decade = -1
               * ((Math.abs( negativeIndex ) / NUM_OF_ROWS_IN_A_DECADAL_CALENDAR));
      }

      int currentDecade = CURRENT_CALENDAR.get( Calendar.YEAR )
            / NUM_OF_YEARS_IN_A_DECADE * NUM_OF_YEARS_IN_A_DECADE;
      decade = currentDecade + (decade * NUM_OF_YEARS_IN_A_DECADE);

      year = decade + (offset - 1);

      return new CalendarData( type, decade, year );

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
               R.layout.decadal_calendar_row_decade,
               parent,
               false );
      }

      int decade = calendarData.decade;
      int fontColor = (CURRENT_CALENDAR.get( Calendar.YEAR ) - decade >= NUM_OF_YEARS_IN_A_DECADE)
            ? mResources.getColor( R.color.inactive )
            : mResources.getColor( R.color.active );

      TextView textView = (TextView) view;
      textView.setText( Integer.toString( decade ) + "s" );
      textView.setTextColor( fontColor );
      textView.setTypeface( mThinTypeface );

      return view;
   }

   public View getYearView(
         int position,
         View convertView,
         ViewGroup parent,
         CalendarData calendarData )
   {
      View view;

      // Creating or recyling.
      if( convertView != null && convertView.getId() == R.id.yearRow )
      {
         view = convertView;
      }
      else
      {
         view = mInflater.inflate(
               R.layout.decadal_calendar_row_year,
               parent,
               false );
      }

      // Caching.
      if( view.getTag() == null )
      {
         ArrayList<View> viewList = new ArrayList<View>();

         viewList.add( view.findViewById( R.id.year ) );
         viewList.add( view.findViewById( R.id.age ) );
         viewList.add( view.findViewById( R.id.ageStar ) );
         viewList.add( view.findViewById( R.id.ageContainer ) );

         ViewGroup monthRow1 = (ViewGroup) view.findViewById( R.id.monthRow1 );
         ViewGroup monthRow2 = (ViewGroup) view.findViewById( R.id.monthRow2 );
         for( int i = 0; i < 6; i++ )
         {
            viewList.add( monthRow1.getChildAt( i ) );
         }
         for( int i = 0; i < 6; i++ )
         {
            viewList.add( monthRow2.getChildAt( i ) );
         }

         view.setTag( viewList );
      }

      int currentYear = CURRENT_CALENDAR.get( Calendar.YEAR );
      int currentMonth = CURRENT_CALENDAR.get( Calendar.MONTH );

      ArrayList<View> viewList = (ArrayList<View>) view.getTag();

      // Year
      int year = calendarData.year;
      boolean isPastYear = year < currentYear;

      TextView yearView = (TextView) viewList.get( 0 );
      yearView.setText( Integer.toString( year ) );
      yearView.setTypeface( isPastYear ? mLightTypeface : mRegularTypeface );
      yearView.setTextColor( isPastYear
            ? mResources.getColor( R.color.inactive )
            : mResources.getColor( R.color.active ) );

      // Age
      TextView ageView = (TextView) viewList.get( 1 );
      String age = getAge( year );
      ageView.setText( age );
      ageView.setTypeface( isPastYear ? mThinTypeface : mLightTypeface );
      ageView.setTextColor( isPastYear ? mResources.getColor( R.color.inactive )
            : mResources.getColor( R.color.active ) );

      // Star
      ImageView starView = (ImageView) viewList.get( 2 );
      starView.setAlpha( isPastYear ? 0.5f : 1.0f );

      // Age & Star Container
      View ageContainerView = viewList.get( 3 );
      ageContainerView.setVisibility( (age == "") ? View.GONE : View.VISIBLE );

      // Months
      int numOfNonMonthViews = 4;
      for( int i = numOfNonMonthViews; i < viewList.size(); i++ )
      {
         TextView monthView = (TextView) viewList.get( i );
         int month = i - numOfNonMonthViews;
         boolean isPast = (isPastYear || (year == currentYear && month < currentMonth));

         monthView.setTextColor( isPast
               ? mResources.getColor( R.color.inactive )
               : mResources.getColor( R.color.active ) );

         monthView.setTypeface( isPast ? mThinTypeface : mLightTypeface );
      }

      return view;
   }

}
