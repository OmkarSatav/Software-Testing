package com.taxiapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId;

	private Long driverId;

	private String pickupLocation;

	private String dropLocation;

	private Double pickupLatitude;

	private Double pickupLongitude;

	private Double dropLatitude;

	private Double dropLongitude;

	private LocalDateTime pickupTime;

	private LocalDateTime dropTime;

	private Integer passengers;

	private String vehicleType;

	private Double distance;

	private Double fare;

	private String paymentMode;

	private String status;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private LocalDateTime completedAt;

	private String cancelReason;

}

