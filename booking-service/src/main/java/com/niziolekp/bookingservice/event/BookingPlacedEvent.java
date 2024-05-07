package com.niziolekp.bookingservice.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;


@Getter
@Setter
public class BookingPlacedEvent extends ApplicationEvent {
    private String bookingNumber;

    public BookingPlacedEvent(Object source, String bookingNumber) {
        super(source);
        this.bookingNumber = bookingNumber;
    }

    public BookingPlacedEvent(String bookingNumber) {
        super(bookingNumber);
        this.bookingNumber = bookingNumber;
    }
}
