
package com.dell.acs;

public class CampaignForInactiveRetailerException extends ACSException {

    private static final long serialVersionUID = -281519022344503929L;

    public CampaignForInactiveRetailerException() {
        super();
    }

    public CampaignForInactiveRetailerException(String msg) {
        super(msg);
    }

    public CampaignForInactiveRetailerException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public CampaignForInactiveRetailerException(Throwable throwable) {
        super(throwable);
    }
}