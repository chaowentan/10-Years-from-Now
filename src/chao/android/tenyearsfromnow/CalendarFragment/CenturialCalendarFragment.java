package chao.android.tenyearsfromnow.CalendarFragment;

import chao.android.tenyearsfromnow.R;
import chao.android.tenyearsfromnow.CalendarAdapter.CenturialCalendarAdapter;
import chao.android.tenyearsfromnow.R.layout;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CenturialCalendarFragment extends ListFragment
{
   CenturialCalendarAdapter mAdapter;

   // ////////////////////////////////////////////////////////////////////////

   @Override
   public void onCreate( Bundle savedInstanceState )
   {
      super.onCreate( savedInstanceState );

      mAdapter = new CenturialCalendarAdapter( getActivity() );

      setListAdapter( mAdapter );
   }

   // ////////////////////////////////////////////////////////////////////////

   @Override
   public View onCreateView(
         LayoutInflater inflater,
         ViewGroup container,
         Bundle savedInstanceState )
   {
      return inflater.inflate( R.layout.centurial_calendar, container, false );
   }

   // ////////////////////////////////////////////////////////////////////////

   @Override
   public void onStart()
   {
      super.onStart();

      getListView().setSelectionFromTop( mAdapter.MID_POSITION, 0 );
   }

   // ////////////////////////////////////////////////////////////////////////

   @Override
   public void onResume()
   {
      super.onResume();

      // The preference might have changed. Refresh the view.
      mAdapter.notifyDataSetChanged();
   }
}
