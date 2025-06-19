package com.innogrid.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BaseModel {

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
