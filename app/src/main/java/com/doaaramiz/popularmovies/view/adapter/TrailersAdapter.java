package com.doaaramiz.popularmovies.view.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doaaramiz.popularmovies.R;
import com.doaaramiz.popularmovies.model.Trailer;

import java.util.List;

/**
 * @author doaaramiz
 * @date 24.12.2015
 */
public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ViewHolder> {

	private List<Trailer> trailers;
	private Context context;

	public TrailersAdapter(List<Trailer> trailers, Context context) {
		this.trailers = trailers;
		this.context = context;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_trailer, viewGroup, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {

		final Trailer trailer = trailers.get(position);

		viewHolder.getTrailerNameTextView().setText(trailer.getName());

		viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Intent intent = null;

				try {
					intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailer.getKey()));
				} catch (ActivityNotFoundException ex) {
					intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
				} finally {
					if (intent != null) {
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent);
					}
				}
			}
		});
	}

	@Override
	public int getItemCount() {
		return trailers.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder {

		private final TextView trailerNameTextView;

		public ViewHolder(View view) {
			super(view);

			trailerNameTextView = (TextView) view.findViewById(R.id.trailerNameTextView);
		}

		public TextView getTrailerNameTextView() {
			return trailerNameTextView;
		}
	}
}
