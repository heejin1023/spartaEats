package com.sparta.spartaeats.address.core;

import com.sparta.spartaeats.address.dto.AddressRequestDto;
import com.sparta.spartaeats.entity.Order;
import com.sparta.spartaeats.entity.TimeStamped;
import com.sparta.spartaeats.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_delivery")
public class Address extends TimeStamped {

    @Id
    @GeneratedValue
    @Column(name = "delvr_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "local", length = 100, nullable = false)
    private String local;

    @Column(name = "zip", length = 20, nullable = false)
    private String zip;

    @Column(name = "address", length = 200, nullable = false)
    private String address;

    @Column(name = "address2", length = 200)
    private String address2;

    @Column(name = "contact", length = 20, nullable = false)
    private String contact;

    @Column(name = "del_yn", nullable = false)
    private Character delYn = 'N';

    @Column(name = "use_yn", nullable = false)
    private Character useYn = 'Y';

    private Long deletedBy;
    private LocalDateTime deletedAt;

    public Address(AddressRequestDto addressRequestDto, Long userIdx) {
        this.local = addressRequestDto.getLocal();
        this.zip = addressRequestDto.getZip();
        this.address = addressRequestDto.getAddress();
        this.address2 = addressRequestDto.getAddress2();
        this.contact = addressRequestDto.getContact();
        this.user = new User(userIdx);  // 이 부분은 실제 User 객체와 매핑하도록 수정 필요
        this.useYn = addressRequestDto.getUseYn() != null ? addressRequestDto.getUseYn() : 'Y';
    }

    public void update(AddressRequestDto addressRequestDto, Long userIdx) {
        this.local = addressRequestDto.getLocal();
        this.zip = addressRequestDto.getZip();
        this.address = addressRequestDto.getAddress();
        this.address2 = addressRequestDto.getAddress2();
        this.contact = addressRequestDto.getContact();
        this.useYn = addressRequestDto.getUseYn() != null ? addressRequestDto.getUseYn() : this.useYn;
    }

    public void delete(Long deletedBy) {
        this.delYn = 'Y';
        this.deletedBy = deletedBy;
        this.deletedAt = LocalDateTime.now();
    }
}
