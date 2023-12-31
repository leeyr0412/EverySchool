package com.everyschool.callservice.domain;

import lombok.Getter;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
public class BaseEntity extends TimeBaseEntity {

    private boolean isDeleted;

    protected BaseEntity() {
        this.isDeleted = false;
    }

    public void remove() {
        this.isDeleted = true;
    }
}
