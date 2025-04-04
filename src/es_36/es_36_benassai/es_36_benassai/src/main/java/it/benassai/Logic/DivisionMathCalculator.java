package it.benassai.Logic;

import it.benassai.Logic.Interfaces.MathOperation;

public class DivisionMathCalculator extends AbstractMathCalculator {

    final MathOperation<Double> operation = (a, b) -> a / b;

    @Override
    public MathOperation<Double> getOperation() {
        return operation;
    }

}
