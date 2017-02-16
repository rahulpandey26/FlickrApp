package com.robosoft.flickrapp.ui;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.robosoft.flickrapp.FavouriteItemClickedListner;
import com.robosoft.flickrapp.R;
import com.robosoft.flickrapp.adapter.FavoriteAdapter;
import com.robosoft.flickrapp.dto.DbModel;
import com.robosoft.flickrapp.helper.DbHelper;
import com.robosoft.flickrapp.utills.Utility;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahul on 26/5/16.
 */
public class FavoriteFragment extends Fragment {

    private List<Bitmap> mFavBitmapList = new ArrayList<Bitmap>();
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private FavoriteAdapter mFavoriteAdapter;
    private FavouriteItemClickedListner mFavItemClicked;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.fav_images_recycler_view);
        mProgressBar = (ProgressBar)view.findViewById(R.id.fav_progress_bar);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DbHelper dbHelper = new DbHelper(getContext());
        Cursor cursor = dbHelper.getData();
        List<DbModel> urlList = new ArrayList<DbModel>();

        int pos = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){

            DbModel db = new DbModel();
            db.setmImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(dbHelper.mData.COLUMN_URL)));
            urlList.add(pos, db);
            pos++;
            cursor.moveToNext();
        }

        if(Utility.isNetworkAvailable(getContext())) {
            for (int postion = 0 ; postion < urlList.size(); postion++) {
                DbModel dbModel = urlList.get(postion);
                DownloadFavImage downloadFavImage = new DownloadFavImage();
                downloadFavImage.execute(dbModel.getmImageUrl());
            }
            setAdapter();
        }
        else {
            Utility.showNetworkErrorMessage(getContext());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mFavItemClicked = (FavouriteItemClickedListner) context;
        } catch (ClassCastException exception) {
            exception.printStackTrace();
        }
    }

    private void setAdapter() {
        mFavoriteAdapter = new FavoriteAdapter(mFavBitmapList , mFavItemClicked);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        mRecyclerView.setAdapter(mFavoriteAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFavItemClicked = null;
    }

    private class DownloadFavImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];
            Bitmap bitmap = null;
            try {
                InputStream input = new java.net.URL(imageURL).openStream();
                bitmap = BitmapFactory.decodeStream(input);
                mFavBitmapList.add(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            mFavoriteAdapter.notifyDataSetChanged();
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
