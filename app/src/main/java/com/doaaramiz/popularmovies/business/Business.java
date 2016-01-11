package com.doaaramiz.popularmovies.business;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.doaaramiz.popularmovies.api.APIConnection;
import com.doaaramiz.popularmovies.exception.BusinessException;
import com.doaaramiz.popularmovies.exception.NetworkException;
import com.doaaramiz.popularmovies.model.Movie;
import com.doaaramiz.popularmovies.model.Review;
import com.doaaramiz.popularmovies.model.SortType;
import com.doaaramiz.popularmovies.model.Trailer;
import com.doaaramiz.popularmovies.provider.MovieContract;
import com.doaaramiz.popularmovies.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Business class contains the application logic
 * Gets the data from the server, and saves the favorite movies in internal DB
 *
 * @author doaaramiz
 * @date 24.12.2015
 */
public class Business {

	private static Business business;
	private Context context;
	private APIConnection apiConnection;

	private Business(Context context) {
		this.context = context;
		apiConnection = APIConnection.getInstance(context);
	}

	public static Business getInstance(Context context) {
		if (business == null)
			business = new Business(context);
		return business;
	}

	/**
	 * Checks network availability
	 *
	 * @return network availability state
	 */
	private boolean isNetworkAvailable() {

		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

		return ((networkInfo != null) && (networkInfo.isAvailable()) && (networkInfo.isConnected()));
	}

	public List<Movie> getMoviesList(SortType sortType) throws NetworkException, BusinessException {

		List<Movie> movieList;

		if (isNetworkAvailable()) {
			movieList = apiConnection.getMoviesListFromAPI(sortType);
		} else {
			throw new NetworkException();
		}

		if (Utils.collectionIsEmpty(movieList))
			throw new BusinessException();

		return movieList;
	}

	public List<Trailer> getMovieTrailer(Long movieId) throws NetworkException, BusinessException {

		List<Trailer> trailerList;

		if (isNetworkAvailable()) {
			trailerList = apiConnection.getMovieTrailerFromAPI(movieId);
		} else {
			throw new NetworkException();
		}

		if (Utils.collectionIsEmpty(trailerList))
			throw new BusinessException();

		return trailerList;
	}

	public List<Review> getMovieReviews(Long movieId) throws NetworkException, BusinessException {

		List<Review> reviewList;

		if (isNetworkAvailable()) {
			reviewList = apiConnection.getMovieReviewsFromAPI(movieId);
		} else {
			throw new NetworkException();
		}

		if (Utils.collectionIsEmpty(reviewList))
			throw new BusinessException();

		return reviewList;
	}

	public void addMovieToFavorite(Movie movie) {

		boolean movieExists = checkMovieInFavorite(movie);

		if (!movieExists) {

			ContentValues contentValues = new ContentValues();

			contentValues.put(MovieContract.MovieEntry._ID, movie.getId());
			contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
			contentValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
			contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
			contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
			contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
			contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
			contentValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());

			context.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

		}
	}

	public void removeMovieFromFavorite(Movie movie) {

		boolean movieExists = checkMovieInFavorite(movie);

		if (movieExists) {

			context.getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
												MovieContract.MovieEntry._ID + " = ?",
												new String[]{String.valueOf(movie.getId())});
		}
	}

	public boolean checkMovieInFavorite(Movie movie) {

		long movieId = 0;

		Cursor cursor = context.getContentResolver().query(
				MovieContract.MovieEntry.CONTENT_URI,
				new String[]{MovieContract.MovieEntry._ID},
				MovieContract.MovieEntry._ID + " = ?",
				new String[]{String.valueOf(movie.getId())},
				null);

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				movieId = cursor.getLong(cursor.getColumnIndex(MovieContract.MovieEntry._ID));
			}
			cursor.close();
		}

		return movieId != 0;
	}

	public List<Movie> getFavoriteMovies() {

		List<Movie> movies = new ArrayList<>();

		Cursor cursor = context.getContentResolver().query(
				MovieContract.MovieEntry.CONTENT_URI,
				null,
				null,
				null,
				null);

		if (cursor != null) {
			while (cursor.moveToNext()) {

				long id = cursor.getLong(cursor.getColumnIndex(MovieContract.MovieEntry._ID));
				String title = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
				String originalTitle = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE));
				String overview = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW));
				String releaseDate = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE));
				String posterPath = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH));
				String voteAverage = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE));
				String backdropPath = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH));

				Movie movie = new Movie();
				movie.setId(id);
				movie.setTitle(title);
				movie.setOriginalTitle(originalTitle);
				movie.setOverview(overview);
				movie.setReleaseDate(releaseDate);
				movie.setPosterPath(posterPath);
				movie.setVoteAverage(voteAverage);
				movie.setBackdropPath(backdropPath);
				movies.add(movie);
			}
			cursor.close();
		}

		return movies;
	}
}
