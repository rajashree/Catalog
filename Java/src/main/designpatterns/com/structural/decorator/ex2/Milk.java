package com.structural.decorator.ex2;

//Decorator Milk that mixes milk with coffee
//note it extends CoffeeDecorator
public class Milk extends CoffeeDecorator
{

  double cost;
  String ingredient;

  public Milk(Coffee decoratedCoffee)
  {
      super(decoratedCoffee);
      cost = 0.5;
      ingredient = "Milk";
  }

  public double getCost()
  {
      return super.getCost() + cost;
  }

  public String getIngredient()
  {
      return super.getIngredient() + super.ingredientSeparator + ingredient;
  }
}

