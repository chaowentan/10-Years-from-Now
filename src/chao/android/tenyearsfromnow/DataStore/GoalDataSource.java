package chao.android.tenyearsfromnow.DataStore;

import java.util.ArrayList;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GoalDataSource
{
   private final GoalSQLiteOpenHelper mDBHelper;
   private SQLiteDatabase             mDatabase;

   public GoalDataSource(Context context)
   {
      mDBHelper = new GoalSQLiteOpenHelper( context );
   }

   public void open()
   {
      mDatabase = mDBHelper.getWritableDatabase();
   }

   public void close()
   {
      mDBHelper.close();
   }

   public Goal insertGoal( String summary, long timestamp )
   {
      Goal goal = null;

      if( mDatabase != null && mDatabase.isOpen() )
      {
         ContentValues values = new ContentValues();
         values.put( GoalSQLiteOpenHelper.GOAL_COLUMN_SUMMARY, summary );
         values.put( GoalSQLiteOpenHelper.GOAL_COLUMN_TIMESTAMP, timestamp );

         long id = mDatabase.insert(
               GoalSQLiteOpenHelper.GOAL_TABLE,
               null,
               values );

         goal = new Goal( id, summary, timestamp );
      }

      return goal;
   }

   public boolean deleteGoal( Goal goal )
   {
      boolean isDeleted = false;

      if( mDatabase != null && mDatabase.isOpen() )
      {
         String whereClause = GoalSQLiteOpenHelper.GOAL_COLUMN_ID + "=?";

         int numOfRowsDelete = mDatabase.delete(
               GoalSQLiteOpenHelper.GOAL_TABLE,
               whereClause,
               new String[] { Long.toString( goal.id ) } );

         isDeleted = (numOfRowsDelete > 0);
      }

      return isDeleted;
   }

   public boolean updateGoal( Goal goal )
   {
      boolean isUpdated = false;

      if( mDatabase != null && mDatabase.isOpen() )
      {
         ContentValues values = new ContentValues();
         values.put( GoalSQLiteOpenHelper.GOAL_COLUMN_SUMMARY, goal.summary );
         values.put( GoalSQLiteOpenHelper.GOAL_COLUMN_TIMESTAMP, goal.timestamp );

         String whereClause = GoalSQLiteOpenHelper.GOAL_COLUMN_ID + "=?";

         int numOfRowsUpdated = mDatabase.update(
               GoalSQLiteOpenHelper.GOAL_TABLE,
               values,
               whereClause,
               new String[] { Long.toString( goal.id ) } );

         isUpdated = (numOfRowsUpdated > 0);
      }

      return isUpdated;
   }

   public ArrayList<Goal> getAllGoals()
   {
      ArrayList<Goal> goals = new ArrayList<Goal>();

      String[] columns = new String[] { GoalSQLiteOpenHelper.GOAL_COLUMN_ID,
            GoalSQLiteOpenHelper.GOAL_COLUMN_SUMMARY,
            GoalSQLiteOpenHelper.GOAL_COLUMN_TIMESTAMP };

      Cursor cursor = mDatabase.query(
            GoalSQLiteOpenHelper.GOAL_TABLE,
            columns,
            null,
            null,
            null,
            null,
            "timestamp" ); // order by date

      if( cursor.moveToFirst() )
      {
         do
         {
            goals.add( getGoalFromCursor( cursor ) );
         } while( cursor.moveToNext() );
      }

      return goals;
   }

   private Goal getGoalFromCursor( Cursor cursor )
   {
      return new Goal(
            cursor.getLong( cursor.getColumnIndex( GoalSQLiteOpenHelper.GOAL_COLUMN_ID ) ),
            cursor.getString( cursor.getColumnIndex( GoalSQLiteOpenHelper.GOAL_COLUMN_SUMMARY ) ),
            cursor.getLong( cursor.getColumnIndex( GoalSQLiteOpenHelper.GOAL_COLUMN_TIMESTAMP ) ) );
   }

   // ////////////////////////////////////////////////////////////////////////

   public class Goal
   {
      public final long id;
      public String     summary;
      public long       timestamp;

      public Goal(long id, String summary, long timestamp)
      {
         this.id = id;
         this.summary = summary;
         this.timestamp = timestamp;
      }
   }

}
