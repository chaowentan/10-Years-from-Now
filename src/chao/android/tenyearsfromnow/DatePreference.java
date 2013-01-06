package chao.android.tenyearsfromnow;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;

public class DatePreference extends DialogPreference
{
   private final Context      mContext;
   private final AttributeSet mAttrs;
   private DatePicker         mDatePicker;
   private long               mTimestampShown = Calendar.getInstance().getTimeInMillis();

   public DatePreference(Context context, AttributeSet attrs)
   {
      super( context, attrs );

      mContext = context;
      mAttrs = attrs;
   }

   @Override
   protected View onCreateDialogView()
   {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime( new Date( mTimestampShown ) );

      mDatePicker = new DatePicker( mContext, mAttrs );
      mDatePicker.setCalendarViewShown( false );
      mDatePicker.init(
            calendar.get( Calendar.YEAR ),
            calendar.get( Calendar.MONTH ),
            calendar.get( Calendar.DAY_OF_MONTH ),
            null );

      return mDatePicker;
   }

   @Override
   protected void onDialogClosed( boolean positiveResult )
   {
      super.onDialogClosed( positiveResult );

      if( positiveResult )
      {
         Calendar calendar = Calendar.getInstance();
         calendar.set(
               mDatePicker.getYear(),
               mDatePicker.getMonth(),
               mDatePicker.getDayOfMonth(),
               0,
               0,
               0 );

         long timestamp = calendar.getTimeInMillis();
         persistLong( timestamp );
         mTimestampShown = timestamp;
      }
   }

   @Override
   protected void onSetInitialValue(
         boolean restorePersistedValue,
         Object defaultValue )
   {
      mTimestampShown = restorePersistedValue
            ? getPersistedLong( mTimestampShown ) : (Long) defaultValue;
   }

}
