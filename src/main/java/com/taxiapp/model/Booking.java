package com.taxiapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long userId;

	private Long driverId;

	private String pickupLocation;
	private String dropLocation;

	@Column(nullable = false)
	private Double pickupLatitude;

	@Column(nullable = false)
	private Double pickupLongitude;

	@Column(nullable = false)
	private Double dropLatitude;

	@Column(nullable = false)
	private Double dropLongitude;

	@Column(nullable = false)
	private LocalDateTime pickupTime;

	@Column(nullable = false)
	private Integer passengers;

	@Column(nullable = false)
	private String vehicleType;

	@Column(nullable = false)
	private Double distance;

	@Column(nullable = false)
	private Double fare;

	@Column(nullable = false)
	private String paymentMode;

	@Column(nullable = false)
	private String status;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime completedAt;
	private String cancelReason;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
		if (status == null) {
			status = "PENDING";
		}
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}
}

