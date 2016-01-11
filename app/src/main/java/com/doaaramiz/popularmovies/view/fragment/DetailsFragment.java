package com.doaaramiz.popularmovies.view.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.doaaramiz.popularmovies.R;
import com.doaaramiz.popularmovies.business.Business;
import com.doaaramiz.popularmovies.exception.BusinessException;
import com.doaaramiz.popularmovies.exception.NetworkException;
import com.doaaramiz.popularmovies.model.Movie;
import com.doaaramiz.popularmovies.model.Review;
import com.doaaramiz.popularmovies.model.Trailer;
import com.doaaramiz.popularmovies.util.Utils;
import com.doaaramiz.popularmovies.view.adapter.ReviewsAdapter;
import com.doaaramiz.popularmovies.view.adapter.TrailersAdapter;
import com.doaaramiz.popularmovies.view.util.CustomLinearLayoutManager;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailsFragment extends BaseFragment {

	private static String ARGS_MOVIE = "args_movie";
	private static String ARGS_TABLET_LAYOUT = "args_tablet_layout";
	private Movie movie;
	private ImageView posterImageView;
	private TextView originalTitleTextView;
	private TextView overviewTextView;
	private TextView voteAverageTextView;
	private TextView releaseDateTextView;
	private ToggleButton favoriteToggleButton;
	private LinearLayout trailersLinearLayout;
	private LinearLayout reviewsLinearLayout;
	private ImageView toolbarImageView;
	private View detailsNestedScrollView;
	private View detailsCollapsingToolbar;
	private ImageView emptyDetailsImageView;
	private List<Trailer> trailers;
	private RecyclerView trailersRecyclerView;
	private RecyclerView.LayoutManager trailersLayoutManager;
	private TrailersAdapter trailersAdapter;
	private List<Review> reviews;
	private RecyclerView reviewsRecyclerView;
	private RecyclerView.LayoutManager reviewsLayoutManager;
	private ReviewsAdapter reviewsAdapter;
	private boolean tabletLayout;
	private ShareActionProvider shareActionProvider;

	public DetailsFragment() {
	}

	public static DetailsFragment newInstance(Movie movie, boolean tabletLayout) {
		DetailsFragment fragment = new DetailsFragment();
		Bundle args = new Bundle();
		args.putSerializable(ARGS_MOVIE, movie);
		args.putBoolean(ARGS_TABLET_LAYOUT, tabletLayout);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments() != null) {
			movie = (Movie) getArguments().getSerializable(ARGS_MOVIE);
			tabletLayout = getArguments().getBoolean(ARGS_TABLET_LAYOUT);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_details, container, false);

		posterImageView = (ImageView) view.findViewById(R.id.posterImageView);
		if (view.findViewById(R.id.originalTitleTextView) != null)
			originalTitleTextView = (TextView) view.findViewById(R.id.originalTitleTextView);
		overviewTextView = (TextView) view.findViewById(R.id.overviewTextView);
		voteAverageTextView = (TextView) view.findViewById(R.id.voteAverageTextView);
		releaseDateTextView = (TextView) view.findViewById(R.id.releaseDateTextView);
		trailersRecyclerView = (RecyclerView) view.findViewById(R.id.trailersRecyclerView);
		reviewsRecyclerView = (RecyclerView) view.findViewById(R.id.reviewsRecyclerView);
		favoriteToggleButton = (ToggleButton) view.findViewById(R.id.favoriteToggleButton);
		trailersLinearLayout = (LinearLayout) view.findViewById(R.id.trailersLinearLayout);
		reviewsLinearLayout = (LinearLayout) view.findViewById(R.id.reviewsLinearLayout);
		if (view.findViewById(R.id.toolbarImageView) != null)
			toolbarImageView = (ImageView) view.findViewById(R.id.toolbarImageView);

		if (view.findViewById(R.id.emptyDetailsImageView) != null)
			emptyDetailsImageView = (ImageView) view.findViewById(R.id.emptyDetailsImageView);

		if (view.findViewById(R.id.detailsNestedScrollView) != null)
			detailsNestedScrollView = view.findViewById(R.id.detailsNestedScrollView);
		if (view.findViewById(R.id.detailsCollapsingToolbar) != null)
			detailsCollapsingToolbar = view.findViewById(R.id.detailsCollapsingToolbar);

		if (movie != null) {
			renderView();
			new TrailerListAsyncTask().execute(movie.getId());
			new ReviewListAsyncTask().execute(movie.getId());
		} else {
			if (tabletLayout)
				renderEmptyView();
		}

		setHasOptionsMenu(true);

		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_details, menu);

		MenuItem menuItem = menu.findItem(R.id.action_share);

		shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

		if (movie != null)
			shareActionProvider.setShareIntent(getShareIntent());
	}

	private Intent getShareIntent() {

		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		shareIntent.setType("text/plain");
		String shareMessage = "Watch " + movie.getOriginalTitle() +
							  (Utils.collectionIsEmpty(trailers) ?
							   "" : " here http://www.youtube.com/watch?v=" + trailers.get(0).getKey());
		shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);

		return shareIntent;
	}

	private void renderView() {

		showProgressDialog();

		if (tabletLayout) {

			emptyDetailsImageView.setVisibility(View.GONE);
			detailsNestedScrollView.setVisibility(View.VISIBLE);
			detailsCollapsingToolbar.setVisibility(View.VISIBLE);

			originalTitleTextView.setText(movie.getOriginalTitle());

			Picasso.with(getActivity())
				   .load(getResources().getString(R.string.poster_base_url) + "w780/" + movie.getBackdropPath())
				   .into(toolbarImageView);

			Picasso.with(getActivity())
				   .load(getActivity().getResources().getString(R.string.poster_base_url) + "w500/" + movie.getPosterPath())
				   .into(posterImageView);
		} else {

			Picasso.with(getActivity())
				   .load(getActivity().getResources().getString(R.string.poster_base_url) + "w185/" + movie.getPosterPath())
				   .into(posterImageView);
		}

		overviewTextView.setText(movie.getOverview());
		voteAverageTextView.setText(movie.getVoteAverage() + " / 10");
		releaseDateTextView.setText(movie.getReleaseDate());

		boolean movieExists = Business.getInstance(getActivity().getApplicationContext()).checkMovieInFavorite(movie);

		favoriteToggleButton.setChecked(movieExists);

		favoriteToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

				if (b) {
					Business.getInstance(getActivity().getApplicationContext()).addMovieToFavorite(movie);
				} else {
					Business.getInstance(getActivity().getApplicationContext()).removeMovieFromFavorite(movie);
				}
			}
		});

		hideProgressDialog();
	}

	private void renderEmptyView() {

		emptyDetailsImageView.setVisibility(View.VISIBLE);
		detailsNestedScrollView.setVisibility(View.GONE);
		detailsCollapsingToolbar.setVisibility(View.GONE);
	}

	private void renderTrailersView() {

		trailersLinearLayout.setVisibility(View.VISIBLE);

		trailersLayoutManager = new CustomLinearLayoutManager(getActivity());
		trailersRecyclerView.setLayoutManager(trailersLayoutManager);
		int studentsListRecyclerViewScrollPosition = (trailersRecyclerView.getLayoutManager() != null) ?
													 ((CustomLinearLayoutManager) trailersRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() : 0;
		trailersRecyclerView.scrollToPosition(studentsListRecyclerViewScrollPosition);

		trailersAdapter = new TrailersAdapter(trailers, getActivity().getApplicationContext());
		trailersRecyclerView.setAdapter(trailersAdapter);

		if (shareActionProvider != null)
			shareActionProvider.setShareIntent(getShareIntent());
	}

	private void renderReviewsView() {

		reviewsLinearLayout.setVisibility(View.VISIBLE);

		reviewsLayoutManager = new CustomLinearLayoutManager(getActivity());
		reviewsRecyclerView.setLayoutManager(reviewsLayoutManager);
		int studentsListRecyclerViewScrollPosition = (reviewsRecyclerView.getLayoutManager() != null) ?
													 ((CustomLinearLayoutManager) reviewsRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() : 0;
		reviewsRecyclerView.scrollToPosition(studentsListRecyclerViewScrollPosition);

		reviewsAdapter = new ReviewsAdapter(reviews, getActivity().getApplicationContext());
		reviewsRecyclerView.setAdapter(reviewsAdapter);
	}

	private class TrailerListAsyncTask extends AsyncTask<Long, Void, List<Trailer>> {

		@Override
		protected void onPreExecute() {
			showProgressDialog();
		}

		@Override
		protected List<Trailer> doInBackground(Long... longs) {

			try {
				return Business.getInstance(getActivity().getApplicationContext()).getMovieTrailer(longs[0]);
			} catch (NetworkException e) {
				e.printStackTrace();
				showNetworkErrorSnackbar();
			} catch (BusinessException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<Trailer> trailers) {

			if (!Utils.collectionIsEmpty(trailers)) {
				DetailsFragment.this.trailers = trailers;
				renderTrailersView();
			}

			hideProgressDialog();
		}
	}

	private class ReviewListAsyncTask extends AsyncTask<Long, Void, List<Review>> {

		@Override
		protected void onPreExecute() {
			showProgressDialog();
		}

		@Override
		protected List<Review> doInBackground(Long... longs) {

			try {
				return Business.getInstance(getActivity().getApplicationContext()).getMovieReviews(longs[0]);
			} catch (NetworkException e) {
				e.printStackTrace();
				showNetworkErrorSnackbar();
			} catch (BusinessException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<Review> reviews) {

			if (!Utils.collectionIsEmpty(reviews)) {
				DetailsFragment.this.reviews = reviews;
				renderReviewsView();
			}

			hideProgressDialog();
		}
	}
}
