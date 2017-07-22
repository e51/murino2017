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
        actions.put("m-verify", new VerifyActionMobileApp());
    }


    public static String getActionPart(HttpServletRequest request) {
        String result = request.getRequestURI();

//        return actions.get(request.getMethod() + request.getPathInfo());
//        System.out.println("request.getPathInfo(): " + request.getPathInfo());
//        System.out.println("request.getRequestURI(): " + request.getRequestURI());
//        System.out.println(request.getRequestURI().substring(11));
//        return actions.get(request.getRequestURI().substring(PROJECT_NAME.length() + 2));

        if (result.lastIndexOf(";") != -1) {
            //remove ";jsessionid=63565260D45BEA8264B98295D8201DBC"
            result = result.substring(0, result.lastIndexOf(";"));
        }

        // remove last "/"
        if (result.endsWith("/")) {
            result = result.substring(0, result.length() - 1);
        }

//        System.out.println(result);

        // get string after last "/"
        result = result.substring(result.lastIndexOf("/") + 1);

//        System.out.println(result);

        return result;
    }

    public static Action getAction(HttpServletRequest request) {
        return actions.get(getActionPart(request));
    }
}
