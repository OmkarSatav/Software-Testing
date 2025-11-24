package com.taxiapp.util;

import java.time.LocalDateTime;

public class FareCalculator {

	public static double calculateFare(double distance, double baseFare, double ratePerKm, LocalDateTime pickupTime) {
		double fare = baseFare + (distance * ratePerKm);

		if (isPeakHour(pickupTime)) {
			fare = fare * 1.5;
		}

		return Math.round(fare * 100.0) / 100.0;
	}

	private static boolean isPeakHour(LocalDateTime dateTime) {
		int hour = dateTime.getHour();
		return (hour >= 8 && hour <= 10) || (hour >= 17 && hour <= 19);
	}

	public static boolean isFareValid(Double fare, double minFare, double maxFare) {
		return fare != null && fare >= minFare && fare <= maxFare;
	}

	public static boolean isFarePositive(Double fare) {
		return fare != null && fare > 0;
	}

}

