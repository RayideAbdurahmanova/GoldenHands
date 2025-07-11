package com.matrix.Java._Spring.controller;

import com.matrix.Java._Spring.dto.RoleDto;
import com.matrix.Java._Spring.dto.RoleRequest;
import com.matrix.Java._Spring.exceptions.DataExistException;
import com.matrix.Java._Spring.model.entity.Role;
import com.matrix.Java._Spring.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
@PreAuthorize("hasAuthority('ADMIN')")
public class RoleController {
    private final RoleService roleService;

    @PostMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createRole(@RequestBody @Valid RoleRequest request) throws DataExistException {
        roleService.createRole(request);
    }

    @PostMapping("assing-role-toUser")
    public void assignRoleToUser(@RequestParam Long userID,@RequestParam Long roleId) throws DataExistException {
        roleService.assignRoleToUser(userID,roleId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RoleDto> getRoles(){
        return roleService.getRoles();
    }

    @DeleteMapping("remove-role-fromUser")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeRoleFromUser(@RequestParam Long userID,@RequestParam Long roleID) throws DataExistException {
        roleService.removeRoleFromUser(userID,roleID);
    }
}
