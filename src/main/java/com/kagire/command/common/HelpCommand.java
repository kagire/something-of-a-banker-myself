package com.kagire.command.common;

import com.kagire.command.Command;
import com.kagire.command.CommandOperator;

public class HelpCommand extends Command {

    @Override
    public String name() {
        return "help";
    }

    @Override
    public String description() {
        return "Prints help for all available commands";
    }

    @Override
    public void execute(String[] args) {
        CommandOperator.commandDescriptions().forEach((name, description) -> {
            if (name == null) return;
            System.out.println(name + " --> " + description);
            System.out.println();
        });
    }
}
