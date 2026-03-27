package com.CineCore.mapper;

import com.CineCore.entity.User;
import com.CineCore.request.UserRequest;
import com.CineCore.response.UserResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {
    public static User toUser(UserRequest userRequest){
        return User
                .builder()
                .name(userRequest.name())
                .email(userRequest.email())
                .password(userRequest.password())
                .build();
    }

    public static UserResponse toUserResponse(User user){
        return UserResponse
                .builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
