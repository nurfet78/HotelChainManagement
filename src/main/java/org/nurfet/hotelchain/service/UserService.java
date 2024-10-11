package org.nurfet.hotelchain.service;

import org.nurfet.hotelchain.dto.UserDto;
import org.nurfet.hotelchain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAllUsers();

    User findUserById(Long id);

    User createUserFromDto(UserDto userDto);

    User updateUserFromDto(UserDto userDto);

    void deleteUserById(Long id);

    boolean existsByUsername(String username);

    User findByUsername(String username);

    UserDto userToUserDto(User user);

    UserDto prepareUserDtoForEdit(Long id);

    boolean validateUserData(UserDto userDto);
}
