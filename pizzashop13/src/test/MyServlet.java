package test;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class MyServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        UserService userService = UserServiceFactory.getUserService();

        String thisURL = req.getRequestURI();

        resp.setContentType("text/html; charset=UTF8");
        if (req.getUserPrincipal() != null) {
            resp.getWriter().println("<p>こんにちは " +
                                     req.getUserPrincipal().getName() +
                                     "さん!<a href=\"" +
                                     userService.createLogoutURL(thisURL) +
                                     "\">サインアウト</a>も出来ます。</p>");
            resp.getWriter().println("<p><a href=/add2.html>"+"注文フォームへ"+"</a></p>");
            resp.getWriter().println("<br><br><br><br><br><p><a href=/shop.html>"+"従業員用フォームへ"+"</a></p>");
        	
        } else {
           resp.getWriter().println("<p><a href=\"" +
                                     userService.createLoginURL(thisURL) +
                                     "\">サインイン</a>してください。</p>");
        	
        }
    }
}