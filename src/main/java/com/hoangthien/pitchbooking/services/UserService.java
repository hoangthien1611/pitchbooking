package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.dto.UserDTO;

public interface UserService {

    UserDTO save(UserDTO userDTO);

    UserDTO getUser(String userName);

    UserDTO getUser(Long id);

    boolean isUserNameExisted(String userName);

    UserDTO updateProfile(UserDTO userDTO);

    boolean changePassword(Long id, String currentPassword, String newPassword);
}
