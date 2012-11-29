package com.dell.acs.web.ws.v2.rest;

import com.dell.acs.web.controller.BaseDellController;
import com.sourcen.core.web.ws.WebService;
import org.springframework.stereotype.Component;

/**
 * @author : vivek
 */
@Component(value = "WebServiceV2")
public abstract class WebServiceImpl extends BaseDellController implements WebService {
}
