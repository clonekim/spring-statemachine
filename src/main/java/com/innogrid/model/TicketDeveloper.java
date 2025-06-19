package com.innogrid.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class TicketDeveloper extends BaseModel {

    private Integer id;
    private Integer tokenId;
    private String credentials;
    private String developer;
    private String developerName;
}
