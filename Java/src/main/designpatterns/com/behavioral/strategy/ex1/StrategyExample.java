/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.behavioral.strategy.ex1;

/**
 * @author : Rajashree Meganathan
 * @date : 11/21/12
 */
public class StrategyExample {

    public static void main(String[] args) {

        Context context;

        // Three contexts following different strategies
        context = new Context(new ConcreteStrategyAdd());
        int resultA = context.executeStrategy(3,4);

        context = new Context(new ConcreteStrategySubtract());
        int resultB = context.executeStrategy(3,4);

        context = new Context(new ConcreteStrategyMultiply());
        int resultC = context.executeStrategy(3,4);
    }
}

// The context class uses this to call the concrete strategy
interface Strategy {
    int execute(int a, int b);
}

// Implements the algorithm using the strategy interface
class ConcreteStrategyAdd implements com.behavioral.strategy.ex1.Strategy {

    public int execute(int a, int b) {
        System.out.println("Called ConcreteStrategy Add's execute()");
        return a + b;  // Do an addition with a and b
    }
}

class ConcreteStrategySubtract implements com.behavioral.strategy.ex1.Strategy {

    public int execute(int a, int b) {
        System.out.println("Called ConcreteStrategy Subtract's execute()");
        return a - b;  // Do a subtraction with a and b
    }
}

class ConcreteStrategyMultiply implements com.behavioral.strategy.ex1.Strategy {

    public int execute(int a, int b) {
        System.out.println("Called ConcreteStrategy Multiply's execute()");
        return a * b;   // Do a multiplication with a and b
    }
}

// Configured with a ConcreteStrategy object and maintains a reference to a Strategy object
class Context {

    private com.behavioral.strategy.ex1.Strategy strategy;

    // Constructor
    public Context(com.behavioral.strategy.ex1.Strategy strategy) {
        this.strategy = strategy;
    }

    public int executeStrategy(int a, int b) {
        return strategy.execute(a, b);
    }
}