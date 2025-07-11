package com.matrix.Java._Spring.service;

import com.matrix.Java._Spring.dto.RoleDto;
import com.matrix.Java._Spring.dto.RoleRequest;
import com.matrix.Java._Spring.exceptions.DataExistException;
import com.matrix.Java._Spring.model.entity.Role;

import java.util.List;

public interface RoleService {
    void createRole(RoleRequest request) throws DataExistException;

    void assignRoleToUser(Long userID, Long roleId);

    List<RoleDto> getRoles();

    void removeRoleFromUser(Long userID, Long roleID);
}
