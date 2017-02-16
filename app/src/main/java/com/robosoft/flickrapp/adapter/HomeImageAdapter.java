package com.robosoft.flickrapp.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.robosoft.flickrapp.HomeItemClickedListner;
import com.robosoft.flickrapp.R;
import com.robosoft.flickrapp.dto.Photo;
import com.robosoft.flickrapp.helper.ImagesCache;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahul on 2/6/16.
 */
public class HomeImageAdapter extends RecyclerView.Adapter<HomeImageAdapter.ImageViewHolder> {

    private List<String> mImageUrlList = new ArrayList<String>();
    private List<Photo> mImageInfo = new ArrayList<Photo>();
    private HomeItemClickedListner mHomeItemClickedListner;

    public HomeImageAdapter(List<Photo> imageInfo, HomeItemClickedListner homeItemClickedListner) {
        mImageInfo = imageInfo;
        mHomeItemClickedListner = homeItemClickedListner;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list_item, parent , false);
        return  new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {

        DownloadImage downloadImage = new DownloadImage(holder.mImageView ,holder.mProgressBar);
        mImageUrlList = mImageInfo.get(position).getmImageUrlList();
        downloadImage.execute(mImageUrlList.get(position),String.valueOf(position));
        holder.mImageTitle.setText(mImageInfo.get(position).getmTitle());
    }

    @Override
    public int getItemCount() {
        return mImageInfo.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageView;
        private TextView mImageTitle;
        private ProgressBar mProgressBar;

        public ImageViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView)itemView.findViewById(R.id.image);
            mImageTitle = (TextView)itemView.findViewById(R.id.image_title);
            mProgressBar = (ProgressBar)itemView.findViewById(R.id.image_progress_bar);
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   mHomeItemClickedListner.onHomeItemClicked(mImageUrlList.get(getAdapterPosition()));
                }
            });
        }
    }

    private class DownloadImage extends AsyncTask<String,Void,Bitmap> {

        private WeakReference<ImageView> mImageViewReference;
        private ProgressBar mProgressBar;

        public DownloadImage(ImageView imageView, ProgressBar progressBar) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            mImageViewReference = new WeakReference<ImageView>(imageView);
            mProgressBar = progressBar;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];
            int position = Integer.parseInt(URL[1]);

            Bitmap bitmap = null;
            try {
                InputStream input = new java.net.URL(imageURL).openStream();
                bitmap = BitmapFactory.decodeStream(input);

                ImagesCache.getInstance().addImageToMemoryCache(String.valueOf(position), bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            mProgressBar.setVisibility(View.GONE);
            if (mImageViewReference != null && result != null) {
                final ImageView imageView = mImageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(result);
                }
            }
        }
    }
}
