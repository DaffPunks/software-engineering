package com.god.damn;

import java.util.Date;


public class Accounting {
    public int Role_Id;
    public int Amount;
    public Date Date_st;
    public Date Date_end;

    public Accounting(int role_Id, int amount, Date date_st, Date date_end) {
        Role_Id = role_Id;
        Amount = amount;
        Date_st = date_st;
        Date_end = date_end;
    }
}
