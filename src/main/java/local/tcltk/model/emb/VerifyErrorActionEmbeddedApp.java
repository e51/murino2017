package local.tcltk.model.emb;

import local.tcltk.model.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VerifyErrorActionEmbeddedApp implements Action {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "auth-error";
    }
}
