package org.example.uberprojectsocketserver.services;

import org.example.uberprojectsocketserver.dto.RideRequestDto;
import org.example.uberprojectsocketserver.dto.RideResponseDto;

public interface DriverRequestService {
    void sendDriversNewRideRequest(RideRequestDto rideRequestDto);

    void handleRideResponse(String userId, RideResponseDto rideResponseDto);
}
