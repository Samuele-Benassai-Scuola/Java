package it.benassai.Logic;

import java.util.ArrayList;
import java.util.List;

public class PrimeCalculator {

    private int top;
    private int low = 2;
    private List<PartialPrimeCalculator> primeCalculators = new ArrayList<>();

    private int calculatorsQuantity;

    public PrimeCalculator(int top, int calculatorsQuantity) {
        this.top = top;
        this.calculatorsQuantity = calculatorsQuantity;

        this.initCalculators();
    }

    public int getTop() {
        return top;
    }

    public List<PartialPrimeCalculator> getcalculators() {
        return primeCalculators;
    }

    public int getCalculatorsQuantity() {
        return calculatorsQuantity;
    }

    private void initCalculators() {
        int i = 0;
        int prevIndex = 2;
        int nextIndex;
        int deltaIndex = (top - low) / calculatorsQuantity;

        while(i < calculatorsQuantity) {
            nextIndex = prevIndex + deltaIndex;
            if(i == calculatorsQuantity - 1)
                nextIndex = top;

            primeCalculators.add(new PartialPrimeCalculator(prevIndex, nextIndex));

            prevIndex = nextIndex;
            i++;
        }
    }

    public void startAndWaitThreads() throws InterruptedException {
        final List<Thread> threads = new ArrayList<>();

        for(PartialPrimeCalculator primeCalculator : primeCalculators)
            threads.add(new Thread(primeCalculator));
        for(Thread thread : threads)
            thread.start();
        for(Thread thread : threads)
            thread.join();
        
    }

    public List<Integer> calcPrimes() {
        int i = 0;
        List<Integer> primes = new ArrayList<>();

        while(i < primeCalculators.size()) {
            if(!primeCalculators.get(i).isFinished())
                throw new IllegalStateException();
            
            primes.addAll(primeCalculators.get(i).getPrimes());

            i++;
        }

        return primes;
    }
    
}
