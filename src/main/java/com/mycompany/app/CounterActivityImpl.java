package com.mycompany.app;

public class CounterActivityImpl implements CounterActivity{
    
    private static int counter = 0;

    @Override
    public int count() {
        return counter++;
    }

}
