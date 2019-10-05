package com.reschikov.testtask.readingapp.repository.provider;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.Single;

public class FireStoreProvider implements RemoteDataProvider {

	private static final int PAGE_SIZE = 500;
	private final FirebaseFirestore db = FirebaseFirestore.getInstance();

	@Override
	public Single<List<String>> getText() {
		return Single.create(emitter -> db.collection("text")
			.document("string")
			.get()
			.addOnCompleteListener(task -> {
				if (task.isSuccessful() && task.getResult() != null) {
					emitter.onSuccess(getPages(task));
				} else {
					emitter.onError(task.getException());
				}
			}));
	}

	private List<String> getPages(Task<DocumentSnapshot> task){
		List<String> list = new ArrayList<>();
		if (task.getResult() == null) return list;
		String string = (String) task.getResult().get("pages");
		if (string == null) return list;
		for (int start = 0, end = PAGE_SIZE; start < string.length() ; start += PAGE_SIZE, end += PAGE_SIZE) {
			if (end > string.length()) end = string.length();
			String str = string.substring(start, end);
			list.add(str);
		}
		return list;
	}
}
