package chao.android.tenyearsfromnow.CalendarFragment;

import chao.android.tenyearsfromnow.R;
import chao.android.tenyearsfromnow.CalendarAdapter.YearlyCalendarAdapter;
import chao.android.tenyearsfromnow.R.layout;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class YearlyCalendarFragment extends ListFragment
{
   YearlyCalendarAdapter mAdapter;

   // ////////////////////////////////////////////////////////////////////////

   @Override
   public void onCreate( Bundle savedInstanceState )
   {
      super.onCreate( savedInstanceState );

      mAdapter = new YearlyCalendarAdapter( getActivity() );

      setListAdapter( mAdapter );
   }

   // ////////////////////////////////////////////////////////////////////////

   @Override
   public View onCreateView(
         LayoutInflater inflater,
         ViewGroup container,
         Bundle savedInstanceState )
   {
      return inflater.inflate( R.layout.yearly_calendar, container, false );
   }

   // ////////////////////////////////////////////////////////////////////////

   @Override
   public void onStart()
   {
      super.onStart();

      getListView().setSelectionFromTop(
            mAdapter.getPositionForCurrentTime(),
            0 );
   }

}
