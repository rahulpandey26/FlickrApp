package com.robosoft.flickrapp.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.robosoft.flickrapp.DownloadImageItemClickedListner;
import com.robosoft.flickrapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahul on 31/5/16.
 */
public class DownloadedImageRecyclerAdapter extends RecyclerView.Adapter<DownloadedImageRecyclerAdapter.DownloadImageViewHolder> {

    private List<String> mDownloadImages = new ArrayList<String>();
    private DownloadImageItemClickedListner mDownloadImageItemClickedListner;

    public DownloadedImageRecyclerAdapter(ArrayList<String> imageList,DownloadImageItemClickedListner downloadedImageItemClicked) {
        mDownloadImages = imageList;
        mDownloadImageItemClickedListner = downloadedImageItemClicked;
    }

    @Override
    public DownloadImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_single_row, parent,false);
        return new DownloadImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DownloadImageViewHolder holder, int position) {
        Bitmap bitmap = decodeBitmapFromUri(mDownloadImages.get(position), 220, 220);
        holder.mDownloadImageView.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return mDownloadImages.size();
    }

    public class DownloadImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView mDownloadImageView;

        public DownloadImageViewHolder(View itemView) {
            super(itemView);
            mDownloadImageView = (ImageView)itemView.findViewById(R.id.image);
            mDownloadImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDownloadImageItemClickedListner.onDownloadImageClicked(mDownloadImages.get(getAdapterPosition()));
                }
            });
        }
    }

    public Bitmap decodeBitmapFromUri(String path, int reqWidth, int reqHeight) {

        Bitmap bitmap = null;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(path, options);

        return bitmap;
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            } else {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }
        return inSampleSize;
    }
}
