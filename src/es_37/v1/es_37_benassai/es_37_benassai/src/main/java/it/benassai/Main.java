package it.benassai;

import java.util.ArrayList;
import java.util.List;

import it.benassai.Logic.PartialAdders;

public class Main {
    public static void main(String[] args) {
        final List<Integer> list = new ArrayList<>();

        for(int i = 0; i < 100000000; i++)
            list.add((int)(Math.random() * 11));
        
        PartialAdders adders = new PartialAdders(list, 1000);

        try {
            adders.startAndWaitThreadsAdders();
        } catch (InterruptedException e) {
            System.err.println( "Something went wrong." );
        }

        System.out.println( adders.calcSum() );
    }
}