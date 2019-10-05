package com.reschikov.testtask.readingapp.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reschikov.testtask.readingapp.R;
import com.reschikov.testtask.readingapp.presenter.Bindable;

public class PagerAdapter extends RecyclerView.Adapter{

	private Bindable bindable;

	public PagerAdapter(Bindable bindable) {
		this.bindable = bindable;
	}

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_page, parent, false);
		return new ViewPageHolder(itemView, bindable);
	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
		DisplayedText displayedText = (DisplayedText) holder;
		bindable.bindView(displayedText, position);
	}

	@Override
	public int getItemCount() {
		return bindable.getItemCount();
	}

	@Override
	public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
		holder.itemView.getViewTreeObserver().addOnScrollChangedListener((ViewPageHolder)holder);
	}

	@Override
	public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
		ScrollView view = (ScrollView) holder.itemView;
		view.getViewTreeObserver().removeOnScrollChangedListener((ViewPageHolder)holder);
		if (view.getScrollY() != 0) view.post(() -> view.scrollTo(0, 0));
	}

	public static class ViewPageHolder extends RecyclerView.ViewHolder implements
		DisplayedText,
		ViewTreeObserver.OnScrollChangedListener{

		private TextView textView;
		private Bindable bindable;
		private int heightLine;

		ViewPageHolder(@NonNull View itemView, Bindable bindable) {
			super(itemView);
			this.bindable = bindable;
			textView = itemView.findViewById(R.id.text);
			heightLine = textView.getLineHeight();
		}

		@Override
		public void show(String text, int place) {
			textView.setText(text);
			if (place != 0)	itemView.post(() -> itemView.scrollTo(0, place));
		}

		@Override
		public void onScrollChanged() {
			int scrollY = itemView.getScrollY();
			textView.post(() ->{
				int lines = textView.getLineCount();
				bindable.determineAmountOfRead(lines, heightLine, scrollY);
			});
		}
	}
}
