package chao.android.tenyearsfromnow;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingActivity extends PreferenceActivity implements
      OnSharedPreferenceChangeListener
{
   public static final String KEY_USER_BIRTH_DATE = "user_birth_date";

   private DatePreference     mDatePreference;

   @Override
   public void onCreate( Bundle savedInstanceState )
   {
      super.onCreate( savedInstanceState );
      addPreferencesFromResource( R.xml.preferences );

      mDatePreference = (DatePreference) getPreferenceScreen().findPreference(
            KEY_USER_BIRTH_DATE );
   }

   @Override
   protected void onResume()
   {
      super.onResume();

      SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();

      setSummaryForBirthDatePreference( sharedPreferences.getLong(
            KEY_USER_BIRTH_DATE,
            0 ) );

      sharedPreferences.registerOnSharedPreferenceChangeListener( this );
   }

   @Override
   protected void onPause()
   {
      super.onPause();

      getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(
            this );
   }

   public void onSharedPreferenceChanged(
         SharedPreferences sharedPreferences,
         String key )
   {
      if( key.equals( KEY_USER_BIRTH_DATE ) )
      {
         setSummaryForBirthDatePreference( sharedPreferences.getLong(
               KEY_USER_BIRTH_DATE,
               0 ) );
      }
   }

   private void setSummaryForBirthDatePreference( long timestamp )
   {
      String summary = (timestamp == 0) ? "Not set."
            : Utility.convertTimestampToDateString( timestamp );

      mDatePreference.setSummary( summary );
   }
}
