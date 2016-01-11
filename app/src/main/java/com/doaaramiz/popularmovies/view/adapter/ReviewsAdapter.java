package com.doaaramiz.popularmovies.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doaaramiz.popularmovies.R;
import com.doaaramiz.popularmovies.model.Review;

import java.util.List;

/**
 * @author doaaramiz
 * @date 24.12.2015
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

	private List<Review> reviews;
	private Context context;

	public ReviewsAdapter(List<Review> reviews, Context context) {
		this.reviews = reviews;
		this.context = context;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_review, viewGroup, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

		final Review review = reviews.get(position);

		viewHolder.getAuthorTextView().setText(review.getAuthor());

		if (review.getContent().length() > 200) {
			String content = review.getContent().trim().substring(1, 200);
			Spanned contentSpanned = Html.fromHtml(content + " ... " + "<u><i>" + " Continue reading" + "</i></u>");
			viewHolder.getContentTextView().setText(contentSpanned);
		} else {
			viewHolder.getContentTextView().setText(review.getContent().trim());
		}

		viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(review.getUrl()));
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}
		});
	}

	@Override
	public int getItemCount() {
		return reviews.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder {

		private final TextView authorTextView;
		private final TextView contentTextView;

		public ViewHolder(View view) {
			super(view);

			authorTextView = (TextView) view.findViewById(R.id.authorTextView);
			contentTextView = (TextView) view.findViewById(R.id.contentTextView);
		}

		public TextView getAuthorTextView() {
			return authorTextView;
		}

		public TextView getContentTextView() {
			return contentTextView;
		}
	}
}
