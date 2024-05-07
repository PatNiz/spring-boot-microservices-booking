package com.niziolekp.bookingservice.controller;

import com.niziolekp.bookingservice.dto.BookingRequest;
import com.niziolekp.bookingservice.service.BookingService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/booki")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "inventory")
    @Retry(name = "inventory")
    public CompletableFuture<String> booking(@RequestBody BookingRequest bookingRequest) {
        log.info("booking");
        return CompletableFuture.supplyAsync(() -> bookingService.doBooking(bookingRequest));
    }

    public CompletableFuture<String> fallbackMethod(BookingRequest bookingRequest, RuntimeException runtimeException) {
        log.info("Cannot do booking Executing Fallback logic");
        return CompletableFuture.supplyAsync(() -> "Oops! Something went wrong, please booking after some time!");
    }
}
