package com.taxiapp;

import com.taxiapp.model.Booking;
import com.taxiapp.model.Driver;
import com.taxiapp.repository.BookingRepository;
import com.taxiapp.repository.DriverRepository;
import com.taxiapp.service.BookingService;
import com.taxiapp.service.DriverService;
import com.taxiapp.service.FareService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

public class BookingServiceUnitTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private DriverService driverService;

    @Mock
    private FareService fareService;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createValidBooking() {
        when(fareService.validatePassengers(2)).thenReturn(true);
        when(fareService.validateDistance(anyDouble())).thenReturn(true);
        when(fareService.calculateFare(anyDouble(), any(LocalDateTime.class))).thenReturn(150.0);

        Driver mockDriver = new Driver();
        mockDriver.setId(1L);
        mockDriver.setAvailable(true);
        when(driverService.findNearestDriver(anyDouble(), anyDouble(), eq("sedan"))).thenReturn(Optional.of(mockDriver));

        when(bookingRepository.save(any(Booking.class))).thenAnswer(i -> i.getArgument(0));

        Booking b = bookingService.createBooking(1L, 0.0, 0.0, 0.01, 0.01, LocalDateTime.now().plusHours(1), 2, "sedan", "card");

        assertNotNull(b);
        assertEquals(2, b.getPassengers());
        verify(driverService).updateDriverAvailability(1L, false);
    }

    @Test
    void rejectPastPickupTime() {
        LocalDateTime past = LocalDateTime.now().minusDays(1);
        assertThrows(RuntimeException.class, () -> bookingService.createBooking(1L, 0.0, 0.0, 0.01, 0.01, past, 2, "sedan", "card"));
    }

}

