package com.doaaramiz.popularmovies.view.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.doaaramiz.popularmovies.R;
import com.doaaramiz.popularmovies.api.APIConnection;
import com.doaaramiz.popularmovies.model.Movie;
import com.doaaramiz.popularmovies.model.SortType;
import com.doaaramiz.popularmovies.util.Utils;
import com.doaaramiz.popularmovies.view.adapter.MoviesAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends BaseFragment {

	private RecyclerView moviesRecyclerView;
	private MoviesAdapter moviesAdapter;
	private RecyclerView.LayoutManager mLayoutManager;
	private List<Movie> movies;

	public MainFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (movies == null)
			movies = new ArrayList<>();

		getMovies();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_main, container, false);

		moviesRecyclerView = (RecyclerView) view.findViewById(R.id.moviesRecyclerView);

		mLayoutManager = new GridLayoutManager(getActivity(), 2);
		moviesRecyclerView.setLayoutManager(mLayoutManager);

		moviesAdapter = new MoviesAdapter(movies, getActivity().getApplicationContext());
		moviesRecyclerView.setAdapter(moviesAdapter);

		setHasOptionsMenu(true);

		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_main, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();

		switch (id) {
			case R.id.action_settings:
				return true;

			case R.id.action_sort:
				sortMoviesDialog();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void getMovies() {

		int sortTypeInt = getSortTypeFromSharedPreference();
		getMoviesBySortType(sortTypeInt);
	}

	private int getSortTypeFromSharedPreference() {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		return sharedPrefs.getInt(getString(R.string.pref_item), -1);
	}

	private void getMoviesBySortType(int sortTypeInt) {

		SortType sortType = null;

		switch (sortTypeInt) {
			case -1:
				break;
			case 0:
				sortType = SortType.POPULARITY_ASC;
				break;
			case 1:
				sortType = SortType.VOTE_AVERAGE_ASC;
				break;
		}

		new MoviesListAsyncTask().execute(sortType);
	}

	private void sortMoviesDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		int item = getSortTypeFromSharedPreference();

		builder.setTitle(R.string.dialog_title)
			   .setSingleChoiceItems(R.array.sort_array, item, new DialogInterface.OnClickListener() {
				   @Override
				   public void onClick(DialogInterface dialogInterface, int i) {

					   SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
					   sharedPrefs.edit().putInt(getString(R.string.pref_item), i).apply();
					   getMoviesBySortType(i);
					   dialogInterface.dismiss();
				   }
			   });

		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int id) {
				dialogInterface.cancel();
			}
		});

		AlertDialog dialog = builder.create();
		dialog.show();
	}

	private class MoviesListAsyncTask extends AsyncTask<SortType, Void, List<Movie>> {

		@Override
		protected void onPreExecute() {
			showProgressDialog();
		}

		@Override
		protected List<Movie> doInBackground(SortType... sortTypes) {

			SortType sortType = null;
			if (sortTypes.length > 0) {
				sortType = sortTypes[0];
			}

			return new APIConnection(getActivity().getApplicationContext()).getMoviesListFromAPI(sortType);
		}

		@Override
		protected void onPostExecute(List<Movie> movies) {

			if (!Utils.collectionIsEmpty(movies)) {
				MainFragment.this.movies = movies;
				moviesAdapter.setMovies(MainFragment.this.movies);
				moviesAdapter.notifyDataSetChanged();
			}

			hideProgressDialog();
		}
	}
}
