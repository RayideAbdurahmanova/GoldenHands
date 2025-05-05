package com.matrix.Java._Spring.dto;

import com.matrix.Java._Spring.enums.UserRole;
import lombok.Data;


@Data
public class CustomerDto {

    private String name;

    private String username;

    private String email;

    private String password;

    private UserRole role;

//    private List<OrderDto> orders;

//    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
//    private List<ProductDto> products;

  //  @OneToMany(mappedBy = "user")
//    private List<WishListDto> wishListDto;

    //@OneToMany(mappedBy = "user")
//    private List<Reviews> reviews;
}
