package com.kagire.command;

import com.kagire.command.common.EmptyCommand;
import com.kagire.exception.CommandException;

public abstract class Command {

    public abstract String name();

    public abstract String description();

    public abstract void execute(String[] args);

    public static Command empty() {
        return new EmptyCommand();
    }

    protected void validateArgs(String[] args, int expectedSize) {
        if (args == null || args.length < expectedSize)
            throw new CommandException("invalid arguments provided");
    }
}
