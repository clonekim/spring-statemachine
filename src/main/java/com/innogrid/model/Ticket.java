package com.innogrid.model;

import com.innogrid.statemachine.States;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class Ticket extends BaseModel {

    private Integer id;
    private States state;
    private String title;
    private String description;
    private List<TicketReviewer> reviewers;
    private TicketDeveloper developer;
    private String createdBy;
    private String createdByName;
    private String canceledBy;
    private String canceledByName;
    private LocalDateTime canceledAt;
    private LocalDateTime grantedAt;
}
