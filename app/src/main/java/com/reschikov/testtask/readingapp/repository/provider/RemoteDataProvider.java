package com.reschikov.testtask.readingapp.repository.provider;

import java.util.List;

import io.reactivex.Single;

public interface RemoteDataProvider {

	Single<List<String>> getText();
}
