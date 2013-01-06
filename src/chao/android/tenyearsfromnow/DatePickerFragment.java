package chao.android.tenyearsfromnow;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements
      DatePickerDialog.OnDateSetListener
{
   private final DatePickerDialog.OnDateSetListener mOnDateSetListener;
   private final Calendar                           mCalendar;

   DatePickerFragment(DatePickerDialog.OnDateSetListener listener)
   {
      mOnDateSetListener = listener;
      mCalendar = Calendar.getInstance();
   }

   DatePickerFragment(
         DatePickerDialog.OnDateSetListener listener,
         Calendar calendar)
   {
      mOnDateSetListener = listener;
      mCalendar = calendar;
   }

   @Override
   public Dialog onCreateDialog( Bundle savedInstanceState )
   {
      int year = mCalendar.get( Calendar.YEAR );
      int month = mCalendar.get( Calendar.MONTH );
      int day = mCalendar.get( Calendar.DAY_OF_MONTH );

      return new DatePickerDialog( getActivity(), this, year, month, day );
   }

   public void onDateSet(
         DatePicker view,
         int year,
         int monthOfYear,
         int dayOfMonth )
   {
      mOnDateSetListener.onDateSet( view, year, monthOfYear, dayOfMonth );
   }
}