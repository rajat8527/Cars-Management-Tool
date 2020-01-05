package com.demo.cars.repository;

import com.demo.cars.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface Car repository.
 *
 * @author Rajat Meena - rajat8527.github.io
 */
public interface CarRepository extends JpaRepository<Car, Long> {
  
}
