package com.taxiapp.config;

import com.taxiapp.model.Driver;
import com.taxiapp.model.FareConfig;
import com.taxiapp.model.User;
import com.taxiapp.repository.DriverRepository;
import com.taxiapp.repository.FareConfigRepository;
import com.taxiapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DriverRepository driverRepository;

	@Autowired
	private FareConfigRepository fareConfigRepository;

	@Override
	public void run(String... args) {
		if (userRepository.count() == 0) {
			initializeUsers();
		}

		if (driverRepository.count() == 0) {
			initializeDrivers();
		}

		if (fareConfigRepository.count() == 0) {
			initializeFareConfig();
		}
	}

	private void initializeUsers() {
		User admin = new User();
		admin.setEmail("admin@taxibooking.com");
		admin.setPassword("admin123");
		admin.setFirstName("Admin");
		admin.setLastName("User");
		admin.setPhoneNumber("9999999999");
		admin.setRole("ADMIN");
		admin.setActive(true);
		admin.setCreatedAt(LocalDateTime.now());
		admin.setUpdatedAt(LocalDateTime.now());
		userRepository.save(admin);

		User testUser = new User();
		testUser.setEmail("user@test.com");
		testUser.setPassword("user123");
		testUser.setFirstName("Test");
		testUser.setLastName("User");
		testUser.setPhoneNumber("8888888888");
		testUser.setRole("USER");
		testUser.setActive(true);
		testUser.setCreatedAt(LocalDateTime.now());
		testUser.setUpdatedAt(LocalDateTime.now());
		userRepository.save(testUser);
	}

	private void initializeDrivers() {
		String[][] driversData = {
			{"Rajesh Kumar", "9876543210", "driver1@taxi.com", "MH01AB1234", "SEDAN", "19.0760", "72.8777"},
			{"Amit Sharma", "9876543211", "driver2@taxi.com", "MH02CD5678", "SUV", "19.1136", "72.9083"},
			{"Priya Singh", "9876543212", "driver3@taxi.com", "MH03EF9012", "HATCHBACK", "19.0896", "72.8656"},
			{"Vijay Patel", "9876543213", "driver4@taxi.com", "MH04GH3456", "SEDAN", "19.1258", "72.8347"},
			{"Sunita Desai", "9876543214", "driver5@taxi.com", "MH05IJ7890", "SUV", "19.0521", "72.8991"}
		};

		for (String[] data : driversData) {
			Driver driver = new Driver();
			driver.setName(data[0]);
			driver.setPhoneNumber(data[1]);
			driver.setEmail(data[2]);
			driver.setLicensePlate(data[3]);
			driver.setVehicleType(data[4]);
			driver.setLatitude(Double.parseDouble(data[5]));
			driver.setLongitude(Double.parseDouble(data[6]));
			driver.setAvailable(true);
			driver.setTotalRides(0);
			driver.setRating(5.0);
			driver.setCreatedAt(LocalDateTime.now());
			driver.setUpdatedAt(LocalDateTime.now());
			driverRepository.save(driver);
		}
	}

	private void initializeFareConfig() {
		FareConfig config = new FareConfig();
		config.setBaseFare(50.0);
		config.setRatePerKm(12.0);
		config.setPeakHourMultiplier(1.5);
		config.setMaxPassengers(4);
		config.setMaxServiceRadius(50.0);
		config.setMinFare(50.0);
		fareConfigRepository.save(config);
	}
}

