package test;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.mail.*;
import javax.mail.internet.*;

import test.PMF;
import test.LinkData;

@SuppressWarnings("serial")
public class MailSendServlet extends HttpServlet {

 public void doGet(HttpServletRequest req, HttpServletResponse resp)
 throws ServletException, IOException {
	 PersistenceManagerFactory factory = PMF.get();
     PersistenceManager manager = factory.getPersistenceManager();
     resp.setCharacterEncoding("UTF-8");
     resp.setContentType("text/html");
     req.setCharacterEncoding("utf-8");
     String param1 = req.getParameter("id");
     PrintWriter out = resp.getWriter();
     List<LinkData> list = null;
     if (param1 == null || param1 ==""){
         String query = "select from " + LinkData.class.getName();
         try {
             list = (List<LinkData>)manager.newQuery(query).execute();
         } catch(JDOObjectNotFoundException e){}
     } else {
         try {
             LinkData data = (LinkData)manager.getObjectById(LinkData.class,Long.parseLong(param1));
             list = new ArrayList();
             list.add(data);
         } catch(JDOObjectNotFoundException e){}
     }
     String res = "";
     if (list != null){
         for(LinkData data:list){
             res += data.getTitle()+data.getUrl() + "様。クワトロ " +
                 data.getCount1() + "枚。チーズメルト" + data.getCount2() +
                 "枚。カマンベールミルフィーユ" + data.getCount3() + "枚。リストランテ"
                 +data.getCount4()+"枚。合計金額は"+data.getCount5()+"円です。";
         }
     }
     out.println("送信が完了しました。");
     resp.getWriter().println("<p><a href=/index.html>"+"注文フォームへ"+"</a></p>");
     manager.close();
    Properties props = new Properties();
    Session session = Session.getDefaultInstance(props, null);
   try {
    Message msg = new MimeMessage(session);
    msg.setFrom(new InternetAddress("pizapizapizashopkadaipiza@gmail.com"));
    msg.addRecipient(Message.RecipientType.TO, new InternetAddress(req.getUserPrincipal().getName()));
    ((MimeMessage)msg).setSubject("確認メールです。","UTF-8");
    msg.setText(res);
    Transport.send(msg);
   } catch (MessagingException e) {
    e.printStackTrace();
   }
 }
}

