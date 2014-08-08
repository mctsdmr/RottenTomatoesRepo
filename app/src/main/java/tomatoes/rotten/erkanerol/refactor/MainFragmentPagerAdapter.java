package tomatoes.rotten.erkanerol.refactor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

    int mainFragmentType;
    MovieListFragment[] fragmentList=new MovieListFragment[MyConstants.MAIN_PAGE_COUNT];

    public MainFragmentPagerAdapter(FragmentManager fm,int type) {
        super(fm);
        this.mainFragmentType=type;
    }

    @Override
    public Fragment getItem(int i) {
        if(fragmentList[i]!=null){
            return fragmentList[i];
        }
        else{
            MovieListFragment fragment=new MovieListFragment();
            Bundle args = new Bundle();
            if(mainFragmentType==MyConstants.MAIN_FRAGMENT_MOVIE){
                args.putInt(MyConstants.LIST_FRAGMENT_TYPE, i );
            }
            else{
                args.putInt(MyConstants.LIST_FRAGMENT_TYPE,MyConstants.MAIN_PAGE_COUNT+i);
            }
            fragment.setArguments(args);
            return fragment;
        }
    }

    @Override
    public int getCount() {
        return MyConstants.MAIN_PAGE_COUNT;
    }
}
