package it.benassai.Logic;

import java.util.function.Consumer;
import java.util.function.Supplier;

import it.benassai.Logic.Interfaces.MathOperation;

public abstract class AbstractMathCalculator {

    final protected Consumer<String> printer = (s) -> {
        System.out.println( "Risultato dell'operazione: " +  s );
    };

    final protected Supplier<Double> randomDouble = () -> {
        final int MAX = 1000;

        return Math.random() * MAX;
    };

    public abstract MathOperation<Double> getOperation();
    
    public Double execute(Double num1, Double num2) {
        final Double result = getOperation().calculate(num1, num2);

        printer.accept(result.toString());
        System.out.println( "Random: " + randomDouble.get() );

        return result;
    }

}
