package com.taxiapp;

import com.taxiapp.model.Booking;
import com.taxiapp.model.Driver;
import com.taxiapp.model.User;
import com.taxiapp.repository.BookingRepository;
import com.taxiapp.repository.DriverRepository;
import com.taxiapp.repository.UserRepository;
import com.taxiapp.service.BookingService;
import com.taxiapp.service.FareService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
@DisplayName("Bypass Testing Suite - Server-Side Validation Tests")
public class BypassTestSuite {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private FareService fareService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private User testUser;
    private Driver testDriver;

    @BeforeEach
    public void setup() {
        bookingRepository.deleteAll();
        userRepository.deleteAll();
        driverRepository.deleteAll();

        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setRole("USER");
        testUser.setActive(true);
        testUser = userRepository.save(testUser);

        testDriver = new Driver();
        testDriver.setName("Test Driver");
        testDriver.setPhoneNumber("1234567890");
        testDriver.setEmail("driver@example.com");
        testDriver.setLicensePlate("MH01AB1234");
        testDriver.setVehicleType("SEDAN");
        testDriver.setLatitude(19.0760);
        testDriver.setLongitude(72.8777);
        testDriver.setAvailable(true);
        testDriver = driverRepository.save(testDriver);
    }

    @Test
    @DisplayName("BYPASS TEST 1: Past Pickup Time Should Be Rejected")
    public void testPastPickupTimeBypass() {
        LocalDateTime pastTime = LocalDateTime.now().minusDays(1);

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> bookingService.createBooking(
                testUser.getId(),
                19.0760, 72.8777,
                19.1136, 72.9083,
                pastTime,
                2,
                "SEDAN",
                "CASH"
        ));

        Assertions.assertTrue(exception.getMessage().contains("Invalid pickup time"));
    }

    @Test
    @DisplayName("BYPASS TEST 2: Excessive Passengers Should Be Rejected")
    public void testExcessivePassengersBypass() {
        LocalDateTime futureTime = LocalDateTime.now().plusHours(2);

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> bookingService.createBooking(
                testUser.getId(),
                19.0760, 72.8777,
                19.1136, 72.9083,
                futureTime,
                25,
                "SEDAN",
                "CASH"
        ));

        Assertions.assertTrue(exception.getMessage().contains("Invalid passenger count"));
    }

    @Test
    @DisplayName("BYPASS TEST 3: Zero Passengers Should Be Rejected")
    public void testZeroPassengersBypass() {
        LocalDateTime futureTime = LocalDateTime.now().plusHours(2);

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> bookingService.createBooking(
                testUser.getId(),
                19.0760, 72.8777,
                19.1136, 72.9083,
                futureTime,
                0,
                "SEDAN",
                "CASH"
        ));

        Assertions.assertTrue(exception.getMessage().contains("Invalid passenger count"));
    }

    @Test
    @DisplayName("BYPASS TEST 4: Negative Passengers Should Be Rejected")
    public void testNegativePassengersBypass() {
        LocalDateTime futureTime = LocalDateTime.now().plusHours(2);

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> bookingService.createBooking(
                testUser.getId(),
                19.0760, 72.8777,
                19.1136, 72.9083,
                futureTime,
                -5,
                "SEDAN",
                "CASH"
        ));

        Assertions.assertTrue(exception.getMessage().contains("Invalid passenger count"));
    }

    @Test
    @DisplayName("BYPASS TEST 5: Location Outside Service Area Should Be Rejected")
    public void testOutsideServiceAreaBypass() {
        LocalDateTime futureTime = LocalDateTime.now().plusHours(2);

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> bookingService.createBooking(
                testUser.getId(),
                19.0760, 72.8777,
                28.6139, 77.2090,
                futureTime,
                2,
                "SEDAN",
                "CASH"
        ));

        Assertions.assertTrue(exception.getMessage().contains("outside service area"));
    }

    @Test
    @DisplayName("BYPASS TEST 6: Multiple Active Bookings Should Be Rejected")
    public void testMultipleActiveBookingsBypass() {
        LocalDateTime futureTime = LocalDateTime.now().plusHours(2);

        bookingService.createBooking(
                testUser.getId(),
                19.0760, 72.8777,
                19.1136, 72.9083,
                futureTime,
                2,
                "SEDAN",
                "CASH"
        );

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> bookingService.createBooking(
                testUser.getId(),
                19.0760, 72.8777,
                19.1136, 72.9083,
                futureTime.plusHours(1),
                2,
                "SEDAN",
                "CASH"
        ));

        Assertions.assertTrue(exception.getMessage().contains("already has an active booking"));
    }

    @Test
    @DisplayName("BYPASS TEST 7: Valid Booking Should Be Accepted")
    public void testValidBooking() {
        LocalDateTime futureTime = LocalDateTime.now().plusHours(2);

        Booking booking = bookingService.createBooking(
                testUser.getId(),
                19.0760, 72.8777,
                19.1136, 72.9083,
                futureTime,
                2,
                "SEDAN",
                "CASH"
        );

        Assertions.assertNotNull(booking);
        Assertions.assertNotNull(booking.getId());
        Assertions.assertEquals("ACTIVE", booking.getStatus());
        Assertions.assertTrue(booking.getFare() > 0);
    }

    @Test
    @DisplayName("BYPASS TEST 8: Passenger Validation - Boundary Test")
    public void testPassengerBoundaryValidation() {
        Assertions.assertFalse(fareService.validatePassengers(0));
        Assertions.assertTrue(fareService.validatePassengers(1));
        Assertions.assertTrue(fareService.validatePassengers(4));
        Assertions.assertFalse(fareService.validatePassengers(5));
        Assertions.assertFalse(fareService.validatePassengers(null));
    }

    @Test
    @DisplayName("BYPASS TEST 9: Distance Validation Test")
    public void testDistanceValidation() {
        Assertions.assertFalse(fareService.validateDistance(-1.0));
        Assertions.assertTrue(fareService.validateDistance(0.0));
        Assertions.assertTrue(fareService.validateDistance(25.0));
        Assertions.assertTrue(fareService.validateDistance(50.0));
        Assertions.assertFalse(fareService.validateDistance(51.0));
        Assertions.assertFalse(fareService.validateDistance(null));
    }

    @Test
    @DisplayName("BYPASS TEST 10: Cancel Non-Existent Booking Should Fail")
    public void testCancelNonExistentBooking() {
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> bookingService.cancelBooking(9999L, testUser.getId(), "Test"));
        Assertions.assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    @DisplayName("BYPASS TEST 11: Cancel Another User's Booking Should Fail")
    public void testCancelOtherUserBooking() {
        LocalDateTime futureTime = LocalDateTime.now().plusHours(2);

        Booking booking = bookingService.createBooking(
                testUser.getId(),
                19.0760, 72.8777,
                19.1136, 72.9083,
                futureTime,
                2,
                "SEDAN",
                "CASH"
        );

        User anotherUser = new User();
        anotherUser.setEmail("another@example.com");
        anotherUser.setPassword("password");
        anotherUser.setFirstName("Another");
        anotherUser.setLastName("User");
        anotherUser.setRole("USER");
        anotherUser.setActive(true);
        anotherUser = userRepository.save(anotherUser);

        Long anotherUserId = anotherUser.getId();

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> bookingService.cancelBooking(booking.getId(), anotherUserId, "Test"));
        Assertions.assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    @DisplayName("BYPASS TEST 12: Cancel Already Cancelled Booking Should Fail")
    public void testCancelAlreadyCancelledBooking() {
        LocalDateTime futureTime = LocalDateTime.now().plusHours(2);

        Booking booking = bookingService.createBooking(
                testUser.getId(),
                19.0760, 72.8777,
                19.1136, 72.9083,
                futureTime,
                2,
                "SEDAN",
                "CASH"
        );

        bookingService.cancelBooking(booking.getId(), testUser.getId(), "First cancel");

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> bookingService.cancelBooking(booking.getId(), testUser.getId(), "Second cancel"));
        Assertions.assertTrue(exception.getMessage().contains("only cancel active"));
    }
}



