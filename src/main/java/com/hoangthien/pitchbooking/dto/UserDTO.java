package com.hoangthien.pitchbooking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
    private Long id;

    private String fullName;

    private String email;

    private String phoneNumber;

    private String avatar;

    private String userName;

    private String password;
}
