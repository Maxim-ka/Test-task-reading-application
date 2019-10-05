package com.reschikov.testtask.readingapp.view;

import androidx.viewpager2.widget.ViewPager2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.reschikov.testtask.readingapp.R;
import com.reschikov.testtask.readingapp.presenter.MainPresenter;
import com.reschikov.testtask.readingapp.view.adapter.PagerAdapter;

public class MainActivity extends MvpAppCompatActivity implements PagingMainView{

	@InjectPresenter MainPresenter presenter;

	private static final String CURRENT_PLACE = "current place";
	private static final String PAGE = "page";
	private static final String PLACE = "place";
	private static final String TITLE = "Стр №  , прочитано   \\";
	private final StringBuilder sb = new StringBuilder(TITLE);
	private int startPage = sb.indexOf("№") + 2;
	private int endPage = sb.indexOf(",");
	private int startPercentageOfRead = sb.length() - 2;
	private SharedPreferences sp;
	private ViewPager2 viewPager;
	private PagerAdapter adapter;

	@ProvidePresenter
	MainPresenter providePresenter(){
		sp = getBaseContext().getSharedPreferences(CURRENT_PLACE, MODE_PRIVATE);
		int lastPage = sp.getInt(PAGE, 0);
		int lastPlace = sp.getInt(PLACE, 0);
		return new MainPresenter(lastPage, lastPlace);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		viewPager = findViewById(R.id.view_pager_2);
		adapter = new PagerAdapter(presenter.getRecyclePresenter());
		viewPager.setAdapter(adapter);
		viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
			@Override
			public void onPageSelected(int position) {
				sb.replace(startPage, endPage, String.valueOf(position + 1));
				setTitle(sb);
			}
		});
	}

	@Override
	protected void onStop() {
		super.onStop();
		String currentPage = sb.substring(startPage, endPage).trim();
		int currentPlace = presenter.getLastPlace();
		rememberCurrentPlace(currentPage, currentPlace);
	}

	private void rememberCurrentPlace(String currentPage, int currentPlace){
		try {
			int page = Integer.parseInt(currentPage) - 1;			
			sp.edit().putInt(PAGE, page).putInt(PLACE, currentPlace).apply();
		}catch (NumberFormatException e){
			showMessage(e.getMessage());
		}
	}

	@Override
	public void loadPages() {
		adapter.notifyDataSetChanged();
	}

	@Override
	public void gotoLastPage(int lastPage) {
		viewPager.setCurrentItem(lastPage);
	}

	@Override
	public void showMessage(String message) {
		Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
	}

	@Override
	public void showReadAmount(int amountOfRead) {
		sb.replace(startPercentageOfRead, sb.length(), String.valueOf(amountOfRead));
		setTitle(sb);
	}
}
