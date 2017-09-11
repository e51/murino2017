package local.tcltk.model;

import local.tcltk.model.emb.VerifyErrorActionEmbeddedApp;
import local.tcltk.model.emb.ErrorActionEmbeddedApp;
import local.tcltk.model.emb.VerifyActionEmbeddedApp;
import local.tcltk.model.emb.HelpActionEmbeddedApp;
import local.tcltk.model.emb.mobile.VerifyActionMobileApp;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class ActionFactory {
    private static final Logger logger = Logger.getLogger(ActionFactory.class);

    private static Map<String, Action> actions = new HashMap<>();

    static {
        actions.put("auth", new AuthAction());                  // Web app
        actions.put("verify", new VerifyAction());              // Web app
        actions.put("view", new ViewAction());                  // Web, Embedded and Mobile app
        actions.put("profile", new ProfileAction());            // Web, Embedded and Mobile app

        actions.put("e-verify", new VerifyActionEmbeddedApp()); // Embedded app
        actions.put("error", new ErrorActionEmbeddedApp());     // Embedded and Mobile app
        actions.put("auth-error", new VerifyErrorActionEmbeddedApp());     // Embedded and Mobile app
        actions.put("help", new HelpActionEmbeddedApp());       // Embedded and Mobile app

        actions.put("m-verify", new VerifyActionMobileApp());   // Mobile app

    }


    public static String getActionPart(HttpServletRequest request) {
        String result = request.getRequestURI();

//        return actions.get(request.getMethod() + request.getPathInfo());
//        System.out.println("request.getPathInfo(): " + request.getPathInfo());
//        System.out.println("request.getRequestURI(): " + request.getRequestURI());
//        System.out.println(request.getRequestURI().substring(11));
//        return actions.get(request.getRequestURI().substring(PROJECT_NAME.length() + 2));

        // remove jsessionid from URL if present
        // remove ";jsessionid=63565260D45BEA8264B98295D8201DBC"
        if (result.lastIndexOf(";") != -1) {
            result = result.substring(0, result.lastIndexOf(";"));
        }

        // remove last "/null"
        if (result.endsWith("/null")) {
            logger.error(String.format("[getActionPart] requestURI: %s contains /null! Cut it. User Agent: %s", request.getRequestURI(), request.getHeader("User-Agent")));
            result = result.substring(0, result.length() - 5);
        }

        // remove last "/"
        if (result.endsWith("/")) {
            result = result.substring(0, result.length() - 1);
        }

        // get string after last "/"
        result = result.substring(result.lastIndexOf("/") + 1);

//        System.out.println(result);
//        //get string after base url
//        result = result.substring(SITE_ROOT.length());
//
//        System.out.println(result);

        return result;
    }

    public static Action getAction(HttpServletRequest request) {
        return actions.get(getActionPart(request));
    }
}
