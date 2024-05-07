package com.niziolekp.tripservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.niziolekp.tripservice.dto.TripRequest;
import com.niziolekp.tripservice.repository.TripRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TripServiceApplicationTests {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TripRepository tripRepository;

    static {
        mongoDBContainer.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dymDynamicPropertyRegistry) {
        dymDynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    void shouldCreateProduct() throws Exception {
        TripRequest tripRequest = getTripRequest();
        String tripRequestString = objectMapper.writeValueAsString(tripRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/trip")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tripRequestString))
                .andExpect(status().isCreated());
        Assertions.assertEquals(1, tripRepository.findAll().size());
    }

    private TripRequest getTripRequest() {
        return TripRequest.builder()
                .name("croatia")
                .description("trip to croatia")
                .price(BigDecimal.valueOf(1200))
                .build();
    }

}
