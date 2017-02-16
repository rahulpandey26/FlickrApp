package com.robosoft.flickrapp.helper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by rahul on 26/5/16.
 */
public class FragmentHelper {

    public  static  void  replaceFragment(int containerId, Fragment fragment, FragmentManager fragmentManager ){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(containerId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public  static  void  addFragment(int containerId, Fragment fragment,FragmentManager fragmentManager ){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(containerId, fragment);
        transaction.commit();
    }

    public static void popBackStackImmediate(final FragmentActivity activity) {
        if (null != activity)
            activity.getSupportFragmentManager().popBackStackImmediate();
    }
}
