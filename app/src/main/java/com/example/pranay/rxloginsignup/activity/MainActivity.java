package com.example.pranay.rxloginsignup.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.pranay.rxloginsignup.R;
import com.example.pranay.rxloginsignup.fragment.LoginFragment;
import com.example.pranay.rxloginsignup.fragment.SignUpFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.viewpager_login_signup)
    ViewPager viewpager_login_signup;
    @BindView(R.id.tablayout_login_signup)
    TabLayout tablayout_login_signup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: goto github repository
            }
        });

        setViewPager();
    }

    /**
     * Set Viewpager with two fragments
     */
    private void setViewPager() {
        viewpager_login_signup.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),this));
        tablayout_login_signup.setupWithViewPager(viewpager_login_signup);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     *  Pager adapter to set fragment in view pager
     */
    public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private int PAGE_COUNT=2;
        private String tabTitles[] = new String[]{getString(R.string.str_login), getString(R.string.str_signup)};
        private Context mContext;

        public MyFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return LoginFragment.getInstant();
                case 1:
                    return SignUpFragment.getInstant();
                default:
                    return LoginFragment.getInstant();
            }
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }

}
