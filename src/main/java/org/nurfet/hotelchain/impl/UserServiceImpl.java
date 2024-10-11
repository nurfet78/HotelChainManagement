package org.nurfet.hotelchain.impl;

import lombok.RequiredArgsConstructor;
import org.nurfet.hotelchain.dto.UserDto;
import org.nurfet.hotelchain.exception.NotFoundException;
import org.nurfet.hotelchain.model.User;
import org.nurfet.hotelchain.model.Role;
import org.nurfet.hotelchain.repository.RoleRepository;
import org.nurfet.hotelchain.repository.UserRepository;
import org.nurfet.hotelchain.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(User.class, id));
    }

    @Override
    @Transactional
    public User createUserFromDto(UserDto userDto) {
        return getUser(userDto, new User());
    }

    @Override
    @Transactional
    public User updateUserFromDto(UserDto userDto) {
        return getUser(userDto, findUserById(userDto.getId()));
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("Имя пользователя %s не найдено", username)));
    }

    @Override
    public UserDto userToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getUsername(),
                passwordEncoder.encode(user.getPassword()),
                user.getRoles().stream().map(Role::getAuthority).collect(Collectors.toSet()));
    }

    @Override
    public UserDto prepareUserDtoForEdit(Long id) {
        return userToUserDto(findUserById(id));
    }

    @Override
    public boolean validateUserData(UserDto userDto) {
        User existingUser = findUserById(userDto.getId());
        String newUsername = userDto.getUsername();

        return existingUser.getUsername().equals(newUsername) || !existsByUsername(newUsername);
    }

    private Role getOrCreateRole(String roleName) {
        return roleRepository.findRoleByAuthority(roleName).orElseGet(() -> roleRepository.save(new Role(roleName)));
    }

    private User getUser(UserDto userDto, User user) {
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        user.removeRole();
        for (String roleName : userDto.getRoles()) {
            Role role = getOrCreateRole(roleName);

            user.getRoles().add(role);
        }

        return userRepository.save(user);
    }
}
