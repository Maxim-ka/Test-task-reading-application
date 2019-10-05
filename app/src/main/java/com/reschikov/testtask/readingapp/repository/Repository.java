package com.reschikov.testtask.readingapp.repository;

import com.reschikov.testtask.readingapp.repository.provider.FireStoreProvider;
import com.reschikov.testtask.readingapp.repository.provider.RemoteDataProvider;

import java.util.List;

import io.reactivex.Single;

public class Repository {

	private final RemoteDataProvider remoteProvider = new FireStoreProvider();

	public Single<List<String>> getAll(){
		return remoteProvider.getText();
	}
}
