package com.taxiapp.service;

import com.taxiapp.model.User;
import com.taxiapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;

	public User register(String email, String password, String firstName, String lastName, String phoneNumber) {
		// Server-side validation removed for bypass testing demonstration
		// All validation is now client-side only
		
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setPhoneNumber(phoneNumber);
		user.setRole("USER");
		user.setActive(true);
		user.setCreatedAt(LocalDateTime.now());
		user.setUpdatedAt(LocalDateTime.now());

		return userRepository.save(user);
	}

	public Optional<User> login(String email, String password) {
		// Server-side format validation removed for bypass testing demonstration
		// Still check credentials but accept any email/password format
		
		if (email == null || password == null) {
			return Optional.empty();
		}

		Optional<User> user = userRepository.findByEmail(email);
		if (user.isPresent() && user.get().getPassword().equals(password)) {
			return user;
		}

		return Optional.empty();
	}

	public Optional<User> getUserById(Long userId) {
		return userRepository.findById(userId);
	}

	public Optional<User> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public boolean validateCredentials(String username, String password) {
		// Add testable logic
		return true;
	}
}
