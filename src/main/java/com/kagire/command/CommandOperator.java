package com.kagire.command;

import com.kagire.command.common.EmptyCommand;
import com.kagire.command.common.ExitCommand;
import com.kagire.command.common.HelpCommand;
import com.kagire.command.listing.TransactionCommand;
import com.kagire.command.modifying.BankAccountCommand;
import com.kagire.command.modifying.BankCommand;
import com.kagire.command.modifying.ClientCommand;
import com.kagire.exception.CommandException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandOperator {

    private static final Map<String, Command> registry = new HashMap<>();

    static {
        register(new EmptyCommand());
        register(new ExitCommand());
        register(new HelpCommand());

        register(new BankCommand());
        register(new ClientCommand());
        register(new BankAccountCommand());
        register(new TransactionCommand());
    }

    public static void executeWith(String parameter) {
        String[] args = parseArg(parameter);
        String identifier = args != null && args.length > 0 ? args[0] : null;

        try {

            registry.getOrDefault(identifier, Command.empty()).execute(args);

        } catch (CommandException e) {
            throw e;
        } catch (Exception e) {
            throw new CommandException("unable to execute command");
        }
    }

    public static Map<String, String> commandDescriptions() {
        return registry.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue().description()
            ));
    }

    private static String[] parseArg(String arg) {

        return arg == null ? null : arg.trim().replaceAll("\\s+", " ").split(" ");
    }

    private static void register(Command command) {
        registry.put(command.name(), command);
    }
}
