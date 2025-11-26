package com.taxiapp.controller;

import com.taxiapp.model.Booking;
import com.taxiapp.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

	@Autowired
	private BookingService bookingService;

	@PostMapping("/create")
	public ResponseEntity<?> createBooking(@RequestBody BookingRequest request, HttpSession session) {
		try {
			Long userId = (Long) session.getAttribute("userId");
			if (userId == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(createErrorResponse("User not authenticated"));
			}

			LocalDateTime pickupTime = LocalDateTime.parse(request.getPickupTime(),
					DateTimeFormatter.ISO_LOCAL_DATE_TIME);

			Booking booking = bookingService.createBooking(
					userId,
					request.getPickupLatitude(),
					request.getPickupLongitude(),
					request.getDropLatitude(),
					request.getDropLongitude(),
					pickupTime,
					request.getPassengers(),
					request.getVehicleType(),
					request.getPaymentMode()
			);

			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("bookingId", booking.getId());
			response.put("fare", booking.getFare());
			response.put("distance", booking.getDistance());
			response.put("driverId", booking.getDriverId());
			response.put("status", booking.getStatus());

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(createErrorResponse(e.getMessage()));
		}
	}

	@GetMapping("/user")
	public ResponseEntity<?> getUserBookings(HttpSession session) {
		Long userId = (Long) session.getAttribute("userId");
		if (userId == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(createErrorResponse("User not authenticated"));
		}

		List<Booking> bookings = bookingService.getUserBookings(userId);
		return ResponseEntity.ok(bookings);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<?> getBooking(@PathVariable Long bookingId, HttpSession session) {
		Long userId = (Long) session.getAttribute("userId");
		if (userId == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(createErrorResponse("User not authenticated"));
		}

		Optional<Booking> booking = bookingService.getBooking(bookingId, userId);
		if (booking.isPresent()) {
			return ResponseEntity.ok(booking.get());
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(createErrorResponse("Booking not found"));
	}

	@PostMapping("/{bookingId}/cancel")
	public ResponseEntity<?> cancelBooking(@PathVariable Long bookingId,
	                                       @RequestBody CancelRequest request,
	                                       HttpSession session) {
		try {
			Long userId = (Long) session.getAttribute("userId");
			if (userId == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(createErrorResponse("User not authenticated"));
			}

			Booking booking = bookingService.cancelBooking(bookingId, userId, request.getReason());

			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("bookingId", booking.getId());
			response.put("status", booking.getStatus());

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(createErrorResponse(e.getMessage()));
		}
	}

	private Map<String, Object> createErrorResponse(String message) {
		Map<String, Object> error = new HashMap<>();
		error.put("success", false);
		error.put("message", message);
		return error;
	}

	static class BookingRequest {
		private Double pickupLatitude;
		private Double pickupLongitude;
		private Double dropLatitude;
		private Double dropLongitude;
		private String pickupTime;
		private Integer passengers;
		private String vehicleType;
		private String paymentMode;

		public Double getPickupLatitude() { return pickupLatitude; }
		public void setPickupLatitude(Double pickupLatitude) { this.pickupLatitude = pickupLatitude; }
		public Double getPickupLongitude() { return pickupLongitude; }
		public void setPickupLongitude(Double pickupLongitude) { this.pickupLongitude = pickupLongitude; }
		public Double getDropLatitude() { return dropLatitude; }
		public void setDropLatitude(Double dropLatitude) { this.dropLatitude = dropLatitude; }
		public Double getDropLongitude() { return dropLongitude; }
		public void setDropLongitude(Double dropLongitude) { this.dropLongitude = dropLongitude; }
		public String getPickupTime() { return pickupTime; }
		public void setPickupTime(String pickupTime) { this.pickupTime = pickupTime; }
		public Integer getPassengers() { return passengers; }
		public void setPassengers(Integer passengers) { this.passengers = passengers; }
		public String getVehicleType() { return vehicleType; }
		public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
		public String getPaymentMode() { return paymentMode; }
		public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }
	}

	static class CancelRequest {
		private String reason;

		public String getReason() { return reason; }
		public void setReason(String reason) { this.reason = reason; }
	}
}

