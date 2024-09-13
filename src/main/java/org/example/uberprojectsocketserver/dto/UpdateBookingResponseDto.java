package org.example.uberprojectsocketserver.dto;

import lombok.*;
import org.example.uberprojectentityservice.models.Driver;
import org.example.uberprojectentityservice.models.BookingStatus;

import java.util.Optional;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookingResponseDto {

    private Long bookingId;
    private Optional<Driver> driver;
    private BookingStatus status;
}
