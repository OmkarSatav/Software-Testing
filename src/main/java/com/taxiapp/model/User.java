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
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String email;

	private String password;

	private String firstName;

	private String lastName;

	private String phoneNumber;

	private String role;

	private Boolean active;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private Integer totalBookings;

}

