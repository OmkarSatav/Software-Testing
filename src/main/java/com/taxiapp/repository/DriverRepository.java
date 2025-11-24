package com.taxiapp.repository;

import com.taxiapp.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

	List<Driver> findByAvailable(Boolean available);

	Optional<Driver> findByEmail(String email);

	Optional<Driver> findByPhoneNumber(String phoneNumber);

	List<Driver> findByVehicleType(String vehicleType);

}

