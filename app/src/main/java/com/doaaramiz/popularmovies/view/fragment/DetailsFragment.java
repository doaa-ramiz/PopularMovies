package com.doaaramiz.popularmovies.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.doaaramiz.popularmovies.R;
import com.doaaramiz.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

public class DetailsFragment extends BaseFragment {

	private Movie movie;
	private ImageView posterImageView;
	private TextView originalTitleTextView;
	private TextView overviewTextView;
	private TextView voteAverageTextView;
	private TextView releaseDateTextView;

	public DetailsFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		movie = (Movie) getActivity().getIntent().getSerializableExtra(getActivity().getResources().getString(R.string.intent_movie));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_details, container, false);

		posterImageView = (ImageView) view.findViewById(R.id.posterImageView);
		originalTitleTextView = (TextView) view.findViewById(R.id.originalTitleTextView);
		overviewTextView = (TextView) view.findViewById(R.id.overviewTextView);
		voteAverageTextView = (TextView) view.findViewById(R.id.voteAverageTextView);
		releaseDateTextView = (TextView) view.findViewById(R.id.releaseDateTextView);

		renderView();

		return view;
	}

	private void renderView() {

		Picasso.with(getActivity()).load(getActivity().getResources().getString(R.string.poster_base_url) + movie.getPosterPath()).into(posterImageView);

		originalTitleTextView.setText(movie.getOriginalTitle());
		overviewTextView.setText(movie.getOverview());
		voteAverageTextView.setText(movie.getVoteAverage());
		releaseDateTextView.setText(movie.getReleaseDate());

	}
}
