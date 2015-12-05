package com.doaaramiz.popularmovies.api;

import com.doaaramiz.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author doaaramiz
 * @date 05.12.2015
 */
public class APIHelper {

	protected static List<Movie> getMoviesListFromJson(String json) {

		List<Movie> movieList = new ArrayList<>();

		try {

			JSONObject moviesJson = new JSONObject(json);
			JSONArray resultsJsonArray = moviesJson.getJSONArray("results");

			for (int i = 0; i < resultsJsonArray.length(); i++) {
				JSONObject movieJson = resultsJsonArray.getJSONObject(i);
				Long id = movieJson.getLong("id");
				String title = movieJson.getString("title");
				String originalTitle = movieJson.getString("original_title");
				String overview = movieJson.getString("overview");
				String posterPath = movieJson.getString("poster_path");
				String voteAverage = movieJson.getString("vote_average");
				String releaseDate = movieJson.getString("release_date");

				Movie movie = new Movie();
				movie.setId(id);
				movie.setTitle(title);
				movie.setOriginalTitle(originalTitle);
				movie.setOverview(overview);
				movie.setPosterPath(posterPath);
				movie.setVoteAverage(voteAverage);
				movie.setReleaseDate(releaseDate);

				movieList.add(movie);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return movieList;
	}

}
