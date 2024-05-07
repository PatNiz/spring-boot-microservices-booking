package com.niziolekp.tripservice.controller;

import com.niziolekp.tripservice.dto.TripResponse;
import com.niziolekp.tripservice.dto.TripRequest;
import com.niziolekp.tripservice.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trip")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody TripRequest tripRequest) {
        tripService.createTrip(tripRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TripResponse> getAllProducts() {
        return tripService.getAllTrips();
    }

}
