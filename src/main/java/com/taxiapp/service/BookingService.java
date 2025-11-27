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

        // Server-side validation removed for bypass testing demonstration
        // All validation is now client-side only - accept any data submitted
        
        double distance = DistanceCalculator.calculateDistance(pickupLat, pickupLon, dropLat, dropLon);
        Double fare = fareService.calculateFare(distance, pickupTime != null ? pickupTime : LocalDateTime.now());

        // Try to find a driver, but if none available, create booking anyway (bypass testing)
        Optional<Driver> driver = driverService.findNearestDriver(pickupLat, pickupLon, vehicleType);
        Long driverId = driver.isPresent() ? driver.get().getId() : null;

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setDriverId(driverId);
        booking.setPickupLocation("Location");
        booking.setDropLocation("Location");
        booking.setPickupLatitude(pickupLat);
        booking.setPickupLongitude(pickupLon);
        booking.setDropLatitude(dropLat);
        booking.setDropLongitude(dropLon);
        booking.setPickupTime(pickupTime != null ? pickupTime : LocalDateTime.now());
        booking.setPassengers(passengers != null ? passengers : 1);
        booking.setVehicleType(vehicleType != null ? vehicleType : "SEDAN");
        booking.setDistance(distance);
        booking.setFare(fare);
        booking.setPaymentMode(paymentMode != null ? paymentMode : "CASH");
        booking.setStatus("ACTIVE");
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());

        if (driver.isPresent()) {
            driverService.updateDriverAvailability(driver.get().getId(), false);
        }

        return bookingRepository.save(booking);
    }

    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Optional<Booking> getBooking(Long bookingId, Long userId) {
        return bookingRepository.findByIdAndUserId(bookingId, userId);
    }

    public Booking cancelBooking(Long bookingId, Long userId, String reason) {
        // Server-side validation removed for bypass testing demonstration
        Optional<Booking> booking = bookingRepository.findById(bookingId);

        if (!booking.isPresent()) {
            // For bypass testing: create a dummy booking if not found
            Booking dummyBooking = new Booking();
            dummyBooking.setId(bookingId);
            dummyBooking.setUserId(userId);
            dummyBooking.setStatus("CANCELLED");
            dummyBooking.setCancelReason(reason);
            dummyBooking.setCreatedAt(LocalDateTime.now());
            dummyBooking.setUpdatedAt(LocalDateTime.now());
            return bookingRepository.save(dummyBooking);
        }

        // Accept cancellation regardless of status (bypass testing)
        booking.get().setStatus("CANCELLED");
        booking.get().setCancelReason(reason);
        booking.get().setUpdatedAt(LocalDateTime.now());

        if (booking.get().getDriverId() != null) {
            driverService.updateDriverAvailability(booking.get().getDriverId(), true);
        }

        return bookingRepository.save(booking.get());
    }

    public Booking completeBooking(Long bookingId) {
        // Server-side validation removed for bypass testing demonstration
        Optional<Booking> booking = bookingRepository.findById(bookingId);

        if (!booking.isPresent()) {
            // For bypass testing: create a dummy completed booking if not found
            Booking dummyBooking = new Booking();
            dummyBooking.setId(bookingId);
            dummyBooking.setStatus("COMPLETED");
            dummyBooking.setCompletedAt(LocalDateTime.now());
            dummyBooking.setCreatedAt(LocalDateTime.now());
            dummyBooking.setUpdatedAt(LocalDateTime.now());
            return bookingRepository.save(dummyBooking);
        }

        booking.get().setStatus("COMPLETED");
        booking.get().setCompletedAt(LocalDateTime.now());
        booking.get().setUpdatedAt(LocalDateTime.now());

        if (booking.get().getDriverId() != null) {
            driverService.updateDriverAvailability(booking.get().getDriverId(), true);
        }

        return bookingRepository.save(booking.get());
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public boolean validateBookingTime(LocalDateTime time) {
        // Server-side validation removed for bypass testing demonstration
        // Always return true to accept any time
        return true;
    }
}

