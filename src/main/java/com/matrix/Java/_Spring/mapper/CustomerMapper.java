package com.matrix.Java._Spring.mapper;

import com.matrix.Java._Spring.dto.CustomerDto;
import com.matrix.Java._Spring.dto.UserProfile;
import com.matrix.Java._Spring.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel="spring")
public interface CustomerMapper {
    @Mapping(target = "email", source = "username")
    UserProfile mapToUserProfile(User userEntity);

    List<CustomerDto> mapToDtoList(List<User> userEntity);
}
