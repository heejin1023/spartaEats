package com.sparta.spartaeats.domain.address;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    // 배송지 등록
    @PostMapping
    public AddressResponseDto createAddress(@RequestBody AddressRequestDto addressRequestDto,
                                            @RequestHeader(value = "X-User-Id", required = true) String userId) {
        return addressService.createAddress(addressRequestDto, userId);
    }

    // 배송지 수정
    @PatchMapping("/{addressId}")
    public AddressResponseDto updateAddress(@PathVariable String addressId,
                                            @RequestBody AddressRequestDto addressRequestDto,
                                            @RequestHeader(value = "X-User-Id", required = true) String userId) {
        return addressService.updateAddress(addressId, addressRequestDto, userId);
    }

    // 배송지 삭제
    @PatchMapping("/{addressId}/delete")
    public void deleteAddress(@PathVariable String addressId,
                              @RequestParam String deletedBy,
                              @RequestHeader(value = "X-User-Id", required = true) String userId) {
        addressService.deleteAddress(addressId, deletedBy, userId);
    }

    // 배송지 상세 조회
    @GetMapping("/{addressId}")
    public AddressResponseDto getAddressById(@PathVariable String addressId,
                                             @RequestHeader(value = "X-User-Id", required = true) String userId) {
        return addressService.getAddressById(addressId, userId);
    }

    // 배송지 조회 (페이징)
    @GetMapping
    public Page<AddressResponseDto> getAddresses(Pageable pageable,
                                                 @RequestHeader(value = "X-User-Id", required = true) String userId) {
        return addressService.getAddresses(pageable, userId);
    }
}
