package apps.com.pratilipi.Utilities;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import apps.com.pratilipi.HTMLRenderedFragment;
import apps.com.pratilipi.PreviewFragment;
import apps.com.pratilipi.SerializedFragment;


/**
 * Created by mkallingal on 6/12/2016.
 */
public class RendererPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 1;
    final String content;
    private String tabTitles[] = new String[] { "Rendered", "Serialized", "HTML" };
    private Context context;
    public RendererPagerAdapter(FragmentManager fm, Context context,String content) {
        super(fm);
        this.context = context;
        this.content= content;
    }

    @Override
    public Fragment getItem(int position) {

            return PreviewFragment.newInstance(content);

    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

}
