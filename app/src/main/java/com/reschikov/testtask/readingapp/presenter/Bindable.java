package com.reschikov.testtask.readingapp.presenter;

import com.reschikov.testtask.readingapp.view.adapter.DisplayedText;

public interface Bindable {

	void bindView(DisplayedText displayedText, int position);
	int getItemCount();
	void determineAmountOfRead(int lineCount, int heightLine, int scrollY);
}
