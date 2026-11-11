package com.mawadda.dto;

import com.mawadda.entity.Parent;

public record ParentResponse(
        Integer id,
        String cin,
        String firstName,
        String lastName,
        String phone
) {
    public static ParentResponse fromEntity(Parent parent) {
        if (parent == null) return null;
        return new ParentResponse(
                parent.getId(),
                parent.getCin(),
                parent.getFirstName(),
                parent.getLastName(),
                parent.getPhone()
        );
    }
}
