package com.sparta.spartaeats.address.dto;

import com.sparta.spartaeats.address.domain.Address;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class AddressResponseDto {
    private UUID delvrUuid;
    private Long userIdx;
    private String zip;
    private String local;
    private String address;
    private String address2;
    private String content;
    private Character useYn;
    private Character delYn;

    public AddressResponseDto(Address savedAddress) {
        this.delvrUuid = savedAddress.getId();
        this.userIdx = 1L;//savedAddress.getUser().getId(); // User 엔티티에서 ID를 가져온다고 가정
        this.zip = savedAddress.getZip();
        this.local = savedAddress.getLocal();
        this.address = savedAddress.getAddress();
        this.address2 = savedAddress.getAddress2();
        this.content = savedAddress.getContact();
        this.useYn = savedAddress.getUseYn();
        this.delYn = savedAddress.getDelYn();
    }

}
