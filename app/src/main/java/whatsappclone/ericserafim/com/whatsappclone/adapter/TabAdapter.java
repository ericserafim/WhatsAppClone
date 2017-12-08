package whatsappclone.ericserafim.com.whatsappclone.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import whatsappclone.ericserafim.com.whatsappclone.fragment.ContatosFragment;
import whatsappclone.ericserafim.com.whatsappclone.fragment.ConversasFragment;

public class TabAdapter extends FragmentStatePagerAdapter {

    private String[] tituloTabs = {"CONVERSAS", "CONTATOS"};

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new ConversasFragment();
                break;
            case 1:
                fragment = new ContatosFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return tituloTabs.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tituloTabs[position];
    }
}
