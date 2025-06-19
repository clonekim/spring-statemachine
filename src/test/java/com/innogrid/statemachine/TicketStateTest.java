package com.innogrid.statemachine;

import com.innogrid.model.Ticket;
import com.innogrid.model.TicketReviewer;
import com.innogrid.model.VoteAnswer;
import com.innogrid.service.TicketService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@SpringBootTest(webEnvironment =  NONE)
public class TicketStateTest {



    @Autowired
    TicketService ticketService;



    @Test
    @DisplayName("리뷰어가 없는 경우")
    public void createTicketAsFail() {
        var ticket = new Ticket();
        ticket.setTitle("This is a ticket");
        ticket.setDescription("This is a ticket description");
        var result = ticketService.create(ticket);

        System.out.println(result);

    }

    @Test
    @DisplayName("리뷰어가 있는 경우")
    public void createTicket() {
        var ticket = new Ticket();
        ticket.setTitle("This is a ticket");
        ticket.setDescription("This is a ticket description");
        ticket.setCreatedBy("KK");
        ticket.setReviewers(List.of(
                new TicketReviewer().setReviewer("K").setVote(VoteAnswer.PENDING),
                new TicketReviewer().setReviewer("J").setVote(VoteAnswer.PENDING)
        ));
        var result = ticketService.create(ticket).block();

        System.out.println(result);
    }

}
