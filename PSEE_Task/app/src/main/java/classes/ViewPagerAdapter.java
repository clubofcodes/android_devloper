package classes;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import fragments.AboutApp;
import fragments.AgeCalcFragment;
import fragments.DateA_SFragment;
import fragments.DateDurFragment;
import fragments.DateInfoFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.mNumOfTabs=behavior;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                AgeCalcFragment age = new AgeCalcFragment();
                return age;
            case 1:
                DateDurFragment duration = new DateDurFragment();
                return duration;
            case 2:
                DateA_SFragment addSub = new DateA_SFragment();
                return addSub;
            case 3:
                DateInfoFragment info = new DateInfoFragment();
                return info;
            case 4:
                AboutApp about = new AboutApp();
                return about;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
