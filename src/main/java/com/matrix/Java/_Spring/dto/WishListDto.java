package com.matrix.Java._Spring.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class WishListDto {

    private Integer id;
    private Integer userId;
    private Integer productId;
}
