package com.doaaramiz.popularmovies.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author doaaramiz
 * @date 01.01.2016
 */
public class MovieContract {

	public static final String CONTENT_AUTHORITY = "com.doaaramiz.popularmovies";

	public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

	public static final String PATH_MOVIE = "movie";

	public static final class MovieEntry implements BaseColumns {

		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

		public static final String TABLE_NAME = "movie";

		public static final String COLUMN_TITLE = "title";
		public static final String COLUMN_ORIGINAL_TITLE = "original_title";
		public static final String COLUMN_OVERVIEW = "overview";
		public static final String COLUMN_RELEASE_DATE = "release_date";
		public static final String COLUMN_POSTER_PATH = "poster_path";
		public static final String COLUMN_VOTE_AVERAGE = "vote_average";
		public static final String COLUMN_BACKDROP_PATH = "backdrop_path";

		public static Uri buildMovieUri(long id) {
			return ContentUris.withAppendedId(CONTENT_URI, id);
		}
	}
}
