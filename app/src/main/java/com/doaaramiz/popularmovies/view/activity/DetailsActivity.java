package com.doaaramiz.popularmovies.view.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.doaaramiz.popularmovies.R;
import com.doaaramiz.popularmovies.model.Movie;
import com.doaaramiz.popularmovies.view.fragment.DetailsFragment;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Movie movie = (Movie) getIntent().getSerializableExtra(getResources().getString(R.string.intent_movie));

		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle(movie.getOriginalTitle());

			Picasso.with(this)
				   .load(getResources().getString(R.string.poster_base_url) + "w500/" + movie.getBackdropPath())
				   .into((ImageView) findViewById(R.id.toolbarImageView));
		}

		if (savedInstanceState == null) {

			getSupportFragmentManager().beginTransaction()
									   .replace(R.id.detail_container, DetailsFragment.newInstance(movie, false))
									   .commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				this.finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected int getLayout() {
		return R.layout.activity_details;
	}
}
