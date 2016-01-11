package com.doaaramiz.popularmovies.view.util;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author doaaramiz
 * @date 24.12.2015
 */
public class CustomLinearLayoutManager extends LinearLayoutManager {

	private int[] mMeasuredDimension = new int[2];

	/**
	 * Creates a vertical LinearLayoutManager
	 *
	 * @param context Current context, will be used to access resources.
	 */
	public CustomLinearLayoutManager(Context context) {
		super(context);
	}

	/**
	 * @param context       Current context, will be used to access resources.
	 * @param orientation   Layout orientation. Should be {@link #HORIZONTAL} or {@link #VERTICAL}.
	 * @param reverseLayout When set to true, layouts from end to start.
	 */
	public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
		super(context, orientation, reverseLayout);
	}

	/**
	 * Measure the attached RecyclerView. Implementations must call
	 * {@link #setMeasuredDimension(int, int)} before returning.
	 * <p/>
	 * <p>The default implementation will handle EXACTLY measurements and respect
	 * the minimum width and height properties of the host RecyclerView if measured
	 * as UNSPECIFIED. AT_MOST measurements will be treated as EXACTLY and the RecyclerView
	 * will consume all available space.</p>
	 *
	 * @param recycler   Recycler
	 * @param state      Transient state of RecyclerView
	 * @param widthSpec  Width {@link View.MeasureSpec}
	 * @param heightSpec Height {@link View.MeasureSpec}
	 */
	@Override
	public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
		final int widthMode = View.MeasureSpec.getMode(widthSpec);
		final int heightMode = View.MeasureSpec.getMode(heightSpec);
		final int widthSize = View.MeasureSpec.getSize(widthSpec);
		final int heightSize = View.MeasureSpec.getSize(heightSpec);
		int width = 0;
		int height = 0;
		for (int i = 0; i < getItemCount(); i++) {
			measureScrapChild(recycler, i,
							  View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
							  View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
							  mMeasuredDimension);

			if (getOrientation() == HORIZONTAL) {
				width = width + mMeasuredDimension[0];
				if (i == 0) {
					height = mMeasuredDimension[1];
				}
			} else {
				height = height + mMeasuredDimension[1];
				if (i == 0) {
					width = mMeasuredDimension[0];
				}
			}
		}

		switch (widthMode) {
			case View.MeasureSpec.EXACTLY:
				width = widthSize;
			case View.MeasureSpec.AT_MOST:
			case View.MeasureSpec.UNSPECIFIED:
		}

		switch (heightMode) {
			case View.MeasureSpec.EXACTLY:
				height = heightSize;
			case View.MeasureSpec.AT_MOST:
			case View.MeasureSpec.UNSPECIFIED:
		}

		int widthDesired = Math.min(widthSize, width);
		setMeasuredDimension(widthDesired, height);
	}

	private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec, int heightSpec, int[] measuredDimension) {

		View view = recycler.getViewForPosition(position);
		recycler.bindViewToPosition(view, position);

		if (view != null) {
			RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
			int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, getPaddingLeft() + getPaddingRight(), p.width);
			int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, getPaddingTop() + getPaddingBottom(), p.height);
			view.measure(childWidthSpec, childHeightSpec);
			Rect outRect = new Rect();
			calculateItemDecorationsForChild(view, outRect);
			measuredDimension[0] = view.getMeasuredWidth() + p.leftMargin + p.rightMargin;
			measuredDimension[1] = view.getMeasuredHeight() + p.bottomMargin + p.topMargin + outRect.bottom + outRect.top;
			recycler.recycleView(view);
		}
	}
}
