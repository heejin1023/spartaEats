package com.sparta.spartaeats.location.entity;

import com.sparta.spartaeats.entity.TimeStamped;
import com.sparta.spartaeats.location.dto.LocationRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "p_store_location")
@NoArgsConstructor
public class Location extends TimeStamped {
    @Id
    @GeneratedValue
    @Column(name = "location_id", length = 200, nullable = false)
    private UUID id;

    @Column(name = "location_name", length = 200, nullable = false)
    private String locationName;

    @Column(name = "del_yn", nullable = false)
    private Character delYn = 'N';

    @Column(name = "use_yn", nullable = false)
    private Character useYn = 'Y';

    public Location(LocationRequestDto requestDto) {
        this.id = requestDto.getLocationId();
        this.locationName = requestDto.getLocationName();
        this.delYn = requestDto.getDelYn();
        this.useYn = requestDto.getUseYn();

    }
}
