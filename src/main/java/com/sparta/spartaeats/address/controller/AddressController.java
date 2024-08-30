package com.sparta.spartaeats.address.controller;

import com.sparta.spartaeats.address.service.AddressService;
import com.sparta.spartaeats.address.dto.AddressRequestDto;
import com.sparta.spartaeats.address.dto.AddressResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    // 배송지 등록
    @PostMapping
    public AddressResponseDto createAddress(@RequestBody AddressRequestDto addressRequestDto,
                                            @RequestHeader(value = "X-User-Id", required = true) Long userIdx) {
        return addressService.createAddress(addressRequestDto, userIdx);
    }

    // 배송지 수정
    @PatchMapping("/{addressId}")
    public AddressResponseDto updateAddress(@PathVariable String addressId,
                                            @RequestBody AddressRequestDto addressRequestDto,
                                            @RequestHeader(value = "X-User-Id", required = true) Long userIdx) {
        return addressService.updateAddress(UUID.fromString(addressId), addressRequestDto, userIdx);
    }

    // 배송지 삭제
    @PatchMapping("/{addressId}/delete")
    public void deleteAddress(@PathVariable String addressId,
                              @RequestParam Long deletedBy,
                              @RequestHeader(value = "X-User-Id", required = true) Long userIdx) {
        addressService.deleteAddress(UUID.fromString(addressId), deletedBy, userIdx);
    }

    // 배송지 상세 조회
    @GetMapping("/{addressId}")
    public AddressResponseDto getAddressById(@PathVariable String addressId,
                                             @RequestHeader(value = "X-User-Id", required = true) Long userIdx) {
        return addressService.getAddressById(UUID.fromString(addressId), userIdx);
    }

    // 배송지 목록 조회 (페이징)
    @GetMapping
    public List<AddressResponseDto> getAddresses(
            Pageable pageable,
            @RequestHeader(value = "X-User-Id", required = true) Long userIdx,
            @RequestHeader(value = "X-Role", required = true) String role,
            @RequestParam(value = "local", required = false) String local,
            @RequestParam(value = "orderId", required = false) Long orderId,
            @RequestParam(value = "useYn", required = false) Character useYn) {

        // 서비스 호출
        return addressService.getAddresses(pageable, userIdx, role, local, orderId, useYn);
    }
}
