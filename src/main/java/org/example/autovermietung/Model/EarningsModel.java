package org.example.autovermietung.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EarningsModel {
    private List<Earning> earnings = new ArrayList<>();
     public void addEarning(Earning e){
         earnings.add(e);
     }
     public List<Earning> getEarnings(){
        return earnings;
     }
    public double getSummary() {
        return earnings.stream().mapToDouble(Earning::getBetrag).sum();
    }
}