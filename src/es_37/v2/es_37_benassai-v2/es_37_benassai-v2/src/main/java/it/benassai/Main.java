package it.benassai;

import it.benassai.Logic.PrimeCalculator;

public class Main {
    public static void main(String[] args) {
        
        PrimeCalculator calculator = new PrimeCalculator(30, 10);

        try {
            calculator.startAndWaitThreads();
        } catch (Exception e) {
            System.err.println("Somehting went wrong.");
        }

        System.out.println( calculator.calcPrimes() );
        System.out.println( calculator.calcPrimes().size() );

    }
}