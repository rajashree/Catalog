package com.structural.decorator.ex2;

//Decorator Whip that mixes whip with coffee
//note it extends CoffeeDecorator
public class Whip extends CoffeeDecorator
{

  double cost;
  String ingredient;

  public Whip(Coffee decoratedCoffee)
  {
      super(decoratedCoffee);
      cost = 0.7;
      ingredient = "Whip";
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

