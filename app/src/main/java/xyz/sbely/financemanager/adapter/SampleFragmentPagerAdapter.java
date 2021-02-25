package xyz.sbely.financemanager.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import xyz.sbely.financemanager.R;
import xyz.sbely.financemanager.fragment.statistics.GraphicsPageFragment;
import xyz.sbely.financemanager.fragment.statistics.ReportPageFragment;

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[];

    List<Fragment> fragments = new ArrayList<>();

    public SampleFragmentPagerAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        tabTitles = new String[] {context.getString(R.string.vipiska), context.getString(R.string.graphics)};
        fragments.add(new ReportPageFragment());
        fragments.add(new GraphicsPageFragment());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
