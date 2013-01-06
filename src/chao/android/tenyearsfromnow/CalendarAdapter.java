package chao.android.tenyearsfromnow;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.TextView;

public abstract class CalendarAdapter extends BaseAdapter
{
   // Static Variables

   static final int      MAX_POSITION                = Integer.MAX_VALUE;
   static final int      MID_POSITION                = MAX_POSITION / 2;
   static final Calendar CURRENT_CALENDAR            = Calendar.getInstance();
   static final int      NUM_OF_YEARS_IN_A_DECADE    = 10;
   static final int      NUM_OF_YEARS_IN_A_CENTURY   = 100;
   static final int      NUM_OF_DECADES_IN_A_CENTURY = 10;

   // Instance Variables

   final Context         mContext;
   final LayoutInflater  mInflater;
   final Resources       mResources;

   final Typeface        mThinTypeface;
   final Typeface        mLightTypeface;
   final Typeface        mRegularTypeface;
   final Typeface        mMediumTypeface;

   // /////////////////////////////////////////////////////////////////////////
   // Methods

   public CalendarAdapter(Context context)
   {
      mContext = context;
      mInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
      mResources = context.getResources();

      // Thin < Light < Regular < Medium < Bold < Black
      mThinTypeface = getTypeface( "fonts/Roboto-Thin.ttf" );
      mLightTypeface = getTypeface( "fonts/Roboto-Light.ttf" );
      mRegularTypeface = getTypeface( "fonts/Roboto-Regular.ttf" );
      mMediumTypeface = getTypeface( "fonts/Roboto-Medium.ttf" );
   }

   private Typeface getTypeface( String fileName )
   {
      return Typeface.createFromAsset( mContext.getAssets(), fileName );
   }

   protected void setInactiveTextStyle( TextView textView )
   {
      textView.setTextColor( mResources.getColor( R.color.inactive ) );
      textView.setTypeface( mLightTypeface );
   }

   protected void setActiveTextStyle( TextView textView )
   {
      textView.setTextColor( mResources.getColor( R.color.active ) );
      textView.setTypeface( mRegularTypeface );
   }

   protected String getAge( int year )
   {
      String age = "";

      long birthDateTimestamp = PreferenceManager.getDefaultSharedPreferences(
            mContext ).getLong( SettingActivity.KEY_USER_BIRTH_DATE, 0 );

      if( birthDateTimestamp > 0 )
      {
         Calendar birthDate = Calendar.getInstance();
         birthDate.setTime( new Date( birthDateTimestamp ) );
         int birthYear = birthDate.get( Calendar.YEAR );

         if( birthYear <= year )
         {
            age = Integer.toString( year - birthYear );
         }
      }

      return age;
   }

   // /////////////////////////////////////////////////////////////////////////
   // Override/Implement base class methods.

   public int getCount()
   {
      return MAX_POSITION;
   }

   public Object getItem( int position )
   {
      return null;
   }

   public long getItemId( int position )
   {
      return 0;
   }

   @Override
   public boolean isEnabled( int position )
   {
      return false; // By default, none of the list items are clickable.
   }
}
