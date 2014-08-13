package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import adapters.MainFragmentPagerAdapter;
import tomatoes.rotten.erkanerol.refactor.MyConstants;
import tomatoes.rotten.erkanerol.refactor.R;


public class MainFragment extends Fragment {

    public MainFragmentPagerAdapter pagerAdapter;
    public ViewPager mViewPager;

    public MainFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments=getArguments();
        if(arguments.getInt(MyConstants.MAIN_TYPE)==MyConstants.MAIN_FRAGMENT_MOVIE){
            pagerAdapter=new MainFragmentPagerAdapter(getChildFragmentManager(),MyConstants.MAIN_FRAGMENT_MOVIE);
        }
        else{
            pagerAdapter=new MainFragmentPagerAdapter(getChildFragmentManager(),MyConstants.MAIN_FRAGMENT_DVD);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewPager = (ViewPager) getActivity().findViewById(R.id.pager_middle);
        mViewPager.setAdapter(pagerAdapter);
    }
}
