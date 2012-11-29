package com.dell.acs.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 *
 * User: vivek
 * Date: 7/6/12
 * Time: 1:25 PM
 *
 */
public class MarketVineAuthentication extends AbstractAuthenticationToken //implements Authentication {
{

    private final Object principal;//signedData
    private Object credentials;//accessKey
    private Object details;//RequestURI

    //~ Constructors ===================================================================================================

    /**
     * This constructor can be safely used by any code that wishes to create a
     * <code>MarketVineAuthentication</code>, as the {@link
     * #isAuthenticated()} will return <code>false</code>.
     *
     */
    public MarketVineAuthentication(Object principal, Object credentials, Object details){
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        this.details = details;
        setAuthenticated(false);
    }

    /**
     * This constructor should only be used by <code>AuthenticationManager</code> or <code>AuthenticationProvider</code>
     * implementations that are satisfied with producing a trusted (i.e. {@link #isAuthenticated()} = <code>true</code>)
     * authentication token.
     *
     * @param principal
     * @param credentials
     * @param authorities
     */
    public MarketVineAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities){
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    /**
     * Stores additional details about the authentication request. These might be an IP address, certificate
     * serial number etc.
     *
     * @return additional details about the authentication request, or <code>null</code> if not used
     */
    @Override
    public Object getDetails() {
        return details;
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }

        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        credentials = null;
    }


}
