package com.reschikov.testtask.readingapp.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(SingleStateStrategy.class)
public interface PagingMainView extends MvpView {

	void loadPages();
	void gotoLastPage(int lastPage);
	void showMessage(String message);
	void showReadAmount(int amountOfRead);
}
