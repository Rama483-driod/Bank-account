
package com.firstbank.model;

public class StudentAccount extends Account {
    public StudentAccount(String a,String b,String c,String d,String e,String f,String g,String h,double i){
        super(a,b,c,d,e,f,g,h,i);
    }
    public double minimumDeposit(){ return 10000; }
    public String accountTypeName(){ return "Student"; }
}
