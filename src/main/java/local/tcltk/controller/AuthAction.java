package local.tcltk.controller;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static local.tcltk.Constants.SITE_ROOT;

public class AuthAction implements Action {
    private static final Logger logger = Logger.getLogger(AuthAction.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

//        logger.info("Request URI: " + request.getRequestURI());
//        logger.info("Query string: " + request.getQueryString());
//        logger.info("SID: " + request.getSession().getId());
//        logger.info("Plane URL: " );
//        logger.info("encodeURL: " + response.encodeURL(SITE_ROOT + "z/page4"));
//        logger.info("encodeRedirectURL: " + response.encodeRedirectURL(SITE_ROOT + "z/page4"));

        return "auth";
    }
}
