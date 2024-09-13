package org.example.uberprojectsocketserver.dto;

import lombok.*;

import java.util.Optional;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookingRequestDto {

    private String status;
    private Long bookingId;
    private Optional<Long> driverId;

}
