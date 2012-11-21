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
class Variable implements Expression {
    private String name;
    public Variable(String name)       { this.name = name; }
    public int interpret(HashMap<String,Expression> variables)  {
        if(null==variables.get(name)) return 0; //Either return new Number(0).
        return variables.get(name).interpret(variables);
    }
}
