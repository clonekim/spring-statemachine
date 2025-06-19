package com.innogrid.service;

import com.innogrid.dao.TicketDao;
import com.innogrid.dao.TicketReviewerDao;
import com.innogrid.dao.TicketStateDao;
import com.innogrid.model.Ticket;
import com.innogrid.model.TicketReviewer;
import com.innogrid.statemachine.Events;
import com.innogrid.statemachine.StateProperty;
import com.innogrid.statemachine.States;
import com.innogrid.tables.records.TicketRecord;
import com.innogrid.tables.records.TicketReviewerRecord;
import com.innogrid.tables.records.TicketStateRecord;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.exception.NoDataFoundException;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineException;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.innogrid.Tables.TICKET_REVIEWER;
import static com.innogrid.statemachine.Events.*;
import static org.springframework.statemachine.StateMachineEventResult.ResultType.ACCEPTED;

@Slf4j
@Service
@AllArgsConstructor
public class TicketService {

    final StateMachineFactory<States, Events> stateMachineFactory;
    final StateMachinePersister<States, Events, String> persister;
    final TicketDao ticketDao;
    final TicketStateDao ticketStateDao;
    final TicketReviewerDao ticketReviewerDao;

    @Transactional
    public Mono<Ticket> create(Ticket ticket) {

        return fetchEventWithResult(
                MessageBuilder.withPayload(CREATE_TICKET).build(),
                sm -> sm.getExtendedState().getVariables().put(StateProperty.REVIEWER_COUNT, ticket.getReviewers().size()),

                sm -> {
                    ticket.setState(sm.getState().getId());
                    var record = new TicketRecord();
                    record.from(ticket);

                    var model = ticketDao.save(record).into(Ticket.class);

                    ticketStateDao.insert(new TicketStateRecord()
                            .setTicketId(model.getId())
                            .setMachineId(sm.getId()));

                    ticketReviewerDao.batchInsert(
                            ticket.getReviewers().stream().map(t -> new TicketReviewerRecord()
                                    .setVote(t.getVote())
                                    .setReviewer(t.getReviewer())
                                    .setTicketId(model.getId())
                            ).toList());

                    return ticket;

                });
    }


    public Mono<TicketReviewer> updateReviewer(TicketReviewer reviewer) {
        var record = ticketReviewerDao.fetchOne(reviewer.getId()).orElseThrow(NoDataFoundException::new);
        record.setComments(reviewer.getComments());
        record.setVote(reviewer.getVote());

        var machineId = ticketStateDao.findMachine(record.getTicketId())
                .orElseThrow(NoDataFoundException::new)
                .getMachineId();

        var message = switch (reviewer.getVote()) {
            case APPROVE -> MessageBuilder.withPayload(REQUEST_APPROVED).build();
            case REJECT -> MessageBuilder.withPayload(REQUEST_REJECTED).build();
            default -> throw new IllegalStateException("Unexpected value: " + reviewer.getVote());
        };

        return fetchEventWithResult(machineId, message, sm -> {
            ticketReviewerDao.update(record);
            return record.into(TicketReviewer.class);
        });

    }

    /* Helper Method */
    public <T> Mono<T> fetchEventWithResult(String machineId, Message<Events> message, Consumer<StateMachine<States, Events>> init, Function<StateMachine<States, Events>, T> handler) {
        StateMachine<States, Events> sm = stateMachineFactory.getStateMachine();

        if (machineId != null) {
            try {
                persister.restore(sm, machineId);
            } catch (Exception e) {
                throw new StateMachineException("Error restoring machine state", e);
            }
        }

        if (init != null) {
            init.accept(sm);
        }

        return sm
                .sendEvent(Mono.just(message))
                .filter(result -> result.getResultType() == ACCEPTED)
                .next()
                .switchIfEmpty(Mono.error(new Exception("Event not accepted!")))
                .map(result -> handler.apply(sm));
    }

    public <T> Mono<T> fetchEventWithResult(Message<Events> message, Function<StateMachine<States, Events>, T> handler) {
        return fetchEventWithResult(null, message, null, handler);
    }

    public <T> Mono<T> fetchEventWithResult(String machineId, Message<Events> message, Function<StateMachine<States, Events>, T> handler) {
        return fetchEventWithResult(machineId, message, null, handler);
    }

    public <T> Mono<T> fetchEventWithResult( Message<Events> message, Consumer<StateMachine<States, Events>> init, Function<StateMachine<States, Events>, T> handler) {
        return fetchEventWithResult(null, message, init, handler);
    }

    public List<TicketReviewer> getReviewers(Integer ticketId) {
        return ticketReviewerDao.fetch(TICKET_REVIEWER.TICKET_ID.eq(ticketId))
                .into(TicketReviewer.class);
    }
}
