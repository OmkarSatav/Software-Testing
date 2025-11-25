package com.taxiapp.controller;

import com.taxiapp.model.User;
import com.taxiapp.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
		try {
			User user = authService.register(
					request.getEmail(),
					request.getPassword(),
					request.getFirstName(),
					request.getLastName(),
					request.getPhoneNumber()
			);

			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("userId", user.getId());
			response.put("email", user.getEmail());
			response.put("role", user.getRole());

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
			error.put("success", false);
			error.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpSession session) {
		Optional<User> user = authService.login(request.getEmail(), request.getPassword());

		if (user.isPresent()) {
			session.setAttribute("userId", user.get().getId());
			session.setAttribute("userRole", user.get().getRole());

			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("userId", user.get().getId());
			response.put("email", user.get().getEmail());
			response.put("role", user.get().getRole());
			response.put("firstName", user.get().getFirstName());

			return ResponseEntity.ok(response);
		}

		Map<String, Object> error = new HashMap<>();
		error.put("success", false);
		error.put("message", "Invalid credentials");
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpSession session) {
		session.invalidate();
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", "Logged out successfully");
		return ResponseEntity.ok(response);
	}

	@GetMapping("/session")
	public ResponseEntity<?> getSession(HttpSession session) {
		Long userId = (Long) session.getAttribute("userId");
		String userRole = (String) session.getAttribute("userRole");

		if (userId != null) {
			Map<String, Object> response = new HashMap<>();
			response.put("authenticated", true);
			response.put("userId", userId);
			response.put("role", userRole);
			return ResponseEntity.ok(response);
		}

		Map<String, Object> response = new HashMap<>();
		response.put("authenticated", false);
		return ResponseEntity.ok(response);
	}

	static class RegisterRequest {
		private String email;
		private String password;
		private String firstName;
		private String lastName;
		private String phoneNumber;

		public String getEmail() { return email; }
		public void setEmail(String email) { this.email = email; }
		public String getPassword() { return password; }
		public void setPassword(String password) { this.password = password; }
		public String getFirstName() { return firstName; }
		public void setFirstName(String firstName) { this.firstName = firstName; }
		public String getLastName() { return lastName; }
		public void setLastName(String lastName) { this.lastName = lastName; }
		public String getPhoneNumber() { return phoneNumber; }
		public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
	}

	static class LoginRequest {
		private String email;
		private String password;

		public String getEmail() { return email; }
		public void setEmail(String email) { this.email = email; }
		public String getPassword() { return password; }
		public void setPassword(String password) { this.password = password; }
	}
}

