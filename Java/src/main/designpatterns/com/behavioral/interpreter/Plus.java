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
class Plus implements Expression {
    Expression leftOperand;
    Expression rightOperand;
    public Plus(Expression left, Expression right) {
        leftOperand = left;
        rightOperand = right;
    }

    public int interpret(HashMap<String,Expression> variables)  {
        return leftOperand.interpret(variables) + rightOperand.interpret(variables);
    }
}
