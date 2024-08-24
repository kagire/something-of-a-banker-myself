package com.kagire.command.modifying;

import com.kagire.command.Command;
import com.kagire.data.dao.BankAccountDao;
import com.kagire.data.dao.BankDao;
import com.kagire.data.dao.ClientDao;
import com.kagire.data.dao.TransactionDao;
import com.kagire.data.entity.Bank;
import com.kagire.data.entity.BankAccount;
import com.kagire.data.entity.Client;
import com.kagire.data.misc.Currency;
import com.kagire.exception.CommandException;
import com.kagire.util.Pair;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BankAccountCommand extends Command {

    @Override
    public String name() {
        return "account";
    }

    @Override
    public String description() {
        return """
            Executes specified operation with bank account. Available operations:
            list <client>                                                  - prints all accounts for specified client
            add <name> <client name> <bank name> <currency>                - adds bank account for specified client & bank with one of currencies: byn, usd or eur
            deposit <name> <value>                                         - adds specified money count to specified account
            transfer <sender account name> <receiver account name> <value> - transfers money""";
    }

    @Override
    public void execute(String[] args) {
        validateArgs(args, 2);
        String operation = args[1];

        var bankAccountDao = new BankAccountDao();
        switch (operation) {
            case "list" -> {
                validateArgs(args, 3);
                String clientName = args[2];

                Client client = new ClientDao().get(Pair.of("name", clientName));

                if (client == null)
                    throw new CommandException("specified client is absent");

                var accounts = bankAccountDao.getAll(Pair.of("client_id", client.getId()));
                if (accounts.isEmpty())
                    System.out.println("no accounts");
                else
                    accounts.forEach(bankAccount -> System.out.printf("%s at %s(%s %s)%n", bankAccount.getName(),
                        bankAccount.getBank().getName(), bankAccount.getValue(), bankAccount.getCurrency()));
            }
            case "add" -> {
                validateArgs(args, 6);
                String accountName = args[2];
                String clientName = args[3];
                String bankName = args[4];
                Currency currency = Currency.valueOf(args[5].toUpperCase());

                Client client = new ClientDao().get(Pair.of("name", clientName));
                Bank bank = new BankDao().get(Pair.of("name", bankName));

                if (client == null)
                    throw new CommandException("specified client is absent");
                if (bank == null)
                    throw new CommandException("specified bank is absent");

                int rowsAffected = bankAccountDao.save(List.of(
                    Pair.of("id", UUID.randomUUID().toString()),
                    Pair.of("name", accountName),
                    Pair.of("client_id", client.getId()),
                    Pair.of("bank_id", bank.getId()),
                    Pair.of("value", 0),
                    Pair.of("currency", currency)
                ));
                if (rowsAffected > 0) System.out.println("account for " + clientName + " added in bank " + bankName);
            }
            case "deposit" -> {
                validateArgs(args, 4);
                String accountName = args[2];
                double value = Double.parseDouble(args[3].toUpperCase());

                if (value <= 0)
                    throw new CommandException("cannot deposit that value");

                Optional.ofNullable(bankAccountDao.get(
                    Pair.of("name", accountName)
                )).ifPresentOrElse(
                    bankAccount -> {
                        int rowsAffected = bankAccountDao.update(
                            List.of(Pair.of("value", bankAccount.getValue() + value)),
                            Pair.of("name", accountName)
                        );
                        if (rowsAffected > 0) System.out.println("deposit done");
                    },
                    () -> System.out.println("no bank account found")
                );
            }
            case "transfer" -> {
                validateArgs(args, 5);
                String senderAccountName = args[2];
                String receiverAccountName = args[3];
                double value = Double.parseDouble(args[4].toUpperCase());

                BankAccount senderAccount = bankAccountDao.get(Pair.of("name", senderAccountName));
                BankAccount receiverAccount = bankAccountDao.get(Pair.of("name", receiverAccountName));

                if (senderAccount == null)
                    throw new CommandException("sender account absent");
                if (receiverAccount == null)
                    throw new CommandException("receiver account absent");

                if (value <= 0 || senderAccount.getValue() < value)
                    throw new CommandException("cannot deposit that value");

                int affectedSenders = bankAccountDao.update(
                    List.of(Pair.of("value", senderAccount.getValue() - value)),
                    Pair.of("name", senderAccountName)
                );

                if (affectedSenders == 0) return;

                double valueToReceive = senderAccount.getCurrency().exchangeRate(receiverAccount.getCurrency()) * value;
                if (!senderAccount.getBank().getId().equals(receiverAccount.getBank().getId()))
                    valueToReceive *= (100 - receiverAccount.getBank().fee(receiverAccount.getClient().getType())) / 100;

                int affectedReceivers = bankAccountDao.update(
                    List.of(Pair.of("value", receiverAccount.getValue() + valueToReceive)),
                    Pair.of("name", receiverAccountName)
                );

                new TransactionDao().save(List.of(
                    Pair.of("id", UUID.randomUUID().toString()),
                    Pair.of("sender_id", senderAccount.getId()),
                    Pair.of("receiver_id", receiverAccount.getId()),
                    Pair.of("value", valueToReceive),
                    Pair.of("currency", receiverAccount.getCurrency()),
                    Pair.of("date", new Timestamp(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()))
                ));

                if (affectedReceivers > 0) System.out.printf("transferred %s %s from %s to %s%n", valueToReceive,
                    receiverAccount.getCurrency(), senderAccount.getName(), receiverAccount.getName());
            }
        }
    }
}
