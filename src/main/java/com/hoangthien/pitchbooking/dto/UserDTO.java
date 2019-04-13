package com.hoangthien.pitchbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;

    private String fullName;

    private String email;

    private String phoneNumber;

    private String avatar;
}
