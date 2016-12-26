package rinseg.asistp.com.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rinseg.asistp.com.rinseg.R;
import rinseg.asistp.com.ui.activities.ActivityMain;

/**
 * Created by Carlos Ramos on 28/10/2016.
 */
public class FragmentTabRops extends Fragment {
    ///// TODO: ::::::::::::::::::::::::::::::::::::::::::::::::::::::: VARIABLES :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    ActivityMain activityMain ;

    ///// TODO: ::::::::::::::::::::::::::::::::::::::::::::::::::::::: EVENTOS :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_tabs_rops, container, false);

        setUpElements(view);
        return view;
    }

    @Override
    public void onResume(){
        activityMain.toolbar.setTitle(R.string.title_rops);
        super.onResume();
    }


///// TODO: ::::::::::::::::::::::::::::::::::::::::::::::::::::::: METODOS :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    //Proceso para cargar las vistas
    private void setUpElements(View v) {

        activityMain = ((ActivityMain) getActivity());

        TabLayout tabLayout =  (TabLayout) v.findViewById(R.id.tab_rops);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_pendientes)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_cerrados)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) v.findViewById(R.id.tab_view_pager_rops);
        PageAdapterRops adapter = new PageAdapterRops(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

}
