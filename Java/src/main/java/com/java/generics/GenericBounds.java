package com.java.generics;

class Stats<T extends Number> {
  T[] nums;

  Stats(T[] o) {
    nums = o;
  }

  double average() {
    double sum = 0.0;

    for (int i = 0; i < nums.length; i++)
      sum += nums[i].doubleValue();

    return sum / nums.length;
  }
}

public class GenericBounds {
  public static void main(String args[]) {
    Integer inums[] = { 1, 2, 3, 4, 5 };
    Stats1<Integer> iob = new Stats1<Integer>(inums);
    double v = iob.average();
    System.out.println("iob average is " + v);

    Double dnums[] = { 1.1, 2.2, 3.3, 4.4, 5.5 };
    Stats1<Double> dob = new Stats1<Double>(dnums);
    double w = dob.average();
    System.out.println("dob average is " + w);

  }
}
        