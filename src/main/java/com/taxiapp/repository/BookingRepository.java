package com.taxiapp.repository;

import com.taxiapp.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

	List<Booking> findByUserId(Long userId);

	List<Booking> findByUserIdOrderByCreatedAtDesc(Long userId);

	Optional<Booking> findByIdAndUserId(Long id, Long userId);

	@Query("SELECT b FROM Booking b WHERE b.userId = :userId AND b.status = 'ACTIVE'")
	Optional<Booking> findActiveBooking(@Param("userId") Long userId);

	List<Booking> findByStatus(String status);

	List<Booking> findByDriverId(Long driverId);

}

