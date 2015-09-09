package com.gknayzeh.popularmovies.app.data;

import android.content.ContentValues;
import android.content.Context;

import com.gknayzeh.popularmovies.app.R;
import com.gknayzeh.popularmovies.app.data.MovieContract.MovieEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Created by gknayzeh on 30/08/15.
 */
public class MovieData implements Comparable<MovieData> {

    final String TMDB_TITLE = "original_title";
    final String TMDB_BACKDROP_PATH = "backdrop_path";
    final String TMDB_POSTER_PATH = "poster_path";
    final String TMDB_OVERVIEW = "overview";
    final String TMDB_VOTE_AVERAGE = "vote_average";
    final String TMDB_RELEASE_DATE = "release_date";

    private String title;
    private String poster;
    private String backdrop;
    private String overview;
    private String releaseDate;
    private double voteAverage;
    private String sortBy;
    private int order;

    public MovieData(String result, String sortBy, int order) throws JSONException {
        this(new JSONObject(result), sortBy, order);

    }

    public MovieData(Context context, String result) throws JSONException {
        this(new JSONObject(result), context.getString(R.string.pref_sort_popular), -1);
    }

    public MovieData(JSONObject movieJson, String sortBy, int order) throws JSONException {

        this.title = movieJson.getString(TMDB_TITLE);
        this.poster = movieJson.getString(TMDB_POSTER_PATH);
        if (!poster.equalsIgnoreCase("null")) {
            this.poster = this.poster.substring(1);
        }
        this.backdrop = movieJson.getString(TMDB_BACKDROP_PATH);
        if (!backdrop.equalsIgnoreCase("null")) {
            this.backdrop = this.backdrop.substring(1);
        }
        this.overview = movieJson.getString(TMDB_OVERVIEW);
        this.voteAverage = movieJson.getDouble(TMDB_VOTE_AVERAGE);
        this.releaseDate = movieJson.getString(TMDB_RELEASE_DATE);

        this.setSortBy(sortBy);
        this.setOrder(order);


    }

    public MovieData() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "MovieData{" +
                "title='" + title + "'" +
                ", poster='" + poster + "'" +
                ", backdrop='" + backdrop + "'" +
                ", overview='" + overview + "'" +
                ", releaseDate='" + releaseDate + "'" +
                ", voteAverage=" + voteAverage + "'" +
                ", sortBy=" + getSortBy() + "'" +
                ", order=" + getOrder() + "'" +
                '}';
    }

    public ContentValues getContentValues() {

        ContentValues movieValues = new ContentValues();

        movieValues.put(MovieEntry.COL_MOVIE_TITLE, getTitle());
        movieValues.put(MovieEntry.COL_POSTER_PATH, getPoster());
        movieValues.put(MovieEntry.COL_BACKDROP_PATH, getBackdrop());
        movieValues.put(MovieEntry.COL_OVERVIEW, getOverview());
        movieValues.put(MovieEntry.COL_RELEASE_DATE, getReleaseDate());
        movieValues.put(MovieEntry.COL_VOTE_AVERAGE, getVoteAverage());
        movieValues.put(MovieEntry.COL_SORT_BY, getSortBy());
        movieValues.put(MovieEntry.COL_SORT_ORDER, getOrder());

        return movieValues;

    }

    public static List<MovieData> convertToMovieData(Vector<ContentValues> cvv) {
        List<MovieData> movieDataList = new ArrayList<MovieData>();
        for (int i = 0; i < cvv.size(); i++) {
            ContentValues movieValues = cvv.elementAt(i);
            MovieData movieData = new MovieData();
            movieData.setTitle(movieValues.getAsString(MovieEntry.COL_MOVIE_TITLE));
            movieData.setPoster(movieValues.getAsString(MovieEntry.COL_POSTER_PATH));
            movieData.setBackdrop(movieValues.getAsString(MovieEntry.COL_BACKDROP_PATH));
            movieData.setOverview(movieValues.getAsString(MovieEntry.COL_OVERVIEW));
            movieData.setReleaseDate(movieValues.getAsString(MovieEntry.COL_RELEASE_DATE));
            movieData.setVoteAverage(movieValues.getAsDouble(MovieEntry.COL_VOTE_AVERAGE));
            movieData.setSortBy(movieValues.getAsString(MovieEntry.COL_SORT_BY));
            movieData.setOrder(movieValues.getAsInteger(MovieEntry.COL_SORT_ORDER));

            movieDataList.add(movieData);
        }

        Collections.sort(movieDataList);

        return movieDataList;
    }


    @Override
    public int compareTo(MovieData another) {
        String sortBy01 = getSortBy();
        String sortBy02 = another.getSortBy();

        if (sortBy01.equalsIgnoreCase(sortBy02)) {
            Integer order01 = Integer.valueOf(getOrder());
            Integer order02 = Integer.valueOf(another.getOrder());
            return order01.compareTo(order02);
        } else {
            return sortBy01.compareTo(sortBy02);
        }
    }
}
