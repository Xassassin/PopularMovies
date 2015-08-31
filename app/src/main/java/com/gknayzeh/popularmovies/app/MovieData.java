package com.gknayzeh.popularmovies.app;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gknayzeh on 30/08/15.
 */
public class MovieData {

    final String TMDB_REULTS = "results";
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

    public MovieData(String result) throws JSONException {
        this(new JSONObject(result));

    }

    public MovieData(JSONObject movieJson) throws JSONException {

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

    @Override
    public String toString() {
        return "MovieData{" +
                "title='" + title + '\'' +
                ", poster='" + poster + '\'' +
                ", backdrop='" + backdrop + '\'' +
                ", overview='" + overview + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", voteAverage=" + voteAverage +
                '}';
    }
}
