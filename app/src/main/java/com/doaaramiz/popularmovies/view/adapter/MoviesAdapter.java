package com.doaaramiz.popularmovies.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.doaaramiz.popularmovies.R;
import com.doaaramiz.popularmovies.model.Movie;
import com.doaaramiz.popularmovies.view.fragment.MainFragment;
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

		if (movie.getPosterPath() != null) {
			Picasso.with(context)
				   .load(context.getResources().getString(R.string.poster_base_url) + "w500/" + movie.getPosterPath())
				   .into(viewHolder.getPosterImageView());
		}

		viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				((MainFragment.Callback) context).onMovieSelected(movie);
			}
		});
	}

	@Override
	public int getItemCount() {
		return movies.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder {

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
