package com.innogrid.controller;

import com.innogrid.input.ReviewInput;
import com.innogrid.input.TicketInput;
import com.innogrid.model.Ticket;
import com.innogrid.model.TicketReviewer;
import com.innogrid.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
@AllArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @MutationMapping("ticket")
    public Mono<Ticket> create(@Argument(name = "input") TicketInput input ) {

        var ticket = new Ticket()
                .setTitle(input.getTitle())
                .setDescription(input.getDescription())
                .setReviewers(input.getReviewers().stream().map(i ->
                        new TicketReviewer()
                                .setReviewer(i)).toList());

        return ticketService.create(ticket);
    }


    @MutationMapping("ticketReviewe")
    public Mono<TicketReviewer> updateReviewer(@Argument(name = "input") ReviewInput input) {
        return ticketService.updateReviewer(
                new TicketReviewer()
                        .setId(input.getId())
                        .setComments(input.getComment())
                        .setVote(input.getVote())
        );

    }



}
