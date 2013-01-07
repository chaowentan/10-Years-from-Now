package chao.android.tenyearsfromnow.DataStore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GoalSQLiteOpenHelper extends SQLiteOpenHelper
{
   // Table and Column Names

   public static final String  GOAL_TABLE             = "goal";
   public static final String  GOAL_COLUMN_ID         = "id";
   public static final String  GOAL_COLUMN_SUMMARY    = "summary";
   public static final String  GOAL_COLUMN_TIMESTAMP  = "timestamp";

   // Internal Database-Related Info

   private static final int    DATABASE_VERSION       = 1;
   private static final String DATABASE_NAME          = "goal.sqlite3";

   private static final String CREATE_TABLE_STATEMENT = String.format(
                                                            "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s INTEGER);",
                                                            GOAL_TABLE,
                                                            GOAL_COLUMN_ID,
                                                            GOAL_COLUMN_SUMMARY,
                                                            GOAL_COLUMN_TIMESTAMP );

   GoalSQLiteOpenHelper(Context context)
   {
      super( context, DATABASE_NAME, null, DATABASE_VERSION );
   }

   @Override
   public void onCreate( SQLiteDatabase db )
   {
      db.execSQL( CREATE_TABLE_STATEMENT );
   }

   @Override
   public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion )
   {
      Log.w(
            GoalSQLiteOpenHelper.class.getName(),
            String.format(
                  "Upgrading database from version %d to %d, which will destroy all existing data.",
                  oldVersion,
                  newVersion ) );

      db.execSQL( "DROP TABLE IF EXISTS " + GOAL_TABLE );
      onCreate( db );
   }
}
