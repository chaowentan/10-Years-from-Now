package chao.android.tenyearsfromnow.CalendarAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import chao.android.tenyearsfromnow.MainActivity;
import chao.android.tenyearsfromnow.R;
import chao.android.tenyearsfromnow.DataStore.GoalDataSource;
import chao.android.tenyearsfromnow.DataStore.GoalDataSource.Goal;
import chao.android.tenyearsfromnow.R.id;
import chao.android.tenyearsfromnow.R.layout;

public class TimelineAdapter extends CalendarAdapter
{
   private ArrayList<Goal>      mGoals;
   private final GoalDataSource mGoalDataSource;

   public TimelineAdapter(Context context)
   {
      super( context );

      mGoalDataSource = ((MainActivity) context).mGoalDataSource;

      refreshData();
   }

   public void refreshData()
   {
      mGoals = mGoalDataSource.getAllGoals();
   }

   @Override
   public int getCount()
   {
      return mGoals.size();
   }

   @Override
   public boolean isEnabled( int position )
   {
      return true; // All items (i.e. goals) are clickable.
   }

   public Goal getGoal( int position )
   {
      Goal goal = null;

      if( 0 <= position && position < mGoals.size() )
      {
         goal = mGoals.get( position );
      }

      return goal;
   }

   public int getPositionForCurrentTime()
   {
      int position = 0;

      return position;
   }

   public View getView( int position, View convertView, ViewGroup parent )
   {
      ViewGroup view = null;
      Goal goal = getGoal( position );

      if( goal != null )
      {
         if( convertView != null )
         {
            view = (ViewGroup) convertView;
         }
         else
         {
            view = (ViewGroup) mInflater.inflate(
                  R.layout.timeline_item,
                  parent,
                  false );
         }

         // Hide the first divider
         if( position == 0 )
         {
            ImageView divider = (ImageView) view.findViewById( R.id.divider );
            divider.setVisibility( View.INVISIBLE );
         }

         // Date

         Date date = new Date( goal.timestamp );
         String year = (new SimpleDateFormat( "yyyy" )).format( date );
         String monthAndDay = (new SimpleDateFormat( "MMM dd" )).format( date );

         TextView dateView = (TextView) view.findViewById( R.id.date );
         dateView.setTypeface( mLightTypeface );
         dateView.setText( year + "\n" + monthAndDay );

         // Goal

         TextView goalView = (TextView) view.findViewById( R.id.goal );
         goalView.setTypeface( mLightTypeface );
         goalView.setText( goal.summary );
      }

      return view;
   }
}
