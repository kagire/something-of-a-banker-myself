package com.kagire.command.listing;

import com.kagire.command.Command;
import com.kagire.data.dao.ClientDao;
import com.kagire.data.dao.TransactionDao;
import com.kagire.data.entity.Client;
import com.kagire.data.entity.Transaction;
import com.kagire.exception.CommandException;
import com.kagire.util.Pair;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Predicate;

public class TransactionCommand extends Command {

    @Override
    public String name() {
        return "transaction";
    }

    @Override
    public String description() {
        return """
            Executes specified operation with transactions. Available operations:
            list <client name> <from date> <to date> - prints all transactions within date range. Date format: dd.mm.yyyy""";
    }

    @Override
    public void execute(String[] args) {
        validateArgs(args, 2);
        String operation = args[1];

        var transactionDao = new TransactionDao();
        if (operation.equals("list")) {
            validateArgs(args, 5);
            String clientName = args[2];
            String fromDateStr = args[3];
            String toDateStr = args[4];

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDateTime fromDate = LocalDate.parse(fromDateStr, formatter).atStartOfDay();
            LocalDateTime toDate = LocalDate.parse(toDateStr, formatter).atStartOfDay();
            Predicate<Transaction> dateFilter = tr -> tr.getDate().isAfter(fromDate) && tr.getDate().isBefore(toDate);

            Client client = new ClientDao().get(Pair.of("name", clientName));
            if (client == null)
                throw new CommandException("specified client is absent");

            List<Transaction> outgoing = transactionDao.getAllOutgoingByClientId(client.getId())
                .stream().filter(dateFilter).toList();
            List<Transaction> incoming = transactionDao.getAllIncomingByClientId(client.getId())
                .stream().filter(dateFilter).toList();

            System.out.println("Outgoing:");
            if (outgoing.isEmpty())
                System.out.println("no transactions");
            else
                outgoing.forEach(tr -> System.out.printf("%s %s %s%n",
                tr.getDate().format(formatter),tr.getValue(), tr.getCurrency()));

            System.out.println("Incoming:");
            if (incoming.isEmpty())
                System.out.println("no transactions");
            else
                incoming.forEach(tr -> System.out.printf("%s %s %s%n",
                tr.getDate().format(formatter),tr.getValue(), tr.getCurrency()));
        }
    }
}
