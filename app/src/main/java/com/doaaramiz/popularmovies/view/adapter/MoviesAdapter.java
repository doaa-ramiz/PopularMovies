package com.doaaramiz.popularmovies.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.doaaramiz.popularmovies.R;
import com.doaaramiz.popularmovies.model.Movie;
import com.doaaramiz.popularmovies.view.activity.DetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author doaaramiz
 * @date 24.11.2015
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

	private List<Movie> movies;
	private Context context;

	public MoviesAdapter(List<Movie> movies, Context context) {
		this.movies = movies;
		this.context = context;
	}

	public void setMovies(List<Movie> movies) {
		this.movies = movies;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_movie, viewGroup, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {

		final Movie movie = movies.get(position);

		Picasso.with(context).load(context.getResources().getString(R.string.poster_base_url) + movie.getPosterPath()).into(viewHolder.getPosterImageView());

		viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(context, DetailsActivity.class);
				intent.putExtra(context.getResources().getString(R.string.intent_movie), movie);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}
		});
	}

	@Override
	public int getItemCount() {
		return movies.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {

		private final ImageView posterImageView;

		public ViewHolder(View view) {
			super(view);
			posterImageView = (ImageView) view.findViewById(R.id.posterImageView);
		}

		public ImageView getPosterImageView() {
			return posterImageView;
		}
	}
}
