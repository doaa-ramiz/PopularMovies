package com.doaaramiz.popularmovies.view.activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.doaaramiz.popularmovies.R;

public class DetailsActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getSupportActionBar() != null)
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
