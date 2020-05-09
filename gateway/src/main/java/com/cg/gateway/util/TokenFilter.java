package com.cg.gateway.util;

import com.cg.gateway.service.CredentialService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class TokenFilter extends ZuulFilter {
    private static final Logger Log = LoggerFactory.getLogger(TokenFilter.class);

    @Autowired
    private CredentialService credentialService;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        try {
            HttpServletRequest request = context.getRequest();
            String token = request.getHeader("token");
            String uri = request.getRequestURI();
            Log.info("request uri=" + uri);
            if (token != null && !token.isEmpty()) {
                DecodedToken decodedToken = TokenUtil.decode(token);
                if (decodedToken != null) {
                    String id = decodedToken.getId();
                    String password = decodedToken.getPassword();
                    boolean correct = credentialService.checkCredentials(id, password);
                    boolean isAdmin = credentialService.isAdmin(id);
                    if (correct) {
                        //
                        //request sender added as header as information which can be used later in routed service
                        //
                        context.addZuulRequestHeader("requestsender", id);
                        //
                        //if is admin then can access anything, no further check needed
                        //
                        if (isAdmin) {
                            return null;
                        }
                        if (!isAdmin) {
                            //
                            //if not admin, then user should only be able to visit urls not starting with /admin urls
                            //
                            if (!uri.startsWith("/admin")) {
                                return null;
                            }
                        }
                    }
                }
            }
            //
            // token does not exist in request or is incorrect so setting zuul response as false
            //
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.SC_UNAUTHORIZED);
            context.setResponseBody("you are not allowed");
            return null;
        }catch (Throwable e){
            Log.error("exception in filter "+e.getMessage());
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.SC_UNAUTHORIZED);
            context.setResponseBody("you are not allowed");
            return null;
        }
    }


}
