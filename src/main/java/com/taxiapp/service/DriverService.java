package com.taxiapp.service;

import com.taxiapp.model.Driver;
import com.taxiapp.repository.DriverRepository;
import com.taxiapp.util.DistanceCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DriverService {
    @Autowired
    private DriverRepository driverRepository;

    public List<Driver> getAvailableDrivers() {
        return driverRepository.findByAvailable(true);
    }

    public Optional<Driver> findNearestDriver(double pickupLat, double pickupLon, String vehicleType) {
        List<Driver> availableDrivers = driverRepository.findByVehicleType(vehicleType);
        availableDrivers.retainAll(getAvailableDrivers());

        if (availableDrivers.isEmpty()) {
            return Optional.empty();
        }

        Driver nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (Driver driver : availableDrivers) {
            double distance = DistanceCalculator.calculateDistance(
                    pickupLat, pickupLon,
                    driver.getLatitude(), driver.getLongitude());

            if (distance < minDistance) {
                minDistance = distance;
                nearest = driver;
            }
        }

        return Optional.ofNullable(nearest);
    }

    public Optional<Driver> getDriverById(Long driverId) {
        return driverRepository.findById(driverId);
    }

    public void updateDriverAvailability(Long driverId, Boolean available) {
        Optional<Driver> driver = driverRepository.findById(driverId);
        if (driver.isPresent()) {
            driver.get().setAvailable(available);
            driver.get().setUpdatedAt(LocalDateTime.now());
            driverRepository.save(driver.get());
        }
    }

    public void updateDriverLocation(Long driverId, Double latitude, Double longitude) {
        Optional<Driver> driver = driverRepository.findById(driverId);
        if (driver.isPresent()) {
            driver.get().setLatitude(latitude);
            driver.get().setLongitude(longitude);
            driver.get().setUpdatedAt(LocalDateTime.now());
            driverRepository.save(driver.get());
        }
    }

    public Driver registerDriver(String name, String phoneNumber, String email,
                                 String licensePlate, String vehicleType) {
        if (driverRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        Driver driver = new Driver();
        driver.setName(name);
        driver.setPhoneNumber(phoneNumber);
        driver.setEmail(email);
        driver.setLicensePlate(licensePlate);
        driver.setVehicleType(vehicleType);
        driver.setAvailable(true);
        driver.setTotalRides(0);
        driver.setRating(5.0);
        driver.setCreatedAt(LocalDateTime.now());
        driver.setUpdatedAt(LocalDateTime.now());

        return driverRepository.save(driver);
    }

    public Driver assignDriver(Long driverId) {
        Driver driver = driverRepository.findById(driverId).orElse(null);
        if (driver != null && driver.isAvailable()) {
            driver.setAvailable(false);
            driver.setUpdatedAt(LocalDateTime.now());
            return driverRepository.save(driver);
        }
        return driver;
    }
}
