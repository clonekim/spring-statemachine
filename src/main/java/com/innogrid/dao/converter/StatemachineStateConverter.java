package com.innogrid.dao.converter;

import com.innogrid.statemachine.States;
import org.jetbrains.annotations.NotNull;
import org.jooq.Converter;

public class StatemachineStateConverter implements Converter<String, States> {

    @Override
    public States from(String s) {
        return States.valueOf(s.toUpperCase());
    }

    @Override
    public String to(States states) {
        return states.name();
    }

    @Override
    public @NotNull Class<String> fromType() {
        return String.class;
    }

    @Override
    public @NotNull Class<States> toType() {
        return States.class;
    }
}
