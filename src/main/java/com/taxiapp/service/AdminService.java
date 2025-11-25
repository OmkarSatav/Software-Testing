package com.taxiapp.service;

import com.taxiapp.model.Booking;
import com.taxiapp.model.Driver;
import com.taxiapp.model.User;
import com.taxiapp.repository.BookingRepository;
import com.taxiapp.repository.DriverRepository;
import com.taxiapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private DriverRepository driverRepository;

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public List<Booking> getAllBookings() {
		return bookingRepository.findAll();
	}

	public List<Driver> getAllDrivers() {
		return driverRepository.findAll();
	}

	public boolean toggleUserStatus(Long userId) {
		User user = userRepository.findById(userId).orElse(null);
		if (user != null) {
			user.setActive(!user.getActive());
			userRepository.save(user);
			return true;
		}
		return false;
	}

	public boolean isAdmin(String role) {
		return "ADMIN".equalsIgnoreCase(role);
	}
}
