package com.simplechat.shared.commands.user;

import com.simplechat.shared.commands.AbstractCommandBuilder;

/**
 * Created by lampt on 6/24/2017.
 */
public class UserCommandBuilder implements AbstractCommandBuilder {

    private String[] messageArgs;

    public UserCommandBuilder(String... messageArgs) {
        this.messageArgs = messageArgs;
    }

    @Override
    public String build() {

        return null;
    }
}
