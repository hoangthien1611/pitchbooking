package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.dto.UserDTO;
import com.hoangthien.pitchbooking.entities.User;
import com.hoangthien.pitchbooking.exception.PitchBookingException;
import com.hoangthien.pitchbooking.mapper.UserMapper;
import com.hoangthien.pitchbooking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public UserDTO save(UserDTO userDTO) {
        String regex = "^[a-zA-Z0-9_]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(userDTO.getUserName());
        if (!matcher.matches()) {
            throw new PitchBookingException("Tên đăng nhập chỉ được phép dùng chữ, số và kí tự _");
        }

        Optional<User> userOptional = userRepository
                .findByUserName(userDTO.getUserName());

        if (userOptional.isPresent()) {
            throw new PitchBookingException("Tên đăng nhập đã tồn tại!");
        }

        User user = userMapper.userDTOToUser(userDTO);
        user.setPassword(encoder.encode(userDTO.getPassword()));
        user.setAvatar("user-default.png");

        return userMapper.userToUserDTO(userRepository.save(user));
    }

    @Override
    public UserDTO getUser(String userName) {
        Optional<User> userOptional = userRepository
                .findByUserName(userName);

        if (userOptional.isPresent()) {
            return userMapper.userToUserDTO(userOptional.get());
        }

        return null;
    }

    @Override
    public boolean isUserNameExisted(String userName) {
        return userRepository.findByUserName(userName).isPresent();
    }
}
