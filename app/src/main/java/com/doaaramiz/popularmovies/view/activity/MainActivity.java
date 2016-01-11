package com.doaaramiz.popularmovies.view.activity;

import android.content.Intent;
import android.os.Bundle;

import com.doaaramiz.popularmovies.R;
import com.doaaramiz.popularmovies.model.Movie;
import com.doaaramiz.popularmovies.view.fragment.DetailsFragment;
import com.doaaramiz.popularmovies.view.fragment.MainFragment;

public class MainActivity extends BaseActivity implements MainFragment.Callback {

	private boolean tabletLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (findViewById(R.id.detail_container) != null) {
			tabletLayout = true;
			if (savedInstanceState == null) {
				getSupportFragmentManager().beginTransaction()
										   .replace(R.id.detail_container, DetailsFragment.newInstance(null, tabletLayout))
										   .commit();
			}
		} else {
			tabletLayout = false;
		}
	}

	@Override
	protected int getLayout() {
		return R.layout.activity_main;
	}

	public boolean isTabletLayout() {
		return tabletLayout;
	}

	@Override
	public void onMovieSelected(Movie movie) {

		if (tabletLayout) {

			getSupportFragmentManager().beginTransaction()
									   .replace(R.id.detail_container, DetailsFragment.newInstance(movie, tabletLayout))
									   .commit();

		} else {
			Intent intent = new Intent(this, DetailsActivity.class);
			intent.putExtra(getResources().getString(R.string.intent_movie), movie);
			startActivity(intent);
		}
	}
}
