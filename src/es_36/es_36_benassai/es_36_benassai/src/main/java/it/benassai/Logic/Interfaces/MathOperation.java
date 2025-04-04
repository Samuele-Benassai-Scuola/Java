package it.benassai.Logic.Interfaces;

public interface MathOperation<T extends Number> {
    
    public T calculate(T num1, T num2);

}
