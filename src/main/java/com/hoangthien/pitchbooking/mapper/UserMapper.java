package com.hoangthien.pitchbooking.mapper;

import com.hoangthien.pitchbooking.dto.UserDTO;
import com.hoangthien.pitchbooking.entities.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    User userDTOToUser(UserDTO userDTO);

    UserDTO userToUserDTO(User user);
}
