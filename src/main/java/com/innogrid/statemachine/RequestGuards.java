package com.innogrid.statemachine;

import com.innogrid.model.VoteAnswer;
import com.innogrid.service.TicketService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class RequestGuards {

    @Setter
    private TicketService ticketService;

    @Bean
    public Guard<States, Events> reviewerExists() {
        return context -> {
            log.debug("리뷰어가 존재 하는지 검증합니다.");
            try {
                int count = context.getExtendedState()
                        .get(StateProperty.REVIEWER_COUNT, Integer.class);
                return count > 0;
            } catch (NullPointerException e) {
                log.error("Not found value", e);
                return false;
            }
        };
    }


       // 모든 승인자가 승인했는지 확인
    @Bean
    public Guard<States, Events> approvedAll() {
        return context -> {

            var ticketId = context.getExtendedState().get("ticketId", Integer.class);
            var reviewers =  ticketService.getReviewers(ticketId);
            return reviewers.stream().allMatch(i -> i.getVote() == VoteAnswer.APPROVE);
        };
    }
}
