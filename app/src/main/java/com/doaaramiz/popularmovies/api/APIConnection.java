package com.doaaramiz.popularmovies.api;

import android.content.Context;
import android.net.Uri;

import com.doaaramiz.popularmovies.BuildConfig;
import com.doaaramiz.popularmovies.R;
import com.doaaramiz.popularmovies.model.Movie;
import com.doaaramiz.popularmovies.model.SortType;

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

	private Context context;

	public APIConnection(Context context) {
		this.context = context;
	}

	public List<Movie> getMoviesListFromAPI(SortType sortType) {

		HttpURLConnection urlConnection = null;
		BufferedReader reader = null;
		String jsonStr = null;

		try {

			Uri.Builder uriBuilder = Uri.parse(context.getResources().getString(R.string.request_base_url)).buildUpon();

			if (sortType != null)
				uriBuilder.appendQueryParameter("sort_by", sortType.getValue());
			uriBuilder.appendQueryParameter("api_key", BuildConfig.POPULAR_MOVIES_API_KEY).build();

			URL url = new URL(uriBuilder.toString());

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

			return APIHelper.getMoviesListFromJson(jsonStr);

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
}
