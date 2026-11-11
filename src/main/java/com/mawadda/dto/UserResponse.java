package com.mawadda.dto;

import com.mawadda.entity.User;
import com.mawadda.enums.Role;

public record UserResponse(
        Integer id,
        String email,
        String firstName,
        String lastName,
        Role role,
        Boolean canCreateService,
        Integer serviceId,
        String serviceName
) {
    public static UserResponse fromEntity(User u) {
        if (u == null) return null;
        return new UserResponse(
                u.getId(),
                u.getEmail(),
                u.getFirstName(),
                u.getLastName(),
                u.getRole(),
                u.getCanCreateService(),
                u.getService() != null ? u.getService().getId() : null,
                u.getService() != null ? u.getService().getName() : null
        );
    }
}
