package com.god.damn;

/**
 * Created by 1 on 16.09.2016.
 */
public class Accounting {//accounting, aga
        public int Id;
        public int Role_Id;
        public String Amount;
        public String Date_st;
        public String Date_end;

        public Accounting(int id, int role_Id, String amount, String date_st, String date_end) {
                Id = id;
                Role_Id = role_Id;
                Amount = amount;
                Date_st = date_st;
                Date_end = date_end;
        }
}
