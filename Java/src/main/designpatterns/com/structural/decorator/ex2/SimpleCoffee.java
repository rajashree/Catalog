package com.structural.decorator.ex2;

//implementation of simple coffee without any extra ingredients
public class SimpleCoffee implements Coffee
{
 
    double cost;
    String ingredient;
 
    public SimpleCoffee()
    {
        cost = 1;
        ingredient = "Coffee";
    }
 
    public double getCost()
    {
        return cost;
    }
 
    public String getIngredient()
    {
        return ingredient;
    }
}
