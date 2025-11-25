package com.taxiapp.controller;

import com.taxiapp.model.Driver;
import com.taxiapp.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@CrossOrigin(origins = "*")
public class DriverController {

	@Autowired
	private DriverService driverService;

	@GetMapping("/available")
	public ResponseEntity<List<Driver>> getAvailableDrivers() {
		return ResponseEntity.ok(driverService.getAvailableDrivers());
	}

	@GetMapping("/{driverId}")
	public ResponseEntity<?> getDriver(@PathVariable Long driverId) {
		return driverService.getDriverById(driverId)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
}

