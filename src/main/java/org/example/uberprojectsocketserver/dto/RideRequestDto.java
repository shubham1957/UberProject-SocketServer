package org.example.uberprojectsocketserver.dto;

import lombok.*;
import org.example.uberprojectsocketserver.models.ExactLocation;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RideRequestDto {

    private Long passengerId;
    private ExactLocation startLocation;
    private ExactLocation endLocation;
    private Long bookingId;
    private List<Long> driverIds;
}
