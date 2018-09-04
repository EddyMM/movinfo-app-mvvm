package com.solo.movinfo.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.solo.movinfo.R;

/**
 * Base activity that hosts a single fragment
 */

public abstract class SingleFragmentActivity extends BaseActivity {
    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_fragment_activity);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.single_fragment);

        if (fragment == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.single_fragment, createFragment())
                    .commit();
        }
    }
}
