/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.behavioral.interpreter;

import java.util.HashMap;

/**
 * @author : Rajashree Meganathan
 * @date : 11/21/12
 */
class Number implements Expression {
    private int number;
    public Number(int number)       { this.number = number; }
    public int interpret(HashMap<String,Expression> variables)  { return number; }
}
