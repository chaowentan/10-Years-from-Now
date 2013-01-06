package chao.android.tenyearsfromnow;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

public class MainActivity extends Activity
{
   private static final String CURRENTLY_SELECTED_NAVIGATION_ITEM = "CURRENTLY_SELECTED_NAVIGATION_ITEM";

   private int                 mCurrentNavigationItemSelected     = 0;
   public GoalDataSource       mGoalDataSource;

   @Override
   public void onCreate( Bundle savedInstanceState )
   {
      super.onCreate( savedInstanceState );

      mGoalDataSource = new GoalDataSource( this );

      initializeActionBar();

      setContentView( R.layout.activity_main );

      processSavedInstanceState( savedInstanceState );
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
   protected void onSaveInstanceState( Bundle outState )
   {
      // TODO: Need to save the scroll position as well.
      outState.putInt(
            CURRENTLY_SELECTED_NAVIGATION_ITEM,
            mCurrentNavigationItemSelected );
   }

   private void processSavedInstanceState( Bundle savedInstanceState )
   {
      if( savedInstanceState != null
            && savedInstanceState.containsKey( CURRENTLY_SELECTED_NAVIGATION_ITEM ) )
      {
         getActionBar().setSelectedNavigationItem(
               savedInstanceState.getInt( CURRENTLY_SELECTED_NAVIGATION_ITEM ) );
      }
   }

   @Override
   public boolean onCreateOptionsMenu( Menu menu )
   {
      getMenuInflater().inflate( R.menu.activity_main, menu );
      return true;
   }

   @Override
   public boolean onOptionsItemSelected( MenuItem item )
   {
      boolean result = true;

      switch( item.getItemId() )
      {
      case R.id.menu_create_new_item:
         startActivity( new Intent( this, NewGoalActivity.class ) );
         break;
      case R.id.menu_settings:
         startActivity( new Intent( this, SettingActivity.class ) );
         break;
      default:
         result = super.onOptionsItemSelected( item );
      }

      return result;
   }

   private void initializeActionBar()
   {
      final int calendarViewListId = R.array.calendar_view_list;

      // Adapter

      SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(
            this,
            calendarViewListId,
            android.R.layout.simple_spinner_dropdown_item );

      // Listener

      ActionBar.OnNavigationListener navigationCallback = new ActionBar.OnNavigationListener()
      {
         public boolean onNavigationItemSelected( int itemPosition, long itemId )
         {
            mCurrentNavigationItemSelected = itemPosition;

            Resources res = getResources();
            String itemString = res.getStringArray( calendarViewListId )[ itemPosition ];

            if( itemString.equals( res.getString( R.string.calendar_view_year ) ) )
            {
               activateFragment( new YearlyCalendarFragment() );
            }
            else if( itemString.equals( res.getString( R.string.calendar_view_decade ) ) )
            {
               activateFragment( new DecadalCalendarFragment() );
            }
            else if( itemString.equals( res.getString( R.string.calendar_view_century ) ) )
            {
               activateFragment( new CenturialCalendarFragment() );
            }
            else if( itemString.equals( res.getString( R.string.calendar_view_timeline ) ) )
            {
               activateFragment( new TimelineFragment() );
            }

            return false;
         }
      };

      // Configuration

      ActionBar actionBar = getActionBar();
      actionBar.setNavigationMode( ActionBar.NAVIGATION_MODE_LIST );
      actionBar.setListNavigationCallbacks( spinnerAdapter, navigationCallback );
   }

   // TODO: Use horizontal swipe to switch between views.
   private void activateFragment( Fragment fragment )
   {
      FragmentTransaction transaction = getFragmentManager().beginTransaction();

      transaction.replace( R.id.calendar_view, fragment );
      transaction.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_FADE );
      // transaction.addToBackStack( null );
      transaction.commit();
   }
}
