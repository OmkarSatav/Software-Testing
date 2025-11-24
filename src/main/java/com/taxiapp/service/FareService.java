package com.taxiapp.service;

import com.taxiapp.model.FareConfig;
import com.taxiapp.repository.FareConfigRepository;
import com.taxiapp.util.FareCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FareService {

	@Autowired
	private FareConfigRepository fareConfigRepository;

	private static final double DEFAULT_BASE_FARE = 50.0;
	private static final double DEFAULT_RATE_PER_KM = 10.0;
	private static final Integer DEFAULT_MAX_PASSENGERS = 4;
	private static final Double DEFAULT_MAX_RADIUS = 50.0;
	private static final Double DEFAULT_MIN_FARE = 50.0;

	public FareConfig getDefaultConfig() {
		List<FareConfig> configs = fareConfigRepository.findAll();
		if (configs.isEmpty()) {
			return initializeDefaultConfig();
		}
		return configs.get(0);
	}

	private FareConfig initializeDefaultConfig() {
		FareConfig config = new FareConfig();
		config.setBaseFare(DEFAULT_BASE_FARE);
		config.setRatePerKm(DEFAULT_RATE_PER_KM);
		config.setPeakHourMultiplier(1.5);
		config.setMaxPassengers(DEFAULT_MAX_PASSENGERS);
		config.setMaxServiceRadius(DEFAULT_MAX_RADIUS);
		config.setMinFare(DEFAULT_MIN_FARE);
		return fareConfigRepository.save(config);
	}

	public Double calculateFare(double distance, LocalDateTime pickupTime) {
		FareConfig config = getDefaultConfig();
		double fare = FareCalculator.calculateFare(distance, config.getBaseFare(),
				config.getRatePerKm(), pickupTime);

		if (fare < config.getMinFare()) {
			return config.getMinFare();
		}

		return fare;
	}

	public boolean validatePassengers(Integer passengers) {
		if (passengers == null) {
			return false;
		}
		FareConfig config = getDefaultConfig();
		return passengers > 0 && passengers <= config.getMaxPassengers();
	}

	public boolean validateDistance(Double distance) {
		if (distance == null) {
			return false;
		}
		FareConfig config = getDefaultConfig();
		return distance >= 0 && distance <= config.getMaxServiceRadius();
	}

	public void updateFareConfig(FareConfig newConfig) {
		FareConfig existing = getDefaultConfig();
		if (newConfig.getBaseFare() != null) {
			existing.setBaseFare(newConfig.getBaseFare());
		}
		if (newConfig.getRatePerKm() != null) {
			existing.setRatePerKm(newConfig.getRatePerKm());
		}
		if (newConfig.getMaxPassengers() != null) {
			existing.setMaxPassengers(newConfig.getMaxPassengers());
		}
		fareConfigRepository.save(existing);
	}

}

