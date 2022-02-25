package com.developeralamin.firechat.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.developeralamin.firechat.R;
import com.developeralamin.firechat.fragment.ChatFragment;
import com.developeralamin.firechat.fragment.HomeFragment;
import com.developeralamin.firechat.fragment.UserFragment;

public class FragmentAdapter extends FragmentPagerAdapter {

    String [] name = {"HOME", "User", "Chat"};

    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
            return new HomeFragment();
            case 1:
                return  new UserFragment();
            case 2:
                return new ChatFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return name.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return name[position];
    }
}
