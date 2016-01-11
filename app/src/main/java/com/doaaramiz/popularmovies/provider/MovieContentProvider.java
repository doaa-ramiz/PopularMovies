package com.doaaramiz.popularmovies.provider;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MovieContentProvider extends ContentProvider {

	static final int MOVIE = 100;
	private static final UriMatcher sUriMatcher = buildUriMatcher();
	private MovieDBHelper movieDBHelper;

	static UriMatcher buildUriMatcher() {

		final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		final String authority = MovieContract.CONTENT_AUTHORITY;

		matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);

		return matcher;
	}

	@Override
	public boolean onCreate() {
		movieDBHelper = new MovieDBHelper(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {

		if (sUriMatcher.match(uri) == MOVIE) {
			return MovieContract.MovieEntry.CONTENT_TYPE;
		} else {
			throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

		Cursor retCursor;

		if (sUriMatcher.match(uri) == MOVIE) {
			retCursor = movieDBHelper.getReadableDatabase().query(
					MovieContract.MovieEntry.TABLE_NAME,
					projection,
					selection,
					selectionArgs,
					null,
					null,
					sortOrder);
		} else {
			throw new UnsupportedOperationException("Unknown uri: " + uri);
		}

		retCursor.setNotificationUri(getContext().getContentResolver(), uri);
		return retCursor;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {

		final SQLiteDatabase db = movieDBHelper.getWritableDatabase();
		Uri returnUri;

		if (sUriMatcher.match(uri) == MOVIE) {
			long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
			if (_id > 0) {
				returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
			} else {
				throw new android.database.SQLException("Failed to insert row into " + uri);
			}
		} else {
			throw new UnsupportedOperationException("Unknown uri: " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return returnUri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {

		final SQLiteDatabase db = movieDBHelper.getWritableDatabase();
		int rowsDeleted;
		if (null == selection) selection = "1";

		if (sUriMatcher.match(uri) == MOVIE) {
			rowsDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
		} else {
			throw new UnsupportedOperationException("Unknown uri: " + uri);
		}

		if (rowsDeleted != 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return rowsDeleted;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

		final SQLiteDatabase db = movieDBHelper.getWritableDatabase();
		int rowsUpdated;

		if (sUriMatcher.match(uri) == MOVIE) {
			rowsUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);
		} else {
			throw new UnsupportedOperationException("Unknown uri: " + uri);
		}

		if (rowsUpdated != 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return rowsUpdated;
	}

	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {

		final SQLiteDatabase db = movieDBHelper.getWritableDatabase();

		if (sUriMatcher.match(uri) == MOVIE) {
			db.beginTransaction();
			int returnCount = 0;
			try {
				for (ContentValues value : values) {
					long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
					if (_id != -1) {
						returnCount++;
					}
				}
				db.setTransactionSuccessful();
			} finally {
				db.endTransaction();
			}
			getContext().getContentResolver().notifyChange(uri, null);
			return returnCount;
		} else {
			return super.bulkInsert(uri, values);
		}
	}

	@Override
	@TargetApi(11)
	public void shutdown() {
		movieDBHelper.close();
		super.shutdown();
	}
}
