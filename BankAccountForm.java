
package com.firstbank.ui;

import javax.swing.*;
import java.awt.*;
import java.time.*;
import com.firstbank.model.*;
import com.firstbank.util.*;

public class BankAccountForm extends JFrame {
    JTextField fn=new JTextField(),ln=new JTextField(),nin=new JTextField(),
            email=new JTextField(),cemail=new JTextField(),phone=new JTextField(),
            pin=new JTextField(),cpin=new JTextField(),deposit=new JTextField(),
            secondNin=new JTextField();

    JComboBox<Integer> year = new JComboBox<>();
    JComboBox<String> month = new JComboBox<>(new String[]{
            "January","February","March","April","May","June",
            "July","August","September","October","November","December"});
    JComboBox<Integer> day = new JComboBox<>();

    JComboBox<String> account = new JComboBox<>(new String[]{
            "Savings","Current","Fixed Deposit","Student","Joint"});

    JComboBox<String> branch = new JComboBox<>(new String[]{
            "Kampala","Gulu","Mbarara","Jinja","Mbale"});

    JTextArea summary = new JTextArea(6,40);

    public BankAccountForm(){
        setTitle("First Bank Uganda - New Account");
        setSize(800,700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel p = new JPanel(new GridLayout(0,2,5,5));

        addField(p,"First Name",fn);
        addField(p,"Last Name",ln);
        addField(p,"National ID",nin);
        addField(p,"Email",email);
        addField(p,"Confirm Email",cemail);
        addField(p,"Phone",phone);
        addField(p,"PIN",pin);
        addField(p,"Confirm PIN",cpin);
        addField(p,"Second NIN (Joint only)",secondNin);

        for(int i=1950;i<=LocalDate.now().getYear();i++) year.addItem(i);
        updateDays();
        year.addActionListener(e->updateDays());
        month.addActionListener(e->updateDays());

        p.add(new JLabel("Year")); p.add(year);
        p.add(new JLabel("Month")); p.add(month);
        p.add(new JLabel("Day")); p.add(day);

        p.add(new JLabel("Account Type")); p.add(account);
        p.add(new JLabel("Branch")); p.add(branch);
        addField(p,"Opening Deposit",deposit);

        JButton submit = new JButton("Submit");
        JButton reset = new JButton("Reset");

        submit.addActionListener(e->submit());
        reset.addActionListener(e->reset());

        JPanel buttons = new JPanel();
        buttons.add(submit);
        buttons.add(reset);

        summary.setEditable(false);

        setLayout(new BorderLayout());
        add(new JScrollPane(p),BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout());
        south.add(buttons,BorderLayout.NORTH);
        south.add(new JLabel("Account Summary is Below:"),BorderLayout.CENTER);
        south.add(new JScrollPane(summary),BorderLayout.SOUTH);
        add(south,BorderLayout.SOUTH);
    }

    private void addField(JPanel p,String name,JTextField t){
        p.add(new JLabel(name));
        p.add(t);
    }

    private void updateDays(){
        day.removeAllItems();
        int y=(Integer)year.getSelectedItem();
        int m=month.getSelectedIndex()+1;
        int days=YearMonth.of(y,m).lengthOfMonth();
        for(int i=1;i<=days;i++) day.addItem(i);
    }

    private void submit(){
        try{
            if(fn.getText().trim().length()<2) throw new Exception("Invalid first name");
            if(!email.getText().equals(cemail.getText())) throw new Exception("Emails do not match");
            if(!pin.getText().equals(cpin.getText())) throw new Exception("PINs do not match");

            String dob = String.format("%04d-%02d-%02d",
                    year.getSelectedItem(),
                    month.getSelectedIndex()+1,
                    day.getSelectedItem());

            LocalDate birth = LocalDate.parse(dob);
            int age = Period.between(birth,LocalDate.now()).getYears();
            if(age<18 || age>75) throw new Exception("Age must be between 18 and 75");

            String type = (String)account.getSelectedItem();
            double dep = Double.parseDouble(deposit.getText());

            String accNo = AccountNumberGenerator.generate((String)branch.getSelectedItem());

            Account a;

            switch(type){
                case "Savings":
                    a = new SavingsAccount(accNo,fn.getText(),ln.getText(),nin.getText(),
                            email.getText(),phone.getText(),dob,
                            (String)branch.getSelectedItem(),dep);
                    break;
                case "Current":
                    a = new CurrentAccount(accNo,fn.getText(),ln.getText(),nin.getText(),
                            email.getText(),phone.getText(),dob,
                            (String)branch.getSelectedItem(),dep);
                    break;
                case "Fixed Deposit":
                    a = new FixedDepositAccount(accNo,fn.getText(),ln.getText(),nin.getText(),
                            email.getText(),phone.getText(),dob,
                            (String)branch.getSelectedItem(),dep);
                    break;
                case "Student":
                    if(age>25) throw new Exception("Student account age is 18-25");
                    a = new StudentAccount(accNo,fn.getText(),ln.getText(),nin.getText(),
                            email.getText(),phone.getText(),dob,
                            (String)branch.getSelectedItem(),dep);
                    break;
                default:
                    if(secondNin.getText().trim().isEmpty())
                        throw new Exception("Joint account requires second NIN");
                    a = new JointAccount(accNo,fn.getText(),ln.getText(),nin.getText(),
                            email.getText(),phone.getText(),dob,
                            (String)branch.getSelectedItem(),dep);
            }

            if(dep < a.minimumDeposit())
                throw new Exception("Minimum deposit is UGX " + (long)a.minimumDeposit());

            String rec = a.summary();
            summary.append(rec + "\n");
            DatabaseManager.save(rec);
            JOptionPane.showMessageDialog(this,"Account Created!");

        }catch(Exception ex){
            JOptionPane.showMessageDialog(this,ex.getMessage());
        }
    }

    private void reset(){
        for(Component c : getContentPane().getComponents()){}
        fn.setText("");ln.setText("");nin.setText("");email.setText("");
        cemail.setText("");phone.setText("");pin.setText("");
        cpin.setText("");deposit.setText("");secondNin.setText("");
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new BankAccountForm().setVisible(true));
    }
}
