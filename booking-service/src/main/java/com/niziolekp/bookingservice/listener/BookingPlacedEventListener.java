package com.niziolekp.bookingservice.listener;

import com.niziolekp.bookingservice.event.BookingPlacedEvent;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingPlacedEventListener {

    private final KafkaTemplate<String, BookingPlacedEvent> kafkaTemplate;
    private final ObservationRegistry observationRegistry;

    @EventListener
    public void handleBookingPlacedEvent(BookingPlacedEvent event) {
        log.info("booking Placed Event Received, Sending bookingPlacedEvent to notificationTopic: {}", event.getBookingNumber());

        // Create Observation for Kafka Template
        try {
            Observation.createNotStarted("notification-topic", this.observationRegistry).observeChecked(() -> {
                CompletableFuture<SendResult<String, BookingPlacedEvent>> future = kafkaTemplate.send("notificationTopic",
                        new BookingPlacedEvent(event.getBookingNumber()));
                return future.handle((result, throwable) -> CompletableFuture.completedFuture(result));
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error while sending message to Kafka", e);
        }
    }
}
