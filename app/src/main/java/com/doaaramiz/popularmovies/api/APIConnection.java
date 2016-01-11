package com.doaaramiz.popularmovies.api;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.doaaramiz.popularmovies.BuildConfig;
import com.doaaramiz.popularmovies.R;
import com.doaaramiz.popularmovies.model.Movie;
import com.doaaramiz.popularmovies.model.Review;
import com.doaaramiz.popularmovies.model.SortType;
import com.doaaramiz.popularmovies.model.Trailer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * @author doaaramiz
 * @date 04.12.2015
 */
public class APIConnection {

	private static APIConnection apiConnection;
	private String baseUrl;

	private APIConnection(Context context) {
		baseUrl = context.getResources().getString(R.string.request_base_url);
	}

	public static APIConnection getInstance(Context context) {
		if (apiConnection == null)
			apiConnection = new APIConnection(context);
		return apiConnection;
	}

	private String getDataFromAPI(Uri.Builder uriBuilder) {

		HttpURLConnection urlConnection = null;
		BufferedReader reader = null;
		String jsonStr;

		try {

			uriBuilder.appendQueryParameter("api_key", BuildConfig.POPULAR_MOVIES_API_KEY).build();

			URL url = new URL(uriBuilder.toString());

			Log.d("URL   -   ", url.toString());

			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();

			InputStream inputStream = urlConnection.getInputStream();
			StringBuilder buffer = new StringBuilder();
			if (inputStream == null) {
				return null;
			}
			reader = new BufferedReader(new InputStreamReader(inputStream));

			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line).append("\n");
			}

			if (buffer.length() == 0) {
				return null;
			}
			jsonStr = buffer.toString();

			return jsonStr;

		} catch (IOException e) {
			return null;
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public List<Movie> getMoviesListFromAPI(SortType sortType) {

		Uri.Builder uriBuilder = Uri.parse(baseUrl + "discover/movie?").buildUpon();

		if (sortType != null)
			uriBuilder.appendQueryParameter("sort_by", sortType.getValue());

		String jsonStr = getDataFromAPI(uriBuilder);

		return Parser.parseMoviesFromJson(jsonStr);
	}

	public List<Trailer> getMovieTrailerFromAPI(Long movieId) {

		Uri.Builder uriBuilder = Uri.parse(baseUrl + "movie/" + movieId + "/videos").buildUpon();

		String jsonStr = getDataFromAPI(uriBuilder);

		return Parser.parseTrailersFromJson(jsonStr);
	}

	public List<Review> getMovieReviewsFromAPI(Long movieId) {

		Uri.Builder uriBuilder = Uri.parse(baseUrl + "movie/" + movieId + "/reviews").buildUpon();

		String jsonStr = getDataFromAPI(uriBuilder);

		return Parser.parseReviewsFromJson(jsonStr);
	}
}
