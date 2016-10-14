package com.god.damn;

import java.time.LocalDate;


public class Accounting {
    public int Id;
    public int Role_Id;
    public int Amount;
    public LocalDate Date_st;
    public LocalDate Date_end;

    public Accounting(int id, int role_Id, int amount, LocalDate date_st, LocalDate date_end) {
        Id = id;
        Role_Id = role_Id;
        Amount = amount;
        Date_st = date_st;
        Date_end = date_end;
    }

    public Accounting(int role_Id, int amount, LocalDate date_st, LocalDate date_end) {
        Role_Id = role_Id;
        Amount = amount;
        Date_st = date_st;
        Date_end = date_end;
    }
}
