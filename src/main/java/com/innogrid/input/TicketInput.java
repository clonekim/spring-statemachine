package com.innogrid.input;

import lombok.Data;

import java.util.List;

@Data
public class TicketInput {
    private String title;
    private String description;
    private List<String> reviewers;
}
