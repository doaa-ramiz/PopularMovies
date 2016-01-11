package com.doaaramiz.popularmovies.api;

import com.doaaramiz.popularmovies.model.Movie;
import com.doaaramiz.popularmovies.model.Review;
import com.doaaramiz.popularmovies.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to convert JSON data to models
 *
 * @author doaaramiz
 * @date 05.12.2015
 */
public class Parser {

	private static JSONArray getResultsJsonArray(String json) throws JSONException {

		JSONObject jsonObject = new JSONObject(json);
		return jsonObject.getJSONArray("results");
	}

	protected static List<Movie> parseMoviesFromJson(String json) {

		List<Movie> movieList = new ArrayList<>();

		try {

			JSONArray resultsJsonArray = getResultsJsonArray(json);

			for (int i = 0; i < resultsJsonArray.length(); i++) {
				JSONObject movieJson = resultsJsonArray.getJSONObject(i);
				Long id = movieJson.getLong("id");
				String title = movieJson.getString("title");
				String originalTitle = movieJson.getString("original_title");
				String overview = movieJson.getString("overview");
				String posterPath = movieJson.getString("poster_path");
				String voteAverage = movieJson.getString("vote_average");
				String releaseDate = movieJson.getString("release_date");
				String backdropPath = movieJson.getString("backdrop_path");

				Movie movie = new Movie();
				movie.setId(id);
				movie.setTitle(title);
				movie.setOriginalTitle(originalTitle);
				movie.setOverview(overview);
				if (!posterPath.equalsIgnoreCase("null"))
					movie.setPosterPath(posterPath);
				movie.setVoteAverage(voteAverage);
				movie.setReleaseDate(releaseDate);
				if (!backdropPath.equalsIgnoreCase("null"))
					movie.setBackdropPath(backdropPath);

				movieList.add(movie);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return movieList;
	}

	protected static List<Trailer> parseTrailersFromJson(String json) {

		List<Trailer> trailerList = new ArrayList<>();

		try {

			JSONArray resultsJsonArray = getResultsJsonArray(json);

			for (int i = 0; i < resultsJsonArray.length(); i++) {
				JSONObject trailerJson = resultsJsonArray.getJSONObject(i);
				String id = trailerJson.getString(("id"));
				String key = trailerJson.getString("key");
				String name = trailerJson.getString("name");
				String site = trailerJson.getString("site");
				int size = trailerJson.getInt("size");

				Trailer trailer = new Trailer();
				trailer.setId(id);
				trailer.setKey(key);
				trailer.setName(name);
				trailer.setSite(site);
				trailer.setSize(size);

				trailerList.add(trailer);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return trailerList;
	}

	protected static List<Review> parseReviewsFromJson(String json) {

		List<Review> reviewList = new ArrayList<>();

		try {

			JSONArray resultsJsonArray = getResultsJsonArray(json);

			for (int i = 0; i < resultsJsonArray.length(); i++) {
				JSONObject reviewJson = resultsJsonArray.getJSONObject(i);
				String id = reviewJson.getString(("id"));
				String author = reviewJson.getString("author");
				String content = reviewJson.getString("content");
				String url = reviewJson.getString("url");

				Review review = new Review();
				review.setId(id);
				review.setAuthor(author);
				review.setContent(content);
				review.setUrl(url);

				reviewList.add(review);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return reviewList;
	}
}
