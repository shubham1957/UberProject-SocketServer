package org.example.uberprojectsocketserver.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RideResponseDto {
    public Boolean response;
    public Long bookingId;
}
