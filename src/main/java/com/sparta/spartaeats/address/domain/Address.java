package com.sparta.spartaeats.address.domain;

import com.sparta.spartaeats.address.dto.AddressRequestDto;
import com.sparta.spartaeats.common.util.TimeStamped;
import com.sparta.spartaeats.order.domain.Order;
import com.sparta.spartaeats.user.domain.User;
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

    @Column(name = "created_by", nullable = false, updatable = false)
    private Long createdBy;

    private Long deletedBy;
    private LocalDateTime deletedAt;


    public Address(AddressRequestDto addressRequestDto, User user) {
        this.local = addressRequestDto.getLocal();
        this.zip = addressRequestDto.getZip();
        this.address = addressRequestDto.getAddress();
        this.address2 = addressRequestDto.getAddress2();
        this.contact = addressRequestDto.getContact();
        this.user = user;  // 이 부분은 실제 User 객체와 매핑하도록 수정 필요
        this.useYn = addressRequestDto.getUseYn() != null ? addressRequestDto.getUseYn() : 'Y';
        this.createdBy = user.getId(); // created_by 필드 설정
    }

    public void update(AddressRequestDto addressRequestDto, User user) {
        this.local = addressRequestDto.getLocal();
        this.zip = addressRequestDto.getZip();
        this.address = addressRequestDto.getAddress();
        this.address2 = addressRequestDto.getAddress2();
        this.contact = addressRequestDto.getContact();
        this.useYn = addressRequestDto.getUseYn() != null ? addressRequestDto.getUseYn() : this.useYn;
    }

    public void delete(User user) {
        this.delYn = 'Y';
        this.deletedBy = user.getId();
        this.deletedAt = LocalDateTime.now();
    }
}
