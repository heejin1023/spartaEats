package com.sparta.spartaeats.domain.address;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    @Transactional
    public AddressResponseDto createAddress(AddressRequestDto addressRequestDto, String userId) {
        Address address = new Address();
        address.setUserIdx(Long.valueOf(userId));  // User ID 설정
        address.setZip(addressRequestDto.getZip());
        address.setLocal(addressRequestDto.getLocal());
        address.setAddress(addressRequestDto.getAddress());
        address.setAddress2(addressRequestDto.getAddress2());
        address.setPhone(addressRequestDto.getPhone());
        address.setUseYn(addressRequestDto.getUseYn());
        address.setDelYn('N');  // 기본 값 설정
        address.setCreatedBy(userId);
        address.setCreatedAt(LocalDateTime.now());

        Address savedAddress = addressRepository.save(address);
        return convertToResponseDto(savedAddress);
    }

    @Transactional
    public AddressResponseDto updateAddress(String addressId, AddressRequestDto addressRequestDto, String userId) {
        Address address = addressRepository.findById(UUID.fromString(addressId))
                .orElseThrow(() -> new IllegalArgumentException("Invalid address ID"));

        address.setZip(addressRequestDto.getZip());
        address.setLocal(addressRequestDto.getLocal());
        address.setAddress(addressRequestDto.getAddress());
        address.setAddress2(addressRequestDto.getAddress2());
        address.setPhone(addressRequestDto.getPhone());
        address.setUseYn(addressRequestDto.getUseYn());
        address.setUpdatedBy(userId);
        address.setUpdatedAt(LocalDateTime.now());

        Address updatedAddress = addressRepository.save(address);
        return convertToResponseDto(updatedAddress);
    }

    @Transactional
    public void deleteAddress(String addressId, String deletedBy, String userId) {
        Address address = addressRepository.findById(UUID.fromString(addressId))
                .orElseThrow(() -> new IllegalArgumentException("Invalid address ID"));

        address.setDelYn('Y');
        address.setDeletedBy(deletedBy);
        address.setDeletedAt(LocalDateTime.now());

        addressRepository.save(address);
    }

    @Transactional(readOnly = true)
    public AddressResponseDto getAddressById(String addressId, String userId) {
        Address address = addressRepository.findById(UUID.fromString(addressId))
                .orElseThrow(() -> new IllegalArgumentException("Invalid address ID"));
        return convertToResponseDto(address);
    }

    @Transactional(readOnly = true)
    public Page<AddressResponseDto> getAddresses(Pageable pageable, String userId) {
        return addressRepository.findAll(pageable).map(this::convertToResponseDto);
    }

    private AddressResponseDto convertToResponseDto(Address address) {
        AddressResponseDto responseDto = new AddressResponseDto();
        responseDto.setDelvrUuid(address.getId());
        responseDto.setUserIdx(address.getUserIdx());
        responseDto.setZip(address.getZip());
        responseDto.setLocal(address.getLocal());
        responseDto.setAddress(address.getAddress());
        responseDto.setAddress2(address.getAddress2());
        responseDto.setPhone(address.getPhone());
        responseDto.setUseYn(address.getUseYn());
        responseDto.setDelYn(address.getDelYn());
        responseDto.setCreatedAt(address.getCreatedAt());
        responseDto.setCreatedBy(address.getCreatedBy());
        responseDto.setUpdatedAt(address.getUpdatedAt());
        responseDto.setUpdatedBy(address.getUpdatedBy());
        responseDto.setDeletedAt(address.getDeletedAt());
        responseDto.setDeletedBy(address.getDeletedBy());
        return responseDto;
    }
}
