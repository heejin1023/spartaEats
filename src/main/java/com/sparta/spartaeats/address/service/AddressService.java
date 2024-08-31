package com.sparta.spartaeats.address.service;

import com.sparta.spartaeats.address.domain.Address;
import com.sparta.spartaeats.common.type.UserRoleEnum;
import com.sparta.spartaeats.address.dto.AddressRequestDto;
import com.sparta.spartaeats.address.dto.AddressResponseDto;
import com.sparta.spartaeats.address.repository.AddressRepository;
import com.sparta.spartaeats.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressResponseDto createAddress(AddressRequestDto addressRequestDto, Long userIdx) {
        // DTO -> Entity 변환
        Address address = new Address(addressRequestDto, userRepository.findById(userIdx).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다")));

        // DB 저장
        Address savedAddress = addressRepository.save(address);

        // Entity -> ResponseDto 변환
        return new AddressResponseDto(savedAddress);
    }

    public List<AddressResponseDto> getAddresses(Pageable pageable, Long userIdx, UserRoleEnum role, String local, Long orderId, Character useYn) {
        List<Address> addresses;
        // DB 조회 및 변환
        // 역할별 조회 차이
        // 관리자 가게주인 > 제한없음
        // 사용자  > 내 주문만
        // 정렬조건 생성일순, 수정일순
        // 검색필드 : 지역 주문id 활성화여부
        if (role == UserRoleEnum.ADMIN || role == UserRoleEnum.OWNER) {
            // 관리자나 가게 주인이라면 모든 주소를 조회
            addresses = addressRepository.findAll(pageable).getContent();
        } else {
            // 일반 사용자는 자신의 주소만 조회
            addresses = addressRepository.findByUserIdAndLocalAndOrderIdAndUseYn(userIdx, local, orderId, useYn, pageable);
        }

        // Entity -> DTO 변환
        return addresses.stream()
                .map(AddressResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public AddressResponseDto updateAddress(UUID addressId, AddressRequestDto addressRequestDto, Long userIdx) {
        // 해당 주소가 DB에 존재하는지 확인
        Address address = findAddress(addressId);

        // 주소 정보 수정
        // 역할별 수정 차이
        // 관리자 > 가능
        // 그 외 > 생성자 idx랑 비교해서 동일할 경우
        address.update(addressRequestDto, userIdx);

        // 수정된 데이터 반환
        return new AddressResponseDto(address);
    }

    public void deleteAddress(UUID addressId, Long deletedBy, Long userId) {
        // 해당 주소가 DB에 존재하는지 확인
        Address address = findAddress(addressId);

        // 주소 삭제 처리 (소프트 삭제로 구현 가능)
        // 역할별 수정 차이
        // 관리자 > 가능
        // 그 외 > 생성자 idx랑 비교해서 동일할 경우
        address.delete(deletedBy);

        // DB에 반영
        addressRepository.save(address);
    }

    public AddressResponseDto getAddressById(UUID addressId, Long userIdx) {
        // 특정 주소 조회
        Address delivery = findAddress(addressId);

        // 조회된 주소 정보를 ResponseDto로 변환하여 반환
        // 역할별 수정 차이
        // 관리자, 가게주인 > 가능
        // 사용자 > 생성자 idx랑 비교해서 동일할 경우
        return new AddressResponseDto(delivery);
    }

    // Address 조회 및 예외 처리
    private Address findAddress(UUID addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("선택한 주소는 존재하지 않습니다."));
    }
}
