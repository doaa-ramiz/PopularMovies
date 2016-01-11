package com.doaaramiz.popularmovies.view.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doaaramiz.popularmovies.R;
import com.doaaramiz.popularmovies.business.Business;
import com.doaaramiz.popularmovies.exception.BusinessException;
import com.doaaramiz.popularmovies.exception.NetworkException;
import com.doaaramiz.popularmovies.model.Movie;
import com.doaaramiz.popularmovies.model.SortType;
import com.doaaramiz.popularmovies.util.Utils;
import com.doaaramiz.popularmovies.view.activity.MainActivity;
import com.doaaramiz.popularmovies.view.adapter.MoviesAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends BaseFragment {

	private List<Movie> movies;
	private RecyclerView moviesRecyclerView;
	private RecyclerView.LayoutManager layoutManager;
	private MoviesAdapter moviesAdapter;

	private LinearLayout emptyListLinearLayout;
	private ImageView emptyListImageView;
	private TextView emptyListTextView;

	private boolean tabletLayout;

	public MainFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (movies == null)
			movies = new ArrayList<>();
	}

	@Override
	public void onStart() {
		super.onStart();

		getMovies();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_main, container, false);

		moviesRecyclerView = (RecyclerView) view.findViewById(R.id.moviesRecyclerView);

		layoutManager = new GridLayoutManager(getActivity(), getSpanCount());

		moviesRecyclerView.setLayoutManager(layoutManager);

		moviesAdapter = new MoviesAdapter(movies, getActivity());
		moviesRecyclerView.setAdapter(moviesAdapter);

		emptyListLinearLayout = (LinearLayout) view.findViewById(R.id.emptyListLinearLayout);
		emptyListImageView = (ImageView) view.findViewById(R.id.emptyListImageView);
		emptyListTextView = (TextView) view.findViewById(R.id.emptyListTextView);

		setHasOptionsMenu(true);

		return view;
	}

	private int getSpanCount() {

		int spanCount = 1;
		int orientation = this.getResources().getConfiguration().orientation;

		switch (orientation) {
			case Configuration.ORIENTATION_PORTRAIT:
				spanCount = 2;
				break;
			case Configuration.ORIENTATION_LANDSCAPE:
				spanCount = 3;
				break;
		}
		return spanCount;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_main, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();

		switch (id) {
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
				sortType = SortType.POPULARITY_DESC;
				break;
			case 1:
				sortType = SortType.VOTE_AVERAGE_DESC;
				break;
			case 2:
				sortType = SortType.FAVORITE;
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

	private void renderEmptyListView() {

		emptyListLinearLayout.setVisibility(View.VISIBLE);
		moviesRecyclerView.setVisibility(View.GONE);

		emptyListImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_movie_slate));
		emptyListTextView.setText("Movie list is currently empty");
	}

	private void renderEmptyFavoriteListView() {

		emptyListLinearLayout.setVisibility(View.VISIBLE);
		moviesRecyclerView.setVisibility(View.GONE);

		emptyListImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_broken_heart_96));
		emptyListTextView.setText("Your favorite list is currently empty");
	}

	private void renderView() {

		emptyListLinearLayout.setVisibility(View.GONE);
		moviesRecyclerView.setVisibility(View.VISIBLE);
	}

	public interface Callback {

		void onMovieSelected(Movie movie);
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

			try {
				if ((sortType != null) && (sortType.equals(SortType.FAVORITE))) {
					return Business.getInstance(getActivity().getApplicationContext()).getFavoriteMovies();
				}

				return Business.getInstance(getActivity().getApplicationContext()).getMoviesList(sortType);

			} catch (NetworkException e) {
				e.printStackTrace();
				showNetworkErrorSnackbar();
			} catch (BusinessException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(List<Movie> movies) {

			if (!Utils.collectionIsEmpty(movies)) {
				MainFragment.this.movies = movies;
				moviesAdapter.setMovies(MainFragment.this.movies);
				moviesAdapter.notifyDataSetChanged();
				renderView();

				if (getActivity() instanceof MainActivity) {
					MainActivity mainActivity = (MainActivity) getActivity();
					tabletLayout = mainActivity.isTabletLayout();
				}

				if (tabletLayout)
					((MainFragment.Callback) getActivity()).onMovieSelected(movies.get(0));

			} else {

				if (getSortTypeFromSharedPreference() == 2) {
					renderEmptyFavoriteListView();
				} else {
					renderEmptyListView();
				}

				if (getActivity() instanceof MainActivity) {
					MainActivity mainActivity = (MainActivity) getActivity();
					tabletLayout = mainActivity.isTabletLayout();
				}

				if (tabletLayout)
					((MainFragment.Callback) getActivity()).onMovieSelected(null);

			}

			hideProgressDialog();
		}
	}
}
