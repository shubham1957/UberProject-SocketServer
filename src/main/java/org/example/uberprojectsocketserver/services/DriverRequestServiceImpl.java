package org.example.uberprojectsocketserver.services;

import org.apache.kafka.common.KafkaException;
import org.example.uberprojectentityservice.models.BookingStatus;
import org.example.uberprojectsocketserver.dto.RideRequestDto;
import org.example.uberprojectsocketserver.dto.RideResponseDto;
import org.example.uberprojectsocketserver.dto.UpdateBookingRequestDto;
import org.example.uberprojectsocketserver.producers.KafkaProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DriverRequestServiceImpl implements DriverRequestService{

    private static  final Logger logger = LoggerFactory.getLogger(DriverRequestServiceImpl.class);
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final KafkaProducerService kafkaProducerService;

    public DriverRequestServiceImpl(SimpMessagingTemplate simpMessagingTemplate, KafkaProducerService kafkaProducerService){
        this.simpMessagingTemplate=simpMessagingTemplate;
        this.kafkaProducerService=kafkaProducerService;
    }
    @Override
    public void sendDriversNewRideRequest(RideRequestDto rideRequestDto) {

        logger.info("New ride request has been sent to the driver!");
        // TODO: Ideally, the request should go only to nearby drivers, but for simplicity, it's sent to all drivers.
        simpMessagingTemplate.convertAndSend("/topic/rideRequest", rideRequestDto);

    }

    @Override
    public void handleRideResponse(String driverId, RideResponseDto rideResponseDto) {

        boolean isAccepted = Boolean.TRUE.equals(rideResponseDto.getResponse());
        logger.info("Ride request: {}", isAccepted ? "Accepted" : "Denied");

        UpdateBookingRequestDto requestDto = UpdateBookingRequestDto.builder()
                .driverId(Optional.of(Long.parseLong(driverId)))
                .bookingId(rideResponseDto.getBookingId())
                .status(BookingStatus.SCHEDULED)
                .build();

        logger.info("Driver Id: {}, Booking Id: {}, Status: {}", requestDto.getDriverId(), requestDto.getBookingId(), requestDto.getStatus());

        // Send the response back to the booking service - async
        try {
            kafkaProducerService.publishMessage("update-booking-topic", requestDto);
        } catch (KafkaException e) {
            logger.error("Failed to publish message to Kafka", e);
            throw new RuntimeException("Failed to publish message to Kafka", e);
        }
    }
}
