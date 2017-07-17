package local.tcltk.controller;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static local.tcltk.Constants.*;

public class ActionFactory {

    private static Map<String, Action> actions = new HashMap<>();

    static {
        actions.put("verify", new VerifyAction());
        actions.put("profile", new ProfileAction());
        actions.put("view", new ViewAction());
        actions.put("auth", new AuthAction());
    }


    public static Action getAction(HttpServletRequest request) {
//        return actions.get(request.getMethod() + request.getPathInfo());
//        System.out.println("request.getPathInfo(): " + request.getPathInfo());
//        System.out.println(request.getRequestURI().substring(11));
//        return actions.get(request.getRequestURI().substring(PROJECT_NAME.length() + 2));
        return actions.get(request.getRequestURI().substring(SITE_ROOT.length()).replace('/', ' ').trim());
    }
}
