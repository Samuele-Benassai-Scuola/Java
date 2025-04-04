package it.benassai;

import it.benassai.Logic.AbstractMathCalculator;
import it.benassai.Logic.ProductMathCalculator;
import it.benassai.Logic.SumMathCalculator;

public class Main {
    public static void main(String[] args) {
        
        final AbstractMathCalculator sumCalc = new SumMathCalculator();
        final AbstractMathCalculator productCalc = new ProductMathCalculator();

        System.out.println( sumCalc.execute(3.0, 4.0) );
        System.out.println( productCalc.execute(3.0, 4.0) );

    }
}