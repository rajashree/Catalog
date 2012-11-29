package com.dell.acs.auth;

import com.dell.acs.managers.APIKeyActivityManager;
import com.dell.acs.managers.AuthenticationManager;
import com.dell.acs.persistence.domain.APIKeyActivity;
import com.dell.acs.persistence.domain.User;
import com.sourcen.core.config.ConfigurationServiceImpl;
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.web.filter.AbstractFilter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;

/**
 * AccessKeyFilter is responsible for the authentication of all the REST endpoints
 * served by Content Server.
 * <p/>
 * Content Server expects an REQUEST Header "Authorization" on all the incoming
 * requests.
 * <p/>
 * The "Authorization" header value should have the following structure
 * Authorization="<AccessKey>:<SignData>"
 * <p/>
 * Some docs for reference, see
 * {@link http://msdn.microsoft.com/en-us/library/windowsazure/dd179428.aspx}
 * {@link http://s3.amazonaws.com/doc/s3-developer-guide/RESTAuthentication.html}
 * <p/>
 * User: vivek
 * Date: 7/4/12
 * Time: 4:17 PM
 */
public class AccessKeyFilter extends AbstractFilter {

    private static final Logger log = Logger.getLogger(AccessKeyFilter.class);
    private final List<String> skipList = Arrays.asList("signature","accessKey","jsonp");

    @Autowired
    private AuthenticationManager customAuthenticationManager;

    @Autowired
    private APIKeyActivityManager apiKeyActivityManager;

    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) throws ServletException {
        if(ConfigurationServiceImpl.getInstance().isDevMode()){
            Boolean enabled = getConfigurationService().getBooleanProperty(getClass(), "isEnabled", false);
            return !enabled;
        }
        return super.shouldNotFilter(request);
    }

    /**
     * check if authentcation token is present.
     * enable HTTP Basic authentication according to HTTP1.0 spec
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     * @see http://www.w3.org/Protocols/HTTP/1.0/spec.html#WWW-Authenticate
     */
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain filterChain) throws ServletException, IOException {

            int delim = 0;
            //Authorization: ce7b9e7472b849ac822bce203b0eb5ab:[B@193385d

            //Fetch the header from REQUEST
            //Split the header values to get ACCESS_KEY and SIGNED DATA
            //Use the ACCESS_KEY to load the corresponding SECRET_KEY
            //Compute the HMAC to verify the singed data matches.
            //If Signed Data is valid, then load the GrantedAuthority for the specified User.
            //Else throw an Authentication Exception which returns the response
            //http://self-issued.info/docs/draft-ietf-oauth-v2-bearer.html#authz-header
            String authHeader = request.getHeader("Authorization");
            APIKeyActivity apiKeyActivity = new APIKeyActivity();

        // moved the private variables into the scope. Filters are singleton, so on concurrent requests
        // this will break if its Private variables.
         String accessKey = null;
         String singedRequestData = null;
         Authentication authentication = null;
         Authentication requestAuth = null;
         String requestURL = null;

        if (StringUtils.isNotEmpty(authHeader)) {
                log.debug("Header found    " + authHeader);
                delim = authHeader.indexOf(":");
                if (delim == -1) {
                    throw new BadCredentialsException("Invalid authentication header");
                }
                requestAuth = createAuthentication(request, authHeader, delim);

            } else if ( StringUtils.isNotEmpty(request.getQueryString()) && request.getQueryString().contains("jsonp") ){  //CS-561: To Support JSONP calls, where we cannot set the REQUEST Header 'Authorization'
                //Here is a good reference point - http://stackoverflow.com/questions/10113911/sending-authorization-headers-with-jquery-and-ajax
                log.debug("\t Handle JSONP request \t");
                if(validateJSONPRequest(request, response)){
                    requestAuth = createAuthenticationFromRequest(request);
                }
                else {
                    return;
                }

            } else {
                log.error("The request requires HTTP authentication. Missing header.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.addHeader("WWW-Authenticate", "error=invalid_request \n" +
                        "error_description=\"Incorrect Authentication Information.");
                return;

            }
        accessKey = (String) requestAuth.getCredentials();
        singedRequestData = (String) requestAuth.getPrincipal();
        requestURL = (String) requestAuth.getDetails();
            log.debug("API Key Tracking \t -  AuthHeader : " + accessKey + ":" + singedRequestData +
                    "    \t RequestURL : " + requestURL + " \t IP Address : " + request.getRemoteAddr());
            //Refactored the code for CS-561
            if(requestAuth != null){

                    try {
                        authentication = this.customAuthenticationManager.authenticate(requestAuth);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } catch (BadCredentialsException ex) {
                        log.error(" API Key authentication failed  : " + ex.getMessage());
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.addHeader("WWW-Authenticate", "error=invalid_request \n" +
                                "error_description=" + ex.getMessage());
                        return;
                    }

                    String username = ((User) authentication.getPrincipal()).getUsername();
                    log.debug(" User Name  " + username);
                    //Set the API Key meterics for tracking
                    apiKeyActivity.setApiKey(accessKey);
                    apiKeyActivity.setIPAddress(request.getRemoteAddr());
                    //Need to reconstruct the complete requestURL, as for JSONP we are escaping the following params - "signature","accessKey","jsonp"
                    apiKeyActivity.setRequestURL(request.getRequestURL().toString()+"?"+request.getQueryString());
                    apiKeyActivity.setUsername(username);
                    apiKeyActivity.setAccessedTime((Calendar.getInstance().getTime()));
                    //Save the API metrics
                    apiKeyActivityManager.saveMetrics(apiKeyActivity);
            }

        filterChain.doFilter(request, response);
    }

    /**
     * JSONP request URL validtor
     * @param request
     */
    private boolean validateJSONPRequest(HttpServletRequest request, HttpServletResponse response ){

        if(!request.getQueryString().contains("signature")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.addHeader("WWW-Authenticate", "error=invalid_request \n" +
                    "Request parameter 'signature' missing.");
            return false;
        } else {
            if( StringUtils.isEmpty(request.getParameter("signature")) ){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.addHeader("WWW-Authenticate", "error=invalid_request \n" +
                    "Request parameter 'signature' must have text; it must not be null, empty, or blank.");
            return false;
            }
        }
        if( !request.getQueryString().contains("accessKey") ){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.addHeader("WWW-Authenticate", "error=invalid_request \n" +
                    "Request parameter 'accessKey' missing. ");
            return false;
        } else {
            if( StringUtils.isEmpty(request.getParameter("accessKey")) ){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.addHeader("WWW-Authenticate", "error=invalid_request \n" +
                        "Request parameter 'accessKey' must have text; it must not be null, empty, or blank.");
                return false;
            }
        }
        return true;
    }

    /**
     * Helper method to create an MarketVineAuthentication object ONLY from HttpServletRequest.
     * This is to support JSONP calls ONLY - CS-561
     * @param httpRequest {@link HttpServletRequest}
     * @return MarketVineAuthentication
     */
    private Authentication createAuthenticationFromRequest(HttpServletRequest httpRequest) {
        log.debug("                 CreateAuthenticationFromRequest for JSONP                        ");
        StringBuffer requestURLBuffer = httpRequest.getRequestURL().append("?");
        Enumeration<String> params = httpRequest.getParameterNames();
        String accessKey = httpRequest.getParameter(skipList.get(1));
        String singedRequestData = httpRequest.getParameter(skipList.get(0));
        int i = 0;
        while(params.hasMoreElements()){
            String name = params.nextElement();
            if( !skipList.contains(name.trim()) )
            {
                if(i == 0)
                {
                    requestURLBuffer.append(name+"="+httpRequest.getParameter(name));
                }
                else {
                    requestURLBuffer.append("&"+name+"="+httpRequest.getParameter(name));
                }
                i++;
            }
        }
        String requestURL = requestURLBuffer.toString().trim();
        log.debug("\t JSONP \t RequestURL \t\t "+requestURL);
        return new MarketVineAuthentication(singedRequestData, accessKey, requestURL);
    }

    /**
     * Helper method to create an MarketVineAuthentication object from REQUEST Header
     * @param httpRequest {@link HttpServletRequest}
     * @param authHeader - Authorization Header
     * @param delim - int delimiter
     * @return MarketVineAuthentication
     */
    private Authentication createAuthentication(HttpServletRequest httpRequest, String authHeader, int delim) {
        String[] tokens = new String[]{authHeader.substring(0, delim), authHeader.substring(delim + 1)};
        assert tokens.length == 2;
        String accessKey = tokens[0];
        String  singedRequestData = tokens[1];
        String requestURL = httpRequest.getRequestURL().toString().trim();
        if (StringUtils.isNotEmpty(httpRequest.getQueryString())) {
            requestURL = requestURL.concat("?").concat(httpRequest.getQueryString());
        }
        log.debug("Access Key  " + accessKey + "   Signed Data  " + singedRequestData);
        log.debug("Requesting URL : " + requestURL);

        return new MarketVineAuthentication(singedRequestData, accessKey, requestURL);
    }


}
