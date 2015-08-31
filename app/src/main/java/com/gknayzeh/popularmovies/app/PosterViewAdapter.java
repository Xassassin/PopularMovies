package com.gknayzeh.popularmovies.app;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by gknayzeh on 30/08/15.
 */
public class PosterViewAdapter extends BaseAdapter {

    private String LOG_TAG = PosterViewAdapter.class.getSimpleName();

    private Context mContext;

    public PosterViewAdapter(Context c) {
        this.mContext = c;
    }

    @Override
    public int getCount() {
        if (MainActivity.mMovieDatas != null) {
            return MainActivity.mMovieDatas.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return MainActivity.mMovieDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            imageView = (ImageView) mInflater.inflate(R.layout.list_item_movie, null);

            // if it's not recycled, initialize some attributes
//            imageView = new ImageView(mContext);
//            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        if (MainActivity.mMovieDatas != null && !MainActivity.mMovieDatas.get(position).getPoster().equalsIgnoreCase("null")) {
            Uri uri = new Uri.Builder()
                    .scheme("http")
                    .authority("image.tmdb.org")
                    .appendPath("t")
                    .appendPath("p")
                    .appendPath("w185")
                    .appendPath(MainActivity.mMovieDatas.get(position).getPoster()).build();

            Log.i(LOG_TAG, "Get View Called: " + uri.toString());

            Picasso.with(mContext).load(uri.toString()).into(imageView);
        } else {
            imageView.setImageResource(R.drawable.blank);
        }
        return imageView;
    }
}
