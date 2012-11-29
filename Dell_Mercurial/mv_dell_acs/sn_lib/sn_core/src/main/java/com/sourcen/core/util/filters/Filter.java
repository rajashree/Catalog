package com.sourcen.core.util.filters;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 0 $, $Date:: 2000-01-01 00:00:01#$
 */
public interface Filter<I, O> {

    O execute(I input);

}
