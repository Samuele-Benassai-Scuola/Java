package it.benassai.Logic;

import java.util.ArrayList;
import java.util.List;

public class PartialPrimeCalculator implements Runnable {

    private int low;
    private int high;

    private boolean finished = false;
    private List<Integer> primes = new ArrayList<>();

    public PartialPrimeCalculator(int low, int high) {
        this.low = low;
        this.high = high;
    }

    public int getLow() {
        return low;
    }

    public int getHigh() {
        return high;
    }

    public boolean isFinished() {
        return finished;
    }

    public List<Integer> getPrimes() {
        return primes;
    }

    @Override
    public void run() {
        int i = low;

        while(i < high) {
            if(isPrime(i))
                primes.add(i);

            i++;
        }

        finished = true;
    }

    private boolean isPrime(int num) {
        for(int i = 2; i <= num / 2; i++)
            if(num % i == 0)
                return false;
        return true;
    }
    
}
