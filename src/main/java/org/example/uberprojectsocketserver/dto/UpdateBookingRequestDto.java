package org.example.uberprojectsocketserver.dto;

import lombok.*;
import org.example.uberprojectentityservice.models.BookingStatus;

import java.util.Optional;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookingRequestDto {

    private BookingStatus status;
    private Long bookingId;
    private Optional<Long> driverId;

}
