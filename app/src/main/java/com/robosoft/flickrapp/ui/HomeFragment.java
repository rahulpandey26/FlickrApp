package com.robosoft.flickrapp.ui;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.robosoft.flickrapp.HomeItemClickedListner;
import com.robosoft.flickrapp.adapter.HomeImageAdapter;
import com.robosoft.flickrapp.helper.ImagesCache;
import com.robosoft.flickrapp.helper.JSONParser;
import com.robosoft.flickrapp.R;
import com.robosoft.flickrapp.dto.Photo;
import com.robosoft.flickrapp.utills.Constants;
import com.robosoft.flickrapp.utills.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahul on 27/5/16.
 */
public class HomeFragment extends Fragment implements Constants.ServerValues, Constants.ImageUrlValues, Constants.BundleKey {

    private List<String> mImageUrlList;
    private List<Photo> mImageInfo;
    private RecyclerView mRecyclerView;
    private HomeItemClickedListner mHomeItemClickedListner;
    private ProgressBar mHomeProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initialize(view);
        return view;
    }

    private void initialize(View view) {
        mRecyclerView = (RecyclerView)view.findViewById(R.id.images_recycler_view);
        mHomeProgressBar = (ProgressBar)view.findViewById(R.id.home_progress_bar);
        mHomeProgressBar.setVisibility(View.VISIBLE);
        mImageInfo = new ArrayList<Photo>();
        mImageUrlList = new ArrayList<String>();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // if interface is not implemented so handle class cast exception
        try {
            mHomeItemClickedListner = (HomeItemClickedListner) context;
        } catch (ClassCastException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //String url = "https://api.flickr.com/services/rest/?&method=flickr.people.getPublicPhotos&api_key=566dc1849e2b1a11b1c2b48c159c81f1&user_id=29096781@N02&format=json&per_page=10&nojsoncallback=1";
        String baseUrl = getResources().getString(R.string.base_url);

            if (Utility.isNetworkAvailable(getContext())) {
                DownloadData downloadData = new DownloadData();
                downloadData.execute(baseUrl);
            } else {
                Utility.showNetworkErrorMessage(getContext());
            }
    }

    private void setAdapter() {
        HomeImageAdapter homeImageAdapter = new HomeImageAdapter(mImageInfo,mHomeItemClickedListner);
        mHomeProgressBar.setVisibility(View.GONE);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setAdapter(homeImageAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mHomeItemClickedListner = null;
    }

    private class DownloadData extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            String url = args[0];
            JSONParser jsonParser = new JSONParser();
            return jsonParser.getJSONFromUrl(url);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            // For checking activity and json data is null or not
            if (getActivity() == null && json == null)
                return;
            try {
                setJSONDataAndUrlForming(json);
                setAdapter();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setJSONDataAndUrlForming(JSONObject json) throws JSONException {

        JSONObject photos = json.getJSONObject(JSON_OBJECT);
        JSONArray jsonArray = photos.getJSONArray(JSON_ARRAY);
        for (int pos = 0; pos < jsonArray.length(); pos++) {

            JSONObject photoObject = jsonArray.getJSONObject(pos);

            String id = photoObject.getString(ID);
            String photoName = photoObject.getString(TITLE);
            String secret = photoObject.getString(SECRET_KEY);
            String server = photoObject.getString(SERVER_KEY);
            String farm = photoObject.getString(FARM_KEY);

            String imageUrl = getResources().getString(R.string.image_url);
            String finalImageUrl = imageUrl.replace(FARM_ID, farm).replace(SERVER_ID, server).replace(ID_KEY, id).replace(SECERT, secret);
            mImageUrlList.add(finalImageUrl);

            Photo photo = new Photo();
            photo.setmId(id);
            photo.setmImageUrlList(mImageUrlList);
            photo.setmTitle(photoName);
            mImageInfo.add(photo);
        }
    }
}
