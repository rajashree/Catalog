package com.creational.abstractfactory.ex1;
public class Parts {
  	/**
* specification of Part of Computer, String
*/
public String specification;

/**
* Constructor sets the name of OS
* @param specification of Part of Computer
*/
public Parts(String specification) {
this.specification = specification;
}

/**
* Returns the name of the part of Computer
*
* @return specification of Part of Computer, String
*/
public String getSpecification() {
return specification;
}

}// End of class
