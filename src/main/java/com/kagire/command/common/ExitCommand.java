package com.kagire.command.common;

import com.kagire.command.Command;

public class ExitCommand extends Command {

    @Override
    public String name() {
        return "exit";
    }

    @Override
    public String description() {
        return "Makes program to shut down";
    }

    @Override
    public void execute(String[] args) {
        System.exit(1);
    }
}
