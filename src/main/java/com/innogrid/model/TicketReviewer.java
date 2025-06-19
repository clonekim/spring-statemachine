package com.innogrid.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class TicketReviewer extends BaseModel {
    private Integer id;
    private Integer ticketId;
    private VoteAnswer vote;
    private String comments;
    private String reviewer;
    private String reviewerName;
}
