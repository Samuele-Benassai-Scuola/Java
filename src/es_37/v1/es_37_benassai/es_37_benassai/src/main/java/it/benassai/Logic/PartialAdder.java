package it.benassai.Logic;

import java.util.ArrayList;
import java.util.List;

public class PartialAdder implements Runnable {
    
    private List<Integer> list = new ArrayList<>();

    private int low;
    private int high;

    private boolean finished = false;
    private int sum = 0;

    public PartialAdder(List<Integer> list, int low, int high) {
        this.list = list;
        this.low = low;
        this.high = high;
    }

    public List<Integer> getList() {
        return list;
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

    public int getSum() {
        return sum;
    }

    @Override
    public void run() {
        int i = low;

        while(i < high) {
            sum += list.get(i);

            i++;
        }

        finished = true;
    }
    
}
