package local.tcltk.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthAction implements Action {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("auth servlet, uri: " + request.getQueryString() + " / session: " + request.getSession(false));
        return "auth";
    }
}
