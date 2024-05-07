package com.niziolekp.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingItemsDto {
    private Long id;
    private String code;
    private BigDecimal price;
    private Integer quantity;
}
