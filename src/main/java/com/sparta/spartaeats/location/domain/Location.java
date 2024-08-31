package com.sparta.spartaeats.location.domain;

import com.sparta.spartaeats.common.util.TimeStamped;
import com.sparta.spartaeats.location.dto.LocationRequestDto;
import com.sparta.spartaeats.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;

    @Column(name = "location_name", length = 200, nullable = false)
    private String locationName;

    @Column(name = "del_yn", nullable = false)
    private Character delYn = 'N';

    @Column(name = "use_yn", nullable = false)
    private Character useYn = 'Y';

    private Long deletedBy;
    private LocalDateTime deletedAt;

    public Location(LocationRequestDto requestDto) {
        this.id = requestDto.getLocationId();
        this.locationName = requestDto.getLocationName();
        this.delYn = requestDto.getDelYn();
        this.useYn = requestDto.getUseYn();

    }

    public void update(LocationRequestDto requestDto, Long userIdx) {
        this.locationName = requestDto.getLocationName();
        this.useYn = requestDto.getUseYn() != null ? requestDto.getUseYn() : this.useYn;
    }

    public void delete(Long deletedBy) {
        this.delYn = 'Y';
        this.deletedBy = deletedBy;
        this.deletedAt = LocalDateTime.now();
    }
}
