package com.behavioral.interpreter.ex1;

import java.util.HashMap;

interface Expression {
    public int interpret(HashMap<String, Expression> variables);
}
