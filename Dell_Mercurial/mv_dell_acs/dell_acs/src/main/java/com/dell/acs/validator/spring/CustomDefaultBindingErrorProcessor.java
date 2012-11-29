package com.dell.acs.validator.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyAccessException;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DefaultBindingErrorProcessor;
import org.springframework.validation.FieldError;

/**
 @author Ashish
 @author $LastChangedBy: Ashish $
 @version $Revision: 1595 $, $Date:: 6/6/12 2:42 PM#$ */
public class CustomDefaultBindingErrorProcessor extends DefaultBindingErrorProcessor {

    private Logger logger = LoggerFactory.getLogger(CustomDefaultBindingErrorProcessor.class);

    /**
     {@inheritDoc}
     */
    @Override
    public void processMissingFieldError(final String missingField, final BindingResult bindingResult) {
        super.processMissingFieldError(missingField, bindingResult);
    }

    /**
     {@inheritDoc}
     */
    @Override
    public void processPropertyAccessException(final PropertyAccessException ex, final BindingResult bindingResult) {

        // Create field error with the exceptions's code, e.g. "typeMismatch".
        String field = ex.getPropertyName();
        String[] codes = bindingResult.resolveMessageCodes(ex.getErrorCode(), field);
        Object[] arguments = getArgumentsForBindError(bindingResult.getObjectName(), field);
        Object rejectedValue = ex.getValue();
        if (rejectedValue != null && rejectedValue.getClass().isArray()) {
            rejectedValue = StringUtils.arrayToCommaDelimitedString(ObjectUtils.toObjectArray(rejectedValue));
        }
        if (field.endsWith("Date")) {
            logger.info("Rejected Value For Date===>" + rejectedValue.toString());
            if (!com.sourcen.core.util.StringUtils.isEmpty(rejectedValue.toString())) {
                bindingResult.addError(new FieldError(
                        bindingResult.getObjectName(), field, rejectedValue, true,
                        codes, arguments, "Invalid " + field + " format"));
            }
        } else {
            bindingResult.addError(new FieldError(
                    bindingResult.getObjectName(), field, rejectedValue, true,
                    codes, arguments, ex.getLocalizedMessage()));
        }
    }
}
