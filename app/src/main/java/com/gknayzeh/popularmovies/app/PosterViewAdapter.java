package com.gknayzeh.popularmovies.app;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.gknayzeh.popularmovies.app.data.MovieContract.MovieEntry;
import com.squareup.picasso.Picasso;

/**
 * Created by gknayzeh on 30/08/15.
 */
public class PosterViewAdapter extends CursorAdapter {

    private String LOG_TAG = PosterViewAdapter.class.getSimpleName();

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
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
        ImageView imageView;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageView = (ImageView) mInflater.inflate(R.layout.list_item_movie, null);

        // if it's not recycled, initialize some attributes
//            imageView = new ImageView(mContext);
//            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//            imageView.setPadding(0, 0, 0, 0);


//        if (MainActivity.mMovieDatas != null && !MainActivity.mMovieDatas.get(cursor.getPosition()).getPoster().equalsIgnoreCase("null")) {
//            Uri uri = new Uri.Builder()
//                    .scheme("http")
//                    .authority("image.tmdb.org")
//                    .appendPath("t")
//                    .appendPath("p")
//                    .appendPath("w185")
//                    .appendPath(MainActivity.mMovieDatas.get(cursor.getPosition()).getPoster()).build();
//
//            Log.i(LOG_TAG, "Get View Called: " + uri.toString());
//
//            Picasso.with(context).load(uri.toString()).into(imageView);
//        } else {
//            imageView.setImageResource(R.drawable.blank);
//        }
        return imageView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ImageView imageView = (ImageView)view;
        String posterPath = cursor.getString(cursor.getColumnIndex(MovieEntry.COL_POSTER_PATH));
        if (!posterPath.equalsIgnoreCase("null")) {
            Uri uri = new Uri.Builder()
                    .scheme("http")
                    .authority("image.tmdb.org")
                    .appendPath("t")
                    .appendPath("p")
                    .appendPath("w185")
                    .appendPath(posterPath).build();

            Log.i(LOG_TAG, "Get View Called: " + uri.toString());

            Picasso.with(context).load(uri.toString()).into(imageView);
        } else {
            imageView.setImageResource(R.drawable.blank);
        }

    }
}
