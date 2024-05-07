package com.niziolekp.bookingservice.service;

import com.niziolekp.bookingservice.dto.BookingItemsDto;
import com.niziolekp.bookingservice.event.BookingPlacedEvent;
import com.niziolekp.bookingservice.model.Booking;
import com.niziolekp.bookingservice.model.BookingLineItems;
import com.niziolekp.bookingservice.repository.BookingRepository;
import com.niziolekp.bookingservice.dto.InventoryResponse;
import com.niziolekp.bookingservice.dto.BookingRequest;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;
    private final WebClient.Builder webClientBuilder;
    private final ObservationRegistry observationRegistry;
    private final ApplicationEventPublisher applicationEventPublisher;

    public String doBooking(BookingRequest bookingRequest) {
        Booking booking = new Booking();
        booking.setBookingNumber(UUID.randomUUID().toString());

        List<BookingLineItems> bookingLineItems = bookingRequest.getBookingLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        booking.setBookingLineItemsList(bookingLineItems);

        List<String> codes = booking.getBookingLineItemsList().stream()
                .map(BookingLineItems::getCode)
                .toList();

        // Call Inventory Service, and place booking if trip is in
        // stock
        Observation inventoryServiceObservation = Observation.createNotStarted("inventory-service-lookup",
                this.observationRegistry);
        inventoryServiceObservation.lowCardinalityKeyValue("call", "inventory-service");
        return inventoryServiceObservation.observe(() -> {
            InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                    .uri("http://inventory-service/api/inventory",
                            uriBuilder -> uriBuilder.queryParam("code", codes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();

            boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
                    .allMatch(InventoryResponse::isInStock);

            if (allProductsInStock) {
                bookingRepository.save(booking);
                // publish booking Placed Event
                applicationEventPublisher.publishEvent(new BookingPlacedEvent(this, booking.getBookingNumber()));
                return "booking Placed";
            } else {
                throw new IllegalArgumentException("trip is not in stock, please try again later");
            }
        });

    }

    private BookingLineItems mapToDto(BookingItemsDto bookingLineItemsDto) {
        BookingLineItems bookingLineItems = new BookingLineItems();
        bookingLineItems.setPrice(bookingLineItemsDto.getPrice());
        bookingLineItems.setQuantity(bookingLineItemsDto.getQuantity());
        bookingLineItems.setCode(bookingLineItemsDto.getCode());
        return bookingLineItems;
    }
}
