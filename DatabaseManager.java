
package com.firstbank.util;

import java.io.*;

public class DatabaseManager {
    public static void save(String record){
        try(PrintWriter pw = new PrintWriter(new FileWriter("accounts.csv",true))){
            pw.println(record);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
