package com.taxiapp.controller;

import com.taxiapp.model.Booking;
import com.taxiapp.model.Driver;
import com.taxiapp.model.User;
import com.taxiapp.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

	@Autowired
	private AdminService adminService;

	@GetMapping("/users")
	public ResponseEntity<?> getAllUsers(HttpSession session) {
		// Server-side role validation removed for bypass testing demonstration
		// Allow any authenticated user to access admin endpoints
		Long userId = (Long) session.getAttribute("userId");
		if (userId == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(createErrorResponse("User not authenticated"));
		}

		List<User> users = adminService.getAllUsers();
		return ResponseEntity.ok(users);
	}

	@GetMapping("/bookings")
	public ResponseEntity<?> getAllBookings(HttpSession session) {
		// Server-side role validation removed for bypass testing demonstration
		// Allow any authenticated user to access admin endpoints
		Long userId = (Long) session.getAttribute("userId");
		if (userId == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(createErrorResponse("User not authenticated"));
		}

		List<Booking> bookings = adminService.getAllBookings();
		return ResponseEntity.ok(bookings);
	}

	@GetMapping("/drivers")
	public ResponseEntity<?> getAllDrivers(HttpSession session) {
		// Server-side role validation removed for bypass testing demonstration
		// Allow any authenticated user to access admin endpoints
		Long userId = (Long) session.getAttribute("userId");
		if (userId == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(createErrorResponse("User not authenticated"));
		}

		List<Driver> drivers = adminService.getAllDrivers();
		return ResponseEntity.ok(drivers);
	}

	@PostMapping("/users/{userId}/toggle-status")
	public ResponseEntity<?> toggleUserStatus(@PathVariable Long userId, HttpSession session) {
		// Server-side role validation removed for bypass testing demonstration
		// Allow any authenticated user to access admin endpoints
		Long sessionUserId = (Long) session.getAttribute("userId");
		if (sessionUserId == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(createErrorResponse("User not authenticated"));
		}

		boolean result = adminService.toggleUserStatus(userId);
		Map<String, Object> response = new HashMap<>();
		response.put("success", result);
		return ResponseEntity.ok(response);
	}

	private Map<String, Object> createErrorResponse(String message) {
		Map<String, Object> error = new HashMap<>();
		error.put("success", false);
		error.put("message", message);
		return error;
	}
}
