package local.tcltk.controller;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class ActionFactory {

    private static Map<String, Action> actions = new HashMap<>();

    static {
        actions.put("verify", new VerifyAction());
        actions.put("profile", new ProfileAction());
        actions.put("view", new ViewAction());
        actions.put("auth", new AuthAction());
        actions.put("e-verify", new VerifyActionEmbeddedApp());
        actions.put("error", new ErrorActionEmbeddedApp());
        actions.put("help", new HelpActionEmbeddedApp());
    }


    public static String getActionPart(HttpServletRequest request) {
        String result = request.getRequestURI();

//        return actions.get(request.getMethod() + request.getPathInfo());
//        System.out.println("request.getPathInfo(): " + request.getPathInfo());
//        System.out.println("request.getRequestURI(): " + request.getRequestURI());
//        System.out.println(request.getRequestURI().substring(11));
//        return actions.get(request.getRequestURI().substring(PROJECT_NAME.length() + 2));

        // remove last "/"
        if (result.endsWith("/")) {
            result = result.substring(0, result.length() - 1);
        }

        // get string after last "/"
        result = result.substring(result.lastIndexOf("/") + 1);

        return result;
    }

    public static Action getAction(HttpServletRequest request) {
        return actions.get(getActionPart(request));
    }
}
