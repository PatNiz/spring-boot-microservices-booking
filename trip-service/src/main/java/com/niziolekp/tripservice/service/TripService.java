package com.niziolekp.tripservice.service;

import com.niziolekp.tripservice.dto.TripResponse;
import com.niziolekp.tripservice.dto.TripRequest;
import com.niziolekp.tripservice.model.Trip;
import com.niziolekp.tripservice.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TripService {

    private final TripRepository tripRepository;

    public void createTrip(TripRequest tripRequest) {
        Trip trip = Trip.builder()
                .name(tripRequest.getName())
                .description(tripRequest.getDescription())
                .price(tripRequest.getPrice())
                .build();

        tripRepository.save(trip);
        log.info("Trip {} is saved", trip.getId());
    }

    public List<TripResponse> getAllTrips() {
        List<Trip> trips = tripRepository.findAll();
        return trips.stream().map(this::mapToTripResponse).toList();
    }

    private TripResponse mapToTripResponse(Trip trip) {
        return TripResponse.builder()
                .id(trip.getId())
                .name(trip.getName())
                .description(trip.getDescription())
                .price(trip.getPrice())
                .build();
    }
}
