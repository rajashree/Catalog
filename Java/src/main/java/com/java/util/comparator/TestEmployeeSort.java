package com.java.util.comparator;

import java.util.*;

public class TestEmployeeSort {

    public static void main(String[] args) {

        List coll = Util.getEmployees();
        Collections.sort(coll, new EmpSortByName());
     // Comparator EmpSortByName = Collections.reverseOrder();
     // Collections.sort(coll, new EmpSortByName());
        printList(coll);

    }

    private static void printList(List<Employee> list) {
        System.out.println("EmpId\tName\tAge");
        for (Employee e: list) {
            System.out.println(e.getEmpId() + "\t" + e.getName() + "\t" + e.getAge()+ "\t" + e.getDate());
        }
    }
}