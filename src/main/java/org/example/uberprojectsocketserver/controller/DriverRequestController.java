package org.example.uberprojectsocketserver.controller;

import org.example.uberprojectsocketserver.dto.RideRequestDto;
import org.example.uberprojectsocketserver.dto.RideResponseDto;
import org.example.uberprojectsocketserver.dto.UpdateBookingRequestDto;
import org.example.uberprojectsocketserver.dto.UpdateBookingResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Controller
@RequestMapping("/api/socket")
public class DriverRequestController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RestTemplate restTemplate;

    public DriverRequestController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.restTemplate=new RestTemplate();
    }

    @PostMapping("/newride")
    public ResponseEntity<Boolean> raiseRideRequest(@RequestBody RideRequestDto requestDto){
        sendDriversNewRideRequest(requestDto);
        return  new ResponseEntity<>(Boolean.TRUE,HttpStatus.OK);
    }

    public void sendDriversNewRideRequest(RideRequestDto requestDto){
        System.out.println("New Ride request has been sent to the driver !");
        // TODO : Ideally the request should go only to the nearby drivers, but for simplicity sending it to all drivers
        simpMessagingTemplate.convertAndSend("/topic/rideRequest", requestDto);
    }

    /*
        Multiple client should not be able to call the below method. So make it synchronized,
        because If two different requests came for the same userId, both threads might try to update the booking status concurrently,
        leading to potential inconsistencies or conflicts in the booking data
     */
    @MessageMapping("/rideResponse/{userId}")
    public synchronized void rideResponseHandler(@DestinationVariable String userId, RideResponseDto rideResponseDto){

        // Boolean.TRUE.equals(rideResponseDto.response) - handles null pointer exception and safely return false
        System.out.println("Ride request : " + (Boolean.TRUE.equals(rideResponseDto.response) ? "Accepted " : "Denied"));
        System.out.println("Driver id : "+userId);

        UpdateBookingRequestDto requestDto = UpdateBookingRequestDto.builder()
                .driverId(Optional.of(Long.parseLong(userId)))
                .status("SCHEDULED")
                .build();

        ResponseEntity<UpdateBookingResponseDto> result = this.restTemplate.postForEntity("http://localhost:7477/api/v1/booking/" + rideResponseDto.bookingId ,requestDto, UpdateBookingResponseDto.class);
        System.out.println(result.getStatusCode());

    }
}
