package com.sparta.spartaeats.address.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequestDto {
    private String zip;
    private String local;
    private String address;
    private String address2;
    private String contact;
    private Character useYn; // 선택적이므로, 필요 시 클라이언트에서 제공
}