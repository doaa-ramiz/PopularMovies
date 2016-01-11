package com.doaaramiz.popularmovies.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author doaaramiz
 * @date 01.01.2016
 */
public class MovieDBHelper extends SQLiteOpenHelper {

	static final String DATABASE_NAME = "movie.db";
	private static final int DATABASE_VERSION = 2;

	public MovieDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {

		final String CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME +
										  " (" +
										  MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY," +
										  MovieContract.MovieEntry.COLUMN_TITLE + " TEXT , " +
										  MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT , " +
										  MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT , " +
										  MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT , " +
										  MovieContract.MovieEntry.COLUMN_POSTER_PATH + " TEXT , " +
										  MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " TEXT , " +
										  MovieContract.MovieEntry.COLUMN_BACKDROP_PATH + " TEXT" +
										  " );";

		sqLiteDatabase.execSQL(CREATE_MOVIE_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);

		onCreate(sqLiteDatabase);
	}
}
