package com.structural.decorator.ex2;

//Decorator Sprinkles that mixes sprinkles with coffee
//note it extends CoffeeDecorator
public class Sprinkles extends CoffeeDecorator
{

  double cost;
  String ingredient;

  public Sprinkles(Coffee decoratedCoffee)
  {
      super(decoratedCoffee);
      cost = 0.2;
      ingredient = "Sprinkles";
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
