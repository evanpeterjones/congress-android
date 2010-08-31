package com.sunlightlabs.android.congress.notifications.finders;

import java.util.List;

import android.content.Intent;
import android.util.Log;

import com.sunlightlabs.android.congress.R;
import com.sunlightlabs.android.congress.notifications.NotificationFinder;
import com.sunlightlabs.android.congress.notifications.Subscription;
import com.sunlightlabs.android.congress.utils.Utils;
import com.sunlightlabs.yahoo.news.NewsItem;
import com.sunlightlabs.yahoo.news.NewsService;

public class BillNewsFinder extends NotificationFinder {

	@Override
	public String decodeId(Object result) {
		return String.valueOf(((NewsItem) result).timestamp.toMillis(true));
	}
	
	@Override
	public List<?> fetchUpdates(Subscription subscription) {
		String searchTerm = subscription.data;
		String apiKey = context.getResources().getString(R.string.yahoo_news_key);
		
		try {
			return new NewsService(apiKey).fetchNewsResults(searchTerm);
		} catch (Exception e) {
			Log.w(Utils.TAG, "LegislatorNewsFinder: Could not fetch news for legislator " + subscription.name);
			return null;
		}
	}
	
	@Override
	public String notificationMessage(Subscription subscription, int results) {
		if (results > 1)
			return results + " new mentions in the news.";
		else
			return results + " new mention in the news.";
	}
	
	@Override
	public Intent notificationIntent(Subscription subscription) {
		return Utils.billLoadIntent(subscription.id, 
				Utils.billTabsIntent().putExtra("tab", "news"));
	}
}