package org.example.uberprojectsocketserver.controller;

import org.example.uberprojectsocketserver.dto.RideRequestDto;
import org.example.uberprojectsocketserver.dto.RideResponseDto;
import org.example.uberprojectsocketserver.services.DriverRequestServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/socket")
public class DriverRequestController {

    private final DriverRequestServiceImpl driverRequestService;

    public DriverRequestController(DriverRequestServiceImpl driverRequestService) {
        this.driverRequestService=driverRequestService;
    }

    @PostMapping("/newride")
    public ResponseEntity<Boolean> raiseRideRequest(@RequestBody RideRequestDto requestDto){
        driverRequestService.sendDriversNewRideRequest(requestDto);
        return  new ResponseEntity<>(Boolean.TRUE,HttpStatus.OK);
    }

    /*
        Multiple client should not be able to call the below method. So make it synchronized,
        because If two different requests came for the same userId, both threads might try to update the booking status concurrently,
        leading to potential inconsistencies or conflicts in the booking data
     */
    @MessageMapping("/rideResponse/{driverId}")
    public synchronized void rideResponseHandler(@DestinationVariable String driverId, RideResponseDto rideResponseDto){
        driverRequestService.handleRideResponse(driverId, rideResponseDto);
    }
}
