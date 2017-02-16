package com.robosoft.flickrapp.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.robosoft.flickrapp.DownloadImageItemClickedListner;
import com.robosoft.flickrapp.FavouriteItemClickedListner;
import com.robosoft.flickrapp.HomeItemClickedListner;
import com.robosoft.flickrapp.R;
import com.robosoft.flickrapp.helper.FragmentHelper;
import com.robosoft.flickrapp.utills.Constants;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,HomeItemClickedListner,DownloadImageItemClickedListner,FavouriteItemClickedListner,Constants.BundleKey {

    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HomeFragment homeFragment = new HomeFragment();
        FragmentHelper.replaceFragment(R.id.main_frame, homeFragment, getSupportFragmentManager());

        mDrawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getResources().getString(R.string.toolbar_name));

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        if(navigationView != null)
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody") // disables certain compiler warnings
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()){
            case R.id.nav_home :
                HomeFragment homeFragment = new HomeFragment();
                FragmentHelper.replaceFragment(R.id.main_frame, homeFragment, getSupportFragmentManager());
                mToolbar.setTitle(getResources().getString(R.string.home_toolbar));
                break;

            case R.id.nav_favourite:
                FavoriteFragment favoriteFragment = new FavoriteFragment();
                FragmentHelper.replaceFragment(R.id.main_frame, favoriteFragment, getSupportFragmentManager());
                mToolbar.setTitle(getResources().getString(R.string.fav_toolbar));
                break;

            case R.id.nav_downloaded_image:
                DownloadedImageFragment downloadedImageFragment = new DownloadedImageFragment();
                FragmentHelper.replaceFragment(R.id.main_frame, downloadedImageFragment, getSupportFragmentManager());
                mToolbar.setTitle(getResources().getString(R.string.download_image_toolbar));
                break;
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onHomeItemClicked(String url) {
        sendDetail(url, BUNDLE_REFERENCE_VALUE_HOME);
    }

    @Override
    public void onFavItemClicked(Bitmap bitmap) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_REFERENCE, BUNDLE_REFERENCE_VALUE_FAV);
        bundle.putParcelable(BUNDLE_KEY_IMAGE, bitmap);
        openImageDetailFragment(bundle);
    }

    @Override
    public void onDownloadImageClicked(String path) {
        sendDetail(path, BUNDLE_REFERENCE_VALUE_DOWNLOAD);
    }

    private void openImageDetailFragment(Bundle bundle){
        ImageDetailFragment imageDetailFragment = new ImageDetailFragment();
        imageDetailFragment.setArguments(bundle);
        FragmentHelper.replaceFragment(R.id.main_frame, imageDetailFragment, getSupportFragmentManager());
    }

    private void sendDetail(String url, String reference) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_REFERENCE , reference);
        bundle.putString(BUNDLE_KEY_IMAGE, url);
        openImageDetailFragment(bundle);
    }

    // Handle the permissions request response for marshmallow
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
