package org.nurfet.hotelchain.impl;

import lombok.RequiredArgsConstructor;
import org.nurfet.hotelchain.model.Role;
import org.nurfet.hotelchain.repository.RoleRepository;
import org.nurfet.hotelchain.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }
}
