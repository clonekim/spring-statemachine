package com.innogrid.dao.converter;

import com.innogrid.model.VoteAnswer;
import org.jetbrains.annotations.NotNull;
import org.jooq.Converter;

public class VoteAnswerConverter implements Converter<String, VoteAnswer> {

    @Override
    public VoteAnswer from(String s) {
        return VoteAnswer.valueOf(s);
    }

    @Override
    public String to(VoteAnswer userObject) {
        return userObject.name();
    }

    @Override
    public @NotNull Class<String> fromType() {
        return String.class;
    }

    @Override
    public @NotNull Class<VoteAnswer> toType() {
        return VoteAnswer.class;
    }

}
