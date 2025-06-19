package com.innogrid.statemachine;

import com.innogrid.dao.TicketReviewerDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.statemachine.StateMachineException;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class RequestActions {

    final StateMachinePersister<States, Events, String> persister;

    final TicketReviewerDao ticketReviewerDao;


    @Bean
    @Transactional
    public Action<States, Events> createTicket() {
        return context ->  {
            log.debug("티켓을 생성합니다.");

            try {
                var state = context.getStateMachine();

                /*
                 지우지 않으면 reviewerCount가 저장되므로
                 꼭 필요한 데이터만 저장할 기 위함이라 여기서는 삭제
                 */
                state.getExtendedState().getVariables().clear();
                persister.persist(state, state.getId());
            } catch (Exception e) {
                throw new StateMachineException("Persist Error", e);
            }
        };

    }

    @Bean
    public Action<States, Events> rejectAction() {
       return context -> {
           log.debug("Rejected");
       };
    }

    @Bean
    public Action<States, Events> approveAction() {
        return context -> {
            log.debug("Approved");
        };
    }
}
