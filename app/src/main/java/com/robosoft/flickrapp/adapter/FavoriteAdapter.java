package com.robosoft.flickrapp.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.robosoft.flickrapp.FavouriteItemClickedListner;
import com.robosoft.flickrapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahul on 31/5/16.
 */
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavouriteViewHolder> {

    private List<Bitmap> mFavImages = new ArrayList<Bitmap>();
    private FavouriteItemClickedListner mFavItemClicked;

    public FavoriteAdapter(List<Bitmap> favBitmapList, FavouriteItemClickedListner favItemClicked) {
        mFavImages = favBitmapList;
        mFavItemClicked = favItemClicked;
    }

    @Override
    public FavouriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_single_row, parent , false);
        return new FavouriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavouriteViewHolder holder, int position) {
        holder.mFavImageView.setImageBitmap(mFavImages.get(position));
    }

    @Override
    public int getItemCount() {
        return mFavImages.size();
    }

    public class FavouriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mFavImageView;

        public FavouriteViewHolder(View itemView) {
            super(itemView);
            mFavImageView = (ImageView)itemView.findViewById(R.id.image);
            mFavImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mFavItemClicked.onFavItemClicked(mFavImages.get(getAdapterPosition()));
        }
    }
}
