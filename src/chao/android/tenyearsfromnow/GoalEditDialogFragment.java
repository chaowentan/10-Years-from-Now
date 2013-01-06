package chao.android.tenyearsfromnow;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import chao.android.tenyearsfromnow.GoalDataSource.Goal;

public class GoalEditDialogFragment extends DialogFragment implements
      OnClickListener, DatePickerDialog.OnDateSetListener
{
   public interface GoalEditDialogListener
   {
      public void onPositiveButtonClick( Goal goal );

      public void onNegativeButtonClick( GoalEditDialogFragment dialog );

      public void onNeutralButtonClick( Goal goal );
   }

   private final Fragment               mOriginFragment;
   private final GoalEditDialogListener mListener;
   private final Goal                   mGoal;

   GoalEditDialogFragment(Fragment originFragment, Goal goal)
   {
      mOriginFragment = originFragment;
      mListener = (GoalEditDialogListener) originFragment;
      mGoal = goal;
   }

   @Override
   public Dialog onCreateDialog( Bundle savedInstanceState )
   {
      AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
      builder.setTitle( "Edit Goal" );
      setContentView( builder );
      setButtons( builder );

      return builder.create();
   }

   @Override
   public void onDismiss( DialogInterface dialog )
   {
      super.onDismiss( dialog );

      mOriginFragment.onResume();
   }

   private void setContentView( AlertDialog.Builder builder )
   {
      ViewGroup contentView = (ViewGroup) getActivity().getLayoutInflater().inflate(
            R.layout.goal_edit_dialog,
            null );

      EditText goalTextBox = (EditText) contentView.findViewById( R.id.goal );
      goalTextBox.setText( mGoal.summary );
      goalTextBox.setSelection( mGoal.summary.length() ); // Move cursor to the
                                                          // end.

      EditText datePicker = (EditText) contentView.findViewById( R.id.completionDate );
      datePicker.setText( Utility.convertTimestampToDateString( mGoal.timestamp ) );
      datePicker.setOnClickListener( this );

      builder.setView( contentView );
   }

   private void setButtons( AlertDialog.Builder builder )
   {
      // ---------------------------------------------------------------------
      // Positive: Save

      builder.setPositiveButton( "Save", new DialogInterface.OnClickListener()
      {
         public void onClick( DialogInterface dialog, int which )
         {
            String newGoalSummary = ((EditText) getDialog().findViewById(
                  R.id.goal )).getText().toString();

            if( newGoalSummary.trim().isEmpty() )
            {
               Toast.makeText(
                     getActivity(),
                     R.string.empty_goal_summary_error,
                     Toast.LENGTH_LONG ).show();
            }
            else
            {
               mGoal.summary = newGoalSummary;
               mListener.onPositiveButtonClick( mGoal );
            }
         }
      } );

      // ---------------------------------------------------------------------
      // Negative: Cancel

      builder.setNegativeButton(
            "Cancel",
            new DialogInterface.OnClickListener()
            {
               public void onClick( DialogInterface dialog, int id )
               {
                  mListener.onNegativeButtonClick( GoalEditDialogFragment.this );
               }
            } );

      // ---------------------------------------------------------------------
      // Neutral: Delete

      builder.setNeutralButton( "Delete", new DialogInterface.OnClickListener()
      {
         public void onClick( DialogInterface dialog, int id )
         {
            mListener.onNeutralButtonClick( mGoal );
         }
      } );
   }

   public void onClick( View v )
   {
      DialogFragment newFragment = new DatePickerFragment(
            this,
            Utility.convertTimestampToCalendar( mGoal.timestamp ) );

      newFragment.show( getFragmentManager(), "datePicker" );
   }

   public void onDateSet(
         DatePicker view,
         int year,
         int monthOfYear,
         int dayOfMonth )
   {
      Calendar calendar = Calendar.getInstance();
      calendar.set( year, monthOfYear, dayOfMonth, 0, 0, 0 );

      TextView datePickerButton = (TextView) getDialog().findViewById(
            R.id.completionDate );
      datePickerButton.setText( Utility.GOAL_DATE_FORMAT.format( calendar.getTime() ) );

      mGoal.timestamp = calendar.getTimeInMillis();
   }
}
