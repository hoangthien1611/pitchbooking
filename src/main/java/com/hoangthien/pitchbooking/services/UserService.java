package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.dto.UserDTO;

public interface UserService {

    UserDTO save(UserDTO userDTO);

    UserDTO getUser(String userName);

    boolean isUserNameExisted(String userName);
}
