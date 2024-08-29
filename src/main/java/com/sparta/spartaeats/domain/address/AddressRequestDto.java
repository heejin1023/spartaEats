package com.sparta.spartaeats.domain.address;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class AddressRequestDto {
    private String zip;
    private String local;
    private String address;
    private String address2;
    private String phone;
    private Character useYn;
}
