package com.matrix.Java._Spring.service.impl;

import com.matrix.Java._Spring.dto.RoleDto;
import com.matrix.Java._Spring.dto.RoleRequest;
import com.matrix.Java._Spring.exceptions.DataExistException;
import com.matrix.Java._Spring.exceptions.DataNotFoundException;
import com.matrix.Java._Spring.mapper.RoleMapper;
import com.matrix.Java._Spring.model.entity.Role;
import com.matrix.Java._Spring.model.entity.User;
import com.matrix.Java._Spring.repository.RoleRepository;
import com.matrix.Java._Spring.repository.UserRepository;
import com.matrix.Java._Spring.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final RoleMapper roleMapper;

    @Override
    public void createRole(RoleRequest request) throws DataExistException {
        log.info("Starting creation of role");
        if (roleRepository.existsByName(request.getRoleName())) {
            throw new DataExistException("This role already exists");
        }
        Role role = new Role();
        role.setName(request.getRoleName());
        role.setCreateDate(LocalDateTime.now());
        roleRepository.save(role);
        log.info("Finishing creation of role : {}", role);
    }

    @Override
    public void assingRoleToUser(Long userID, Long roleId) {
        log.info("Starting assing role to user");
        var user = userRepository.findById(Math.toIntExact(userID))
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        var role = roleRepository.findById(roleId)
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
        user.getRoles().add(role);
        userRepository.save(user);
        log.info("Finishing assing role {}  to user {}", roleId, userID);
    }

    @Override
    public List<RoleDto> getRoles() {
        log.info("Starting retrieval of roles list");
        var roleEntity = roleRepository.findAll();
        List<RoleDto> roleDtos=roleMapper.mapToDtoList(roleEntity);
        log.info("Finishing retrieval {} roles list",roleDtos.size());
        return roleDtos;
    }

    @Override
    public void removeRoleFromUser(Long userID, Long roleID) {
        log.info("Starting removing role from user");
        User user = userRepository.findById(Math.toIntExact(userID)).orElseThrow();
        Role role = roleRepository.findById(roleID).orElseThrow();
        user.getRoles().remove(role);
        userRepository.save(user);
        log.info("Finishing removing role {} from user {}", roleID, userID);
    }
}
