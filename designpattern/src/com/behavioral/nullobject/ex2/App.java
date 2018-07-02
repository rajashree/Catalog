/**
 * The MIT License
 * Copyright (c) 2014-2016 Ilkka Seppälä
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.behavioral.nullobject.ex2;
/**
 * 
 * Null Object pattern replaces null values with neutral objects. Many times this simplifies
 * algorithms since no extra null checks are needed.
 * <p>
 * In this example we build a binary tree where the nodes are either normal or Null Objects. No null
 * values are used in the tree making the traversal easy.
 *
 */
public class App {
  /**
   * Program entry point
   * 
   * @param args command line args
   */
  public static void main(String[] args) {

    Node root =
        new NodeImpl("1", new NodeImpl("11", new NodeImpl("111", NullNode.getInstance(),
            NullNode.getInstance()), NullNode.getInstance()), new NodeImpl("12",
            NullNode.getInstance(), new NodeImpl("122", NullNode.getInstance(),
                NullNode.getInstance())));

    root.walk();
  }
}
