package chao.android.tenyearsfromnow;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import chao.android.tenyearsfromnow.GoalDataSource.Goal;

public class NewGoalActivity extends Activity implements
      DatePickerDialog.OnDateSetListener
{
   private Typeface       mThinTypeface;
   private Typeface       mLightTypeface;

   private GoalDataSource mGoalDataSource;

   @Override
   public void onCreate( Bundle savedInstanceState )
   {
      super.onCreate( savedInstanceState );

      mGoalDataSource = new GoalDataSource( this );

      mThinTypeface = Typeface.createFromAsset(
            getAssets(),
            "fonts/Roboto-Thin.ttf" );

      mLightTypeface = Typeface.createFromAsset(
            getAssets(),
            "fonts/Roboto-Light.ttf" );

      setContentView( R.layout.activity_new_goal );

      setTypeface();
   }

   @Override
   public void onPause()
   {
      if( mGoalDataSource != null )
      {
         mGoalDataSource.close();
      }

      super.onPause();
   }

   @Override
   public void onResume()
   {
      if( mGoalDataSource != null )
      {
         mGoalDataSource.open();
      }

      super.onResume();
   }

   @Override
   public boolean onCreateOptionsMenu( Menu menu )
   {
      getMenuInflater().inflate( R.menu.activity_new_goal, menu );
      return true;
   }

   @Override
   public boolean onOptionsItemSelected( MenuItem item )
   {
      boolean result = true;

      switch( item.getItemId() )
      {
      case R.id.menu_save_new_item:
         saveNewGoal();
         break;
      case R.id.menu_cancel_new_item:
         finish();
         break;
      default:
         result = super.onOptionsItemSelected( item );
      }

      return result;
   }

   private void setTypeface()
   {
      ((TextView) findViewById( R.id.completionDatePickerButtonLabel )).setTypeface( mThinTypeface );
      ((TextView) findViewById( R.id.goalLabel )).setTypeface( mThinTypeface );

      ((TextView) findViewById( R.id.completionDatePickerButton )).setTypeface( mLightTypeface );
      ((TextView) findViewById( R.id.goal )).setTypeface( mLightTypeface );
   }

   public void showDatePickerDialog( View view )
   {
      DialogFragment newFragment = new DatePickerFragment( this );
      newFragment.show( getFragmentManager(), "datePicker" );
   }

   public void onDateSet( DatePicker view, int year, int month, int dayOfMonth )
   {
      Calendar date = Calendar.getInstance();
      date.set( year, month, dayOfMonth, 0, 0, 0 );

      TextView datePickerButton = (TextView) findViewById( R.id.completionDatePickerButton );
      datePickerButton.setText( Utility.GOAL_DATE_FORMAT.format( date.getTime() ) );
      datePickerButton.setTag( date );
   }

   private void saveNewGoal()
   {
      TextView goalView = (TextView) findViewById( R.id.goal );
      TextView dateView = (TextView) findViewById( R.id.completionDatePickerButton );

      String goal = goalView.getText().toString();
      Calendar date = (Calendar) dateView.getTag();

      if( goal.trim().isEmpty() )
      {
         Toast.makeText(
               this,
               R.string.empty_goal_summary_error,
               Toast.LENGTH_LONG ).show();
      }
      else if( date == null )
      {
         Toast.makeText(
               this,
               R.string.empty_goal_completion_date_error,
               Toast.LENGTH_LONG ).show();
      }
      else
      {
         Goal insertedGoal = mGoalDataSource.insertGoal(
               goal,
               date.getTimeInMillis() );

         Toast.makeText(
               this,
               getString( R.string.goal_created_msg )
                     + Utility.shortenString( goal ),
               Toast.LENGTH_LONG ).show();

         finish();
      }
   }
}
