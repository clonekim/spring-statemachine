
```sql


CREATE TABLE state_machine (
    machine_id VARCHAR(255) PRIMARY KEY NOT NULL,
    state      VARCHAR(255)             NOT NULL
);

CREATE TABLE document (
    id           INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title        VARCHAR(255) NOT NULL,
    description  TEXT         NOT NULL,
    created_date TIMESTAMP
);


create table document_state (
    machine_id VARCHAR(255) NOT NULL REFERENCES state_machine (machine_id),
    doc_id     INT          NOT NULL REFERENCES document (id),
    CONSTRAINT uk_document_state UNIQUE (machine_id, doc_id)
);



```