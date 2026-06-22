
package com.firstbank.model;

public class FixedDepositAccount extends Account {
    public FixedDepositAccount(String a,String b,String c,String d,String e,String f,String g,String h,double i){
        super(a,b,c,d,e,f,g,h,i);
    }
    public double minimumDeposit(){ return 1000000; }
    public String accountTypeName(){ return "FixedDeposit"; }
}
