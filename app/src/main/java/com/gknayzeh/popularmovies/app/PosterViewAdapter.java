package com.gknayzeh.popularmovies.app;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by gknayzeh on 30/08/15.
 */
public class PosterViewAdapter extends CursorAdapter {

    private String LOG_TAG = PosterViewAdapter.class.getSimpleName();

    public PosterViewAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

//    @Override
//    public int getCount() {
//        if (MainActivity.mMovieDatas != null) {
//            return MainActivity.mMovieDatas.size();
//        } else {
//            return 0;
//        }
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return MainActivity.mMovieDatas.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_movie, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ImageView imageView = (ImageView) view;
        if (cursor != null) {
            String posterPath = cursor.getString(MoviesFragment.COL_POSTER_PATH);
            if (!posterPath.equalsIgnoreCase("null")) {
                Uri uri = new Uri.Builder()
                        .scheme("http")
                        .authority("image.tmdb.org")
                        .appendPath("t")
                        .appendPath("p")
                        .appendPath("w185")
                        .appendPath(posterPath).build();

                Log.i(LOG_TAG, "Get View Called: " + uri.toString() + "; Title:" + cursor.getString(MoviesFragment.COL_MOVIE_TITLE));

                Picasso.with(context).load(uri.toString()).into(imageView);
            } else {
                imageView.setImageResource(R.drawable.blank);
            }
        } else {
            imageView.setImageResource(R.drawable.blank);
        }

    }
}
