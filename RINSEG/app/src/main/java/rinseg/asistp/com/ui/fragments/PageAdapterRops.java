package rinseg.asistp.com.ui.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Carlos Ramos on 28/10/2016.
 */
public class PageAdapterRops extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PageAdapterRops(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                FragmentROPsPendientes ropsPndt = new FragmentROPsPendientes();
                return ropsPndt;
            case 1:
                FragmentROPsCerrados ropsCrrd = new FragmentROPsCerrados();
                return ropsCrrd;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
