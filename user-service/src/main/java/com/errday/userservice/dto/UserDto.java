package com.errday.userservice.dto;

import com.errday.userservice.vo.ResponseOrder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class UserDto {
    private String email;
    private String name;
    private String pwd;
    private String userId;
    private LocalDate createAt;
    private String encryptedPw;

    List<ResponseOrder> orders;
}
