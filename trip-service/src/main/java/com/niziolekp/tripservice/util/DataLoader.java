package com.niziolekp.tripservice.util;

import com.niziolekp.tripservice.model.Trip;
import com.niziolekp.tripservice.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final TripRepository tripRepository;

    @Override
    public void run(String... args) throws Exception {
        if (tripRepository.count() < 1) {
            Trip trip = new Trip();
            trip.setName("croatia");
            trip.setDescription("trip to croatia");
            trip.setPrice(BigDecimal.valueOf(1000));
            tripRepository.save(trip);
        }
    }
}
