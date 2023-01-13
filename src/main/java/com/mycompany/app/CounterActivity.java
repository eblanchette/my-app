package com.mycompany.app;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

/**
 * Hello world!
 *
 */
@ActivityInterface
public interface CounterActivity 
{
    @ActivityMethod
    public int count();
}
