package com.innogrid.service;

import com.innogrid.dao.DocumentDao;
import com.innogrid.dao.DocumentStateDao;
import com.innogrid.model.Document;
import com.innogrid.statemachine.Events;
import com.innogrid.statemachine.States;
import com.innogrid.tables.records.DocumentRecord;
import com.innogrid.tables.records.DocumentStateRecord;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jooq.exception.NoDataFoundException;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@AllArgsConstructor
public class DocumentService {

    StateMachineFactory<States, Events> factory;
    StateMachinePersister<States, Events, String> persister;
    IDGenerator idGenerator;
    DocumentDao documentDao;
    DocumentStateDao documentStateDao;


    @SneakyThrows
    @Transactional
    public Document create(Document document) {


        var machineId = idGenerator.generate();
        StateMachine<States, Events> stateMachine = factory.getStateMachine(machineId);
        stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(Events.SUBMIT).build()))
                .blockLast();

        persister.persist(stateMachine, machineId);

        var record = new DocumentRecord();
        record.from(document);
        document.setId(documentDao.insert(record).getId());
        document.setState(stateMachine.getState().getId());
        documentStateDao.insert(new DocumentStateRecord().setMachineId(machineId).setDocId(document.getId()));

        return document;

    }


    public Document getDocument(Integer id) {

        try {
            var docState = documentStateDao.getByDocId(id).orElseThrow(NoDataFoundException::new);
            var stateMachine = factory.getStateMachine(docState.getMachineId());
            persister.restore(stateMachine, docState.getMachineId());

            var doc = documentDao.get(id).orElseThrow(NoDataFoundException::new);
            var document = doc.into(Document.class);
            document.setState(stateMachine.getState().getId());

            return document;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
