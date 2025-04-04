package it.benassai.Logic;

import java.util.ArrayList;
import java.util.List;

public class PartialAdders {

    private List<Integer> list = new ArrayList<>();
    private List<PartialAdder> adders = new ArrayList<>();

    private int addersQuantity;

    public PartialAdders(List<Integer> list, int addersQuantity) {
        this.list = list;
        this.addersQuantity = addersQuantity;

        this.initAdders();
    }

    public List<Integer> getList() {
        return list;
    }

    public List<PartialAdder> getAdders() {
        return adders;
    }

    public int getAddersQuantity() {
        return addersQuantity;
    }

    private void initAdders() {
        int i = 0;
        int prevIndex = 0;
        int nextIndex = 0;
        int deltaIndex = list.size() / addersQuantity;

        while(i < addersQuantity) {
            nextIndex = prevIndex + deltaIndex;

            adders.add(new PartialAdder(list, prevIndex, nextIndex));

            prevIndex = nextIndex;
            i++;
        }
    }

    public void startAndWaitThreadsAdders() throws InterruptedException {
        final List<Thread> threads = new ArrayList<>();

        for(PartialAdder adder : adders)
            threads.add(new Thread(adder));
        for(Thread thread : threads)
            thread.start();
        for(Thread thread : threads)
            thread.join();
        
    }

    public int calcSum() {
        int i = 0;
        int sum = 0;

        while(i < adders.size()) {
            if(!adders.get(i).isFinished())
                throw new IllegalStateException();
            
            sum += adders.get(i).getSum();

            i++;
        }

        return sum;
    }
    
}
