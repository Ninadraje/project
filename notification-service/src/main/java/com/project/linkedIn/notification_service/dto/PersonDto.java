package com.project.linkedIn.notification_service.dto;

import lombok.Data;

@Data
public class PersonDto {

    private Long id;
    private Long userId;
    private String name;

    public Long getId() {
        return this.id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public String getName() {
        return this.name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
