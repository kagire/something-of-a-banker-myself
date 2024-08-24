package com.kagire.control;

import com.kagire.command.CommandOperator;
import com.kagire.exception.CommandException;

import java.util.Scanner;

public class TerminalControl {

    public void run() {

        Scanner sc = new Scanner(System.in);
        System.out.println("Type \"help\" to get list of available options");

        while(true) {
            try {
                System.out.println("Enter command");

                String input = sc.nextLine();
                CommandOperator.executeWith(input);

            } catch (CommandException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
