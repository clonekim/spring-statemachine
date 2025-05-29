package com.innogrid.model;

import com.innogrid.statemachine.States;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class Document {

    private Integer id;
    private String title;
    private String description;
    private LocalDateTime createdDate;
    private States state;
}
