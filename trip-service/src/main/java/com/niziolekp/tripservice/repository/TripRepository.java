package com.niziolekp.tripservice.repository;

import com.niziolekp.tripservice.model.Trip;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TripRepository extends MongoRepository<Trip, String> {
}
