package com.innogrid.dao.converter;

import com.innogrid.statemachine.Events;
import org.jetbrains.annotations.NotNull;
import org.jooq.Converter;

public class StatemachineEventConverter implements Converter<String, Events> {

    @Override
    public Events from(String s) {
        if(s == null || s.isEmpty())
            return null;

        return Events.valueOf(s.toUpperCase());
    }

    @Override
    public String to(Events events) {
        if(events == null)
            return null;

        return events.name();
    }

    @Override
    public @NotNull Class<String> fromType() {
        return String.class;
    }

    @Override
    public @NotNull Class<Events> toType() {
        return Events.class;
    }
}
