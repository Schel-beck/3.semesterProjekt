package app;

import app.entities.Unicorn;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        UnicornDAO unicornDAO = new UnicornDAO();
        Unicorn unicorn = new Unicorn(1,"Harry",12,21);
       unicornDAO.delete(1);


    }
}