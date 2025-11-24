package com.taxiapp.util;

import java.time.LocalDateTime;

public class TimeValidator {

	public static boolean isValidPickupTime(LocalDateTime pickupTime) {
		return pickupTime != null && pickupTime.isAfter(LocalDateTime.now());
	}

	public static boolean isWithinBusinessHours(LocalDateTime dateTime) {
		int hour = dateTime.getHour();
		return hour >= 6 && hour <= 23;
	}

	public static boolean isNotInPast(LocalDateTime dateTime) {
		return dateTime != null && !dateTime.isBefore(LocalDateTime.now());
	}

}

