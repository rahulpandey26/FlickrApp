package com.robosoft.flickrapp.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.robosoft.flickrapp.R;
import com.robosoft.flickrapp.dto.Photo;
import com.robosoft.flickrapp.helper.DbHelper;
import com.robosoft.flickrapp.utills.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Random;

/**
 * Created by rahul on 26/5/16.
 */
public class ImageDetailFragment extends Fragment implements View.OnClickListener,Constants.BundleKey,Constants.SdFolder,Constants.SwitchCaseValue,Constants.FbSharing {

    private ImageView mImage;
    private ImageButton mFavouriteBtn;
    private DbHelper mDbHelper;
    private String mImageUrl;
    private boolean mIsFavClicked, mIsDownloaded;
    private LinearLayout mOptionLayout;
    private Bitmap mDownloadedImage;
    private ProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_detail, container, false);
        setHasOptionsMenu(true);
        initialize(view);
        return view;
    }

    private void initialize(View view) {
        mImage = (ImageView)view.findViewById(R.id.image_detail);
        mProgressBar = (ProgressBar)view.findViewById(R.id.image_detail_progress);
        mFavouriteBtn = (ImageButton)view.findViewById(R.id.favorite_btn);
        ImageButton shareButton = (ImageButton)view.findViewById(R.id.share_btn);
        ImageButton downloadBtn = (ImageButton)view.findViewById(R.id.download_btn);
        mOptionLayout = (LinearLayout)view.findViewById(R.id.option_layout);
        mProgressBar.setVisibility(View.VISIBLE);
        mFavouriteBtn.setOnClickListener(this);
        shareButton.setOnClickListener(this);
        downloadBtn.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDbHelper = new DbHelper(getContext());
        getBundleData();

        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
               // getActivity().onBackPressed();
            }
        });
    }

    private void getBundleData() {
        Bundle bundle = this.getArguments();
        String reference =  bundle.getString(BUNDLE_KEY_REFERENCE);
        if(reference != null){
            switch (reference) {
                case HOME_IMAGE:
                    mImageUrl = bundle.getString(BUNDLE_KEY_IMAGE);
                    DownloadImageDetail downloadImageDetail = new DownloadImageDetail();
                    downloadImageDetail.execute(mImageUrl);
                    mOptionLayout.setVisibility(View.VISIBLE);
                    break;

                case FAVORITE_IMAGE:
                    Bitmap bitmap = bundle.getParcelable(BUNDLE_KEY_IMAGE);
                    mImage.setImageBitmap(bitmap);
                    mProgressBar.setVisibility(View.GONE);
                    mOptionLayout.setVisibility(View.GONE);
                    break;

                case DOWNLOADED_IMAGE:
                    String path = bundle.getString(BUNDLE_KEY_IMAGE);
                    if(path!= null){
                        File imgFile = new File(path);

                        if(imgFile.exists()) {
                            Bitmap imageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            mImage.setImageBitmap(imageBitmap);
                            mProgressBar.setVisibility(View.GONE);
                        }
                        mOptionLayout.setVisibility(View.INVISIBLE);
                    } else {
                        Toast.makeText(getContext() , getResources().getString(R.string.path_not_found_msg),Toast.LENGTH_SHORT).show();
                    }
                    break;

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.favorite_btn:
                if (mIsFavClicked) {
                    mFavouriteBtn.setImageResource(R.drawable.like);
                    mDbHelper.delete(mImageUrl);
                    mIsFavClicked = false;
                } else {
                    mFavouriteBtn.setImageResource(R.drawable.likefilled);
                    mDbHelper.insert(mImageUrl);
                    mIsFavClicked = true;
                }
                break;

            case R.id.download_btn:
                if(mIsDownloaded)
                    Snackbar.make(getView() , getResources().getString(R.string.snackbar_msg_complete) , Snackbar.LENGTH_LONG).show();
                 else {
                    saveImage(mDownloadedImage);
                    Snackbar.make(getView() , getResources().getString(R.string.snackbar_msg) , Snackbar.LENGTH_LONG).show();
                    mIsDownloaded = true;
                }
                break;

            case R.id.share_btn:
                Intent intent = new Intent(getActivity() , FacebookShareActivity.class);
                intent.putExtra(FB_SHARE_KEY , mImageUrl);
                startActivity(intent);
                break;
        }
    }

    private void saveImage(Bitmap bitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File imageDir = new File(root + DOWNLOADED_IMAGE_FOLDER);
        imageDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        Photo photo = new Photo();
        String fname = photo.getmTitle() + n + IMAGE_EXTENSION;
        File file = new File(imageDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class DownloadImageDetail extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];
            Bitmap bitmap = null;
            try {
                InputStream input = new java.net.URL(imageURL).openStream();
                bitmap = BitmapFactory.decodeStream(input);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            mImage.setImageBitmap(result);
            mDownloadedImage = result;
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      /*  if (requestCode == 100 && getActivity() != null) {
            FragmentHelper.popBackStackImmediate(getActivity());
        }*/
    }
}
