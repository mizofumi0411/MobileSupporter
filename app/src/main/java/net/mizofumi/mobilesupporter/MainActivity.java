package net.mizofumi.mobilesupporter;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.FindCallback;
import com.nifty.cloud.mb.core.NCMB;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBFile;
import com.nifty.cloud.mb.core.NCMBQuery;

import net.mizofumi.mobilesupporter.Tabs.AllFragment;
import net.mizofumi.mobilesupporter.Tabs.OldPokeWiFiFragment;
import net.mizofumi.mobilesupporter.Tabs.Type1Fragment;
import net.mizofumi.mobilesupporter.Tabs.Type2Fragment;
import net.mizofumi.mobilesupporter.Tabs.Type3Fragment;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Mbaasの初期化
        NCMB.initialize(this,MbaasAPIUtils.appKey,MbaasAPIUtils.clientKey);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


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
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_all_image_delete){
            NCMBQuery<NCMBFile> files = NCMBFile.getQuery();
            files.findInBackground(new FindCallback<NCMBFile>() {
                boolean error = false;
                @Override
                public void done(List<NCMBFile> list, NCMBException e) {
                    if (e == null){
                        for (NCMBFile file:list) {
                            file.deleteInBackground(new DoneCallback() {
                                @Override
                                public void done(NCMBException e) {
                                    if (e != null){
                                        error = true;
                                    }
                                }
                            });
                        }
                        if (!error){
                            Toast.makeText(MainActivity.this,"すべて削除しました",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MainActivity.this,"一部削除できませんでした",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
            switch (position){
                case 0:
                    return AllFragment.newInstance();
                case 1:
                    return Type1Fragment.newInstance("hoge","fuga");
                case 2:
                    return Type3Fragment.newInstance();
                case 3:
                    return OldPokeWiFiFragment.newInsance();
                case 4:
                    return Type2Fragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "全件";
                case 1:
                    return "タイプ1";
                case 2:
                    return "タイプ3";
                case 3:
                    return "旧PocketWi-Fi";
                case 4:
                    return "タイプ2";
            }
            return null;
        }
    }
}
