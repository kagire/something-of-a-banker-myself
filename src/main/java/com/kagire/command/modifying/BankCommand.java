package com.kagire.command.modifying;

import com.kagire.command.Command;
import com.kagire.data.dao.BankDao;
import com.kagire.util.Pair;

import java.util.List;
import java.util.UUID;

public class BankCommand extends Command {

    @Override
    public String name() {
        return "bank";
    }

    @Override
    public String description() {
        return """
            Executes specified operation with bank. Available operations:
            list                                                        - prints all banks
            add <name> <person fee> <company fee>                       - adds bank with specified name and payment fees
            update <name> <new name> <new person fee> <new company fee> - updates bank with specified name and payment fee
            delete <name>                                               - deletes bank with specified name""";
    }

    @Override
    public void execute(String[] args) {
        validateArgs(args, 2);
        String operation = args[1];

        var bankDao = new BankDao();
        switch (operation) {
            case "list" -> {
                var banks = bankDao.getAll();
                 if (banks.isEmpty())
                     System.out.println("There are no banks");
                 else
                     banks.forEach(bank -> System.out.printf("%s (person fee: %s, company fee: %s)%n",
                         bank.getName(), bank.getPersonFee(), bank.getCompanyFee()));

            }
            case "add" -> {
                validateArgs(args, 5);
                String bankName = args[2];
                double personFee = Double.parseDouble(args[3]);
                double companyFee = Double.parseDouble(args[4]);

                int rowsAffected = bankDao.save(List.of(
                    Pair.of("id", UUID.randomUUID().toString()),
                    Pair.of("name", bankName),
                    Pair.of("person_fee", personFee),
                    Pair.of("company_fee", companyFee)
                ));
                if (rowsAffected > 0) System.out.println("bank " + bankName + " added");

            }
            case "update" -> {
                validateArgs(args, 6);
                String bankName = args[2];
                String bankNewName = args[3];
                double bankNewPersonFee = Double.parseDouble(args[4]);
                double bankNewCompanyFee = Double.parseDouble(args[5]);

                int rowsAffected = bankDao.update(
                    List.of(
                        Pair.of("name", bankNewName),
                        Pair.of("person_fee", bankNewPersonFee),
                        Pair.of("company_fee", bankNewCompanyFee)
                    ),
                    Pair.of("name", bankName)
                );
                if (rowsAffected > 0) System.out.println("bank " + bankName + " updated to " + bankNewName);

            }
            case "delete" -> {
                validateArgs(args, 3);
                String bankName = args[2];

                int rowsAffected = bankDao.delete(Pair.of("name", bankName));
                if (rowsAffected > 0)  System.out.println("bank " + bankName + " deleted");
            }
        }
    }
}
