package com.robosoft.flickrapp.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.robosoft.flickrapp.DownloadImageItemClickedListner;
import com.robosoft.flickrapp.R;
import com.robosoft.flickrapp.adapter.DownloadedImageRecyclerAdapter;
import com.robosoft.flickrapp.utills.Constants;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by rahul on 26/5/16.
 */
public class DownloadedImageFragment extends Fragment implements Constants.SdFolder {

    private RecyclerView mRecyclerView;
    private ArrayList<String> mImageList = new ArrayList<String>();
    private DownloadImageItemClickedListner mDownloadedImageItemClicked;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_downloaded_images, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.download_images_recycler_view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mDownloadedImageItemClicked = (DownloadImageItemClickedListner)context;
        } catch (ClassCastException exception){
            exception.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String ExternalStorageDirectoryPath = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath();

        String targetPath = ExternalStorageDirectoryPath + DOWNLOADED_IMAGE_FOLDER;

        Toast.makeText(getContext().getApplicationContext(), targetPath, Toast.LENGTH_LONG).show();
        File targetDirectory = new File(targetPath);

        File[] files = targetDirectory.listFiles();

        if(targetDirectory.exists()) {
            for (File file : files) {
                mImageList.add(file.getAbsolutePath());
            }

            DownloadedImageRecyclerAdapter downloadedImageRecyclerAdapter = new DownloadedImageRecyclerAdapter(mImageList ,mDownloadedImageItemClicked);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            mRecyclerView.setAdapter(downloadedImageRecyclerAdapter);
        }
        else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setMessage(getResources().getString(R.string.no_downloaded_file_alert_message));
            alertDialogBuilder.setPositiveButton(getResources().getString(R.string.alert_positive_button_text), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {

                    arg0.cancel();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDownloadedImageItemClicked = null;
    }
}
