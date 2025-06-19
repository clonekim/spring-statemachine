package com.innogrid.input;

import com.innogrid.model.VoteAnswer;
import lombok.Data;

@Data
public class ReviewInput {
    private Integer id;
    private VoteAnswer vote;
    private String comment;
}
