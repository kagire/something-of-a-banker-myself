package com.kagire.command.common;

import com.kagire.command.Command;
import com.kagire.exception.CommandException;

public class EmptyCommand extends Command {

    @Override
    public String name() {
        return null;
    }

    @Override
    public String description() {
        return "";
    }

    @Override
    public void execute(String[] args) {
        throw new CommandException("unrecognised command");
    }
}
