package com.reschikov.testtask.readingapp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.reschikov.testtask.readingapp.repository.Repository;
import com.reschikov.testtask.readingapp.view.PagingMainView;
import com.reschikov.testtask.readingapp.view.adapter.DisplayedText;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class MainPresenter extends MvpPresenter<PagingMainView> {

	private static final int TOTAL_PERCENTAGE = 100;
	private final RecyclePresenter recyclePresenter = new RecyclePresenter();
	private final Repository repository = new Repository();
	private List<String> list;
	private int lastPage;
	private int lastPlace;
	private int scrollY;
	private Disposable disposable;

	public RecyclePresenter getRecyclePresenter() {
		return recyclePresenter;
	}

	public int getLastPlace() {
		return scrollY;
	}

	public MainPresenter(int lastPage, int lastPlace) {
		this.lastPage = lastPage;
		this.lastPlace = lastPlace;
		setPages();
	}

	private void setPages(){
		disposable = repository.getAll()
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(list -> {
					this.list = list;
					getViewState().loadPages();
					getViewState().gotoLastPage(lastPage);
				},
				e -> getViewState().showMessage(e.getMessage()));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (disposable != null) disposable.dispose();
	}

	private class RecyclePresenter implements Bindable{

		@Override
		public void bindView(DisplayedText displayedText, int position) {
			int place = (lastPage == position) ? lastPlace : 0;
			displayedText.show(list.get(position), place);
			if (lastPage == position) lastPlace = 0;
		}

		@Override
		public int getItemCount() {
			return (list != null) ? list.size() : 0;
		}

		@Override
		public void determineAmountOfRead(int lineCount, int heightLine, int scrollY) {
			int percent = 0;
			if (lineCount != 0){
				MainPresenter.this.scrollY = scrollY;
				int currentLine = scrollY / heightLine;
				percent = currentLine * TOTAL_PERCENTAGE / lineCount;
			}
			getViewState().showReadAmount(percent);
		}
	}
}
