package com.structural.filter.ex1;

import java.util.List;

public interface Criteria {
   public List<Person> meetCriteria(List<Person> persons);
}
