package com.dksoft.tn.dto;

import com.dksoft.tn.entity.Role;

import java.util.Set;

public record UserDto(
        Long id ,
        String firstName ,
        String lastName ,
        String username ,
        String email ,
        String password ,
        String phone,
        Set<Role> roles
) {}
