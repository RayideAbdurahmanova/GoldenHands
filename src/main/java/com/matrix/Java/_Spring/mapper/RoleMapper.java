package com.matrix.Java._Spring.mapper;

import com.matrix.Java._Spring.dto.RoleDto;
import com.matrix.Java._Spring.model.entity.Role;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    List<RoleDto> mapToDtoList(List<Role> roleEntity);
}
