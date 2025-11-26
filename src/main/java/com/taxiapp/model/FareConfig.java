package com.taxiapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "fare_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FareConfig {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Double baseFare = 50.0;

	@Column(nullable = false)
	private Double ratePerKm = 12.0;

	@Column(nullable = false)
	private Double peakHourMultiplier = 1.5;

	@Column(nullable = false)
	private Integer maxPassengers = 4;

	@Column(nullable = false)
	private Double maxServiceRadius = 50.0;

	@Column(nullable = false)
	private Double minFare = 50.0;
}

