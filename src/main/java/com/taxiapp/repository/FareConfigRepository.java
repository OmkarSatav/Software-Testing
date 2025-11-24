package com.taxiapp.repository;

import com.taxiapp.model.FareConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FareConfigRepository extends JpaRepository<FareConfig, Long> {

}

