package com.taxiapp.service;


import com.taxiapp.model.Booking;
import com.taxiapp.model.Driver;
import com.taxiapp.repository.BookingRepository;
import com.taxiapp.util.DistanceCalculator;
import com.taxiapp.util.TimeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private DriverService driverService;

    @Autowired
    private FareService fareService;

    public Booking createBooking(Long userId, double pickupLat, double pickupLon,
                                 double dropLat, double dropLon, LocalDateTime pickupTime,
                                 Integer passengers, String vehicleType, String paymentMode) {

        if (!TimeValidator.isValidPickupTime(pickupTime)) {
            throw new RuntimeException("Invalid pickup time - must be in future");
        }

        if (!fareService.validatePassengers(passengers)) {
            throw new RuntimeException("Invalid passenger count - must be 1 to 4");
        }

        Optional<Booking> activeBooking = bookingRepository.findActiveBooking(userId);
        if (activeBooking.isPresent()) {
            throw new RuntimeException("User already has an active booking");
        }

        double distance = DistanceCalculator.calculateDistance(pickupLat, pickupLon, dropLat, dropLon);

        if (!fareService.validateDistance(distance)) {
            throw new RuntimeException("Pickup location outside service area");
        }

        Double fare = fareService.calculateFare(distance, pickupTime);

        Optional<Driver> driver = driverService.findNearestDriver(pickupLat, pickupLon, vehicleType);
        if (!driver.isPresent()) {
            throw new RuntimeException("No drivers available");
        }

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setDriverId(driver.get().getId());
        booking.setPickupLocation("Location");
        booking.setDropLocation("Location");
        booking.setPickupLatitude(pickupLat);
        booking.setPickupLongitude(pickupLon);
        booking.setDropLatitude(dropLat);
        booking.setDropLongitude(dropLon);
        booking.setPickupTime(pickupTime);
        booking.setPassengers(passengers);
        booking.setVehicleType(vehicleType);
        booking.setDistance(distance);
        booking.setFare(fare);
        booking.setPaymentMode(paymentMode);
        booking.setStatus("ACTIVE");
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());

        driverService.updateDriverAvailability(driver.get().getId(), false);

        return bookingRepository.save(booking);
    }

    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Optional<Booking> getBooking(Long bookingId, Long userId) {
        return bookingRepository.findByIdAndUserId(bookingId, userId);
    }

    public Booking cancelBooking(Long bookingId, Long userId, String reason) {
        Optional<Booking> booking = bookingRepository.findByIdAndUserId(bookingId, userId);

        if (!booking.isPresent()) {
            throw new RuntimeException("Booking not found");
        }

        if (!"ACTIVE".equals(booking.get().getStatus())) {
            throw new RuntimeException("Can only cancel active bookings");
        }

        booking.get().setStatus("CANCELLED");
        booking.get().setCancelReason(reason);
        booking.get().setUpdatedAt(LocalDateTime.now());

        driverService.updateDriverAvailability(booking.get().getDriverId(), true);

        return bookingRepository.save(booking.get());
    }

    public Booking completeBooking(Long bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);

        if (!booking.isPresent()) {
            throw new RuntimeException("Booking not found");
        }

        booking.get().setStatus("COMPLETED");
        booking.get().setCompletedAt(LocalDateTime.now());
        booking.get().setUpdatedAt(LocalDateTime.now());

        driverService.updateDriverAvailability(booking.get().getDriverId(), true);

        return bookingRepository.save(booking.get());
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public boolean validateBookingTime(LocalDateTime time) {
        return time != null && time.isAfter(LocalDateTime.now());
    }
}

