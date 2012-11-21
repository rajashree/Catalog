package com.behavioral.interpreter;

import java.util.HashMap;

interface Expression {
    public int interpret(HashMap<String, Expression> variables);
}
