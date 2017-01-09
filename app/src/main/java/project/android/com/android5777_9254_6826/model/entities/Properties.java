//
//
//  Generated by StarUML(tm) Java Add-In
//
//  @ Project : Untitled
//  @ File Name : Properties.java
//  @ Date : 2016-11-21
//  @ Author : 
//
//
package project.android.com.android5777_9254_6826.model.entities;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

import java.io.Serializable;

public class Properties implements Serializable {
	public enum AttractionType {
		HotelDeal,
		TravelAgency,
		EntertainmentShow,
		Airline;

	}
	public static int valueof(String str){
		switch(str){
			case "HotelDeal":
				return 0;
			case "TravelAgency":
				return 1;
			case "EntertainmentShow":
				return 2;
			case "Airline":
				return 3;
			default:
				return -1;

		}

	}
	public static AttractionType Valueof(String str){
		switch(str){
			case "HotelDeal":
				return AttractionType.HotelDeal;
			case "TravelAgency":
				return AttractionType.TravelAgency;
			case "EntertainmentShow":
				return AttractionType.EntertainmentShow;
			case "Airline":
				return AttractionType.Airline;
			default:
				return AttractionType.HotelDeal;

		}

	}
	public static String[] getTypes() {
		return new String[]{"HotelDeal",
				"TravelAgency",
				"EntertainmentShow",
				"Airline"};
	}
	public static void animateView(final View view, final int toVisibility, float toAlpha, int duration) {
		boolean show = toVisibility == View.VISIBLE;
		if (show) {
			view.setAlpha(0);
		}
		view.setVisibility(View.VISIBLE);
		view.animate()
				.setDuration(duration)
				.alpha(show ? toAlpha : 0)
				.setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						view.setVisibility(toVisibility);
					}
				});
	}
}
