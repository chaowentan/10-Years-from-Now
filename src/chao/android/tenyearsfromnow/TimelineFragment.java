package chao.android.tenyearsfromnow;

import android.app.DialogFragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import chao.android.tenyearsfromnow.GoalDataSource.Goal;
import chao.android.tenyearsfromnow.GoalEditDialogFragment.GoalEditDialogListener;

public class TimelineFragment extends ListFragment implements
      GoalEditDialogListener
{
   TimelineAdapter mAdapter;

   @Override
   public void onCreate( Bundle savedInstanceState )
   {
      super.onCreate( savedInstanceState );

      mAdapter = new TimelineAdapter( getActivity() );

      setListAdapter( mAdapter );
   }

   @Override
   public View onCreateView(
         LayoutInflater inflater,
         ViewGroup container,
         Bundle savedInstanceState )
   {
      // TODO: Show age in timeline view.
      return inflater.inflate( R.layout.timeline, container, false );
   }

   @Override
   public void onStart()
   {
      super.onStart();

      getListView().setSelectionFromTop(
            mAdapter.getPositionForCurrentTime(),
            0 );
   }

   @Override
   public void onResume()
   {
      mAdapter.refreshData();
      mAdapter.notifyDataSetChanged();

      super.onResume();
   }

   @Override
   public void onListItemClick(
         ListView listView,
         View itemView,
         int position,
         long itemRowId )
   {
      DialogFragment dialog = new GoalEditDialogFragment(
            this,
            mAdapter.getGoal( position ) );

      dialog.show( getFragmentManager(), "editGoal" );
   }

   public void onPositiveButtonClick( Goal goal )
   {
      // Save modified goal.

      GoalDataSource dataSource = ((MainActivity) getActivity()).mGoalDataSource;

      boolean isUpdated = dataSource.updateGoal( goal );

      if( isUpdated )
      {
         Toast.makeText(
               getActivity(),
               getString( R.string.goal_modified_msg )
                     + Utility.shortenString( goal.summary ),
               Toast.LENGTH_LONG ).show();
      }
      else
      {
         Log.w( "TimelineFragment", "Unable to update goal." );
      }
   }

   public void onNegativeButtonClick( GoalEditDialogFragment dialog )
   {
      // Do nothing. Let the dialog dismiss.
   }

   public void onNeutralButtonClick( Goal goal )
   {
      // Delete goal.

      GoalDataSource dataSource = ((MainActivity) getActivity()).mGoalDataSource;

      boolean isDeleted = dataSource.deleteGoal( goal );

      if( isDeleted )
      {
         Toast.makeText(
               getActivity(),
               getString( R.string.goal_deleted_msg )
                     + Utility.shortenString( goal.summary ),
               Toast.LENGTH_LONG ).show();
      }
      else
      {
         Log.w( "TimelineFragment", "Unable to delete goal." );
      }
   }
}
