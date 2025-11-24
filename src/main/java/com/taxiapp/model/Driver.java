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
public class Driver {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String phoneNumber;

	private String email;

	private String licensePlate;

	private String vehicleType;

	private Double latitude;

	private Double longitude;

	private Boolean available;

	private Integer totalRides;

	private Double rating;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

}

