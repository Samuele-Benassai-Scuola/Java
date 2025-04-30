package it.benassai.logic;

import java.util.ArrayList;
import java.util.List;

import it.benassai.logic.pojo.LogResult;
import lombok.Getter;

@Getter
public class Log {
    
    private List<LogResult> data;
    private static Log instance;

    public static Log getInstance() {
        if(instance == null)
            return new Log();
        
        return instance;
    }

    private Log() {
        this.data = new ArrayList<>();
    }

    public void add(LogResult e) {
        data.add(e);
    }

    public LogResult getLastResult() {
        return data.getLast();
    }

}
