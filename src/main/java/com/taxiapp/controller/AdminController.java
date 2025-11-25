package com.taxiapp.controller;

import com.taxiapp.model.Booking;
import com.taxiapp.model.Driver;
import com.taxiapp.model.User;
import com.taxiapp.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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
		String userRole = (String) session.getAttribute("userRole");
		if (!adminService.isAdmin(userRole)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(createErrorResponse("Admin access required"));
		}

		List<User> users = adminService.getAllUsers();
		return ResponseEntity.ok(users);
	}

	@GetMapping("/bookings")
	public ResponseEntity<?> getAllBookings(HttpSession session) {
		String userRole = (String) session.getAttribute("userRole");
		if (!adminService.isAdmin(userRole)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(createErrorResponse("Admin access required"));
		}

		List<Booking> bookings = adminService.getAllBookings();
		return ResponseEntity.ok(bookings);
	}

	@GetMapping("/drivers")
	public ResponseEntity<?> getAllDrivers(HttpSession session) {
		String userRole = (String) session.getAttribute("userRole");
		if (!adminService.isAdmin(userRole)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(createErrorResponse("Admin access required"));
		}

		List<Driver> drivers = adminService.getAllDrivers();
		return ResponseEntity.ok(drivers);
	}

	@PostMapping("/users/{userId}/toggle-status")
	public ResponseEntity<?> toggleUserStatus(@PathVariable Long userId, HttpSession session) {
		String userRole = (String) session.getAttribute("userRole");
		if (!adminService.isAdmin(userRole)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(createErrorResponse("Admin access required"));
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
