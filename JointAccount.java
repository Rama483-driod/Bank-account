
package com.firstbank.model;

public class JointAccount extends Account {
    public JointAccount(String a,String b,String c,String d,String e,String f,String g,String h,double i){
        super(a,b,c,d,e,f,g,h,i);
    }
    public double minimumDeposit(){ return 100000; }
    public String accountTypeName(){ return "Joint"; }
}
