package com.sparta.spartaeats.product;

import com.sparta.spartaeats.store.Store;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Data
@Getter
public class ProductRequestDto {

    private UUID store_id;
    private String product_name;
    private Integer price;
    private String product_description;
    private String use_yn;

}
