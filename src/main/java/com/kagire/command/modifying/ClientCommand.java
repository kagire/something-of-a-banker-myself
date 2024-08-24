package com.kagire.command.modifying;

import com.kagire.command.Command;
import com.kagire.data.dao.ClientDao;
import com.kagire.data.misc.ClientType;
import com.kagire.util.Pair;

import java.util.List;
import java.util.UUID;

public class ClientCommand extends Command {

    @Override
    public String name() {
        return "client";
    }

    @Override
    public String description() {
        return """
            Executes specified operation with client. Available operations:
            list                     - prints all clients
            add <name> <type>        - adds client with specified name and type. Available types: "person" or "company"
            update <name> <new name> - updates client`s name with specified name
            delete <name>            - deletes client with specified name""";
    }

    @Override
    public void execute(String[] args) {
        validateArgs(args, 2);
        String operation = args[1];

        var clientDao = new ClientDao();
        switch (operation) {
            case "list" -> {
                var clients = clientDao.getAll();
                if (clients.isEmpty())
                    System.out.println("There are no clients");
                else
                    clients.forEach(client -> System.out.printf("%s (%s)%n", client.getName(), client.getType()));

            }
            case "add" -> {
                validateArgs(args, 4);
                String clientName = args[2];
                ClientType type = ClientType.valueOf(args[3].toUpperCase());

                int rowsAffected = clientDao.save(List.of(
                    Pair.of("id", UUID.randomUUID().toString()),
                    Pair.of("name", clientName),
                    Pair.of("type", type)
                ));
                if (rowsAffected > 0) System.out.println("client " + clientName + " added");

            }
            case "update" -> {
                validateArgs(args, 4);
                String clientName = args[2];
                String clientNewName = args[3];

                int rowsAffected = clientDao.update(
                    List.of(Pair.of("name", clientNewName)),
                    Pair.of("name", clientName)
                );
                if (rowsAffected > 0) System.out.println("client " + clientName + " updated to " + clientNewName);

            }
            case "delete" -> {
                validateArgs(args, 3);
                String clientName = args[2];

                int rowsAffected = clientDao.delete(Pair.of("name", clientName));
                if (rowsAffected > 0) System.out.println("client " + clientName + " deleted");
            }
        }
    }
}
