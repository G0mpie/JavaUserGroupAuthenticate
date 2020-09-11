
package com.microsoft.azuresamples.webapp.authentication;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import com.microsoft.azuresamples.webapp.Config;

@WebServlet(name = "TokenDetailsServlet", urlPatterns = "/auth_token_details")
public class TokenDetailsServlet extends HttpServlet {

    private final String[] exClaims = {"iat", "exp", "nbf", "uti", "aio"};
    private HashMap<String,String> fakeClaims = new HashMap<>();
    private final List<String> excludeClaims = Arrays.asList(exClaims);

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        final MsalAuthSession thething = Config.configureMsalSessionAttributes(req);
        
        fakeClaims.put("iat", "the iat claim");
        fakeClaims.put("issuer", "the issuer claim");
        HashMap<String,String> claimsToDisplay = new HashMap<>();
        fakeClaims.forEach((k,v) -> {
            if (!excludeClaims.contains(k))
                claimsToDisplay.put(k, v);
        });
        claimsToDisplay.forEach((k,v) -> {
            System.out.println("key " + k + " value " + v);
        });
        req.setAttribute("claims", claimsToDisplay);
        req.setAttribute("bodyContent", "auth/token.jsp");
        final RequestDispatcher view = req.getRequestDispatcher("index.jsp");
        view.forward(req, resp);
    }
}

