package com.fdb.mobile.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;






/**
 * Servlet implementation class EmiratesWebAction
 */
public class EmiratesWebAction extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmiratesWebAction() {
	super();
	// TODO Auto-generated constructor stub
    }


    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	System.out.println("Get cALLED");
    	doPost(request,response);
    }
    
    

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	String redirect =getFacebookUrlAuth(request);
    	response.sendRedirect(redirect);
    }

    public String getFacebookUrlAuth(HttpServletRequest request) {
        HttpSession session =
        		request.getSession();
        String sessionId = session.getId();
        String appId = "1608692689396413";
        String redirectUrl = "http://localhost:8080/decodedubai/change.sec";
        String returnValue = "https://www.facebook.com/dialog/oauth?client_id="
                + appId + "&redirect_uri=" + redirectUrl
                + "&scope=public_profile,email&state=" + sessionId;
        return returnValue;
    }
   public static void main(String[] args) {
	   String token = null;
       
      // String appId = "1608692689396413";
       String appId = "1608763436056005"; //test id
       String redirectUrl = "http://localhost:8080/index.sec";
       String faceAppSecret = "ae8f919f25cceeebd5c397ec0cb924a7";
       String newUrl = "https://graph.facebook.com/oauth/access_token?client_id="
               + appId + "&redirect_uri=" + redirectUrl + "&client_secret="
               + faceAppSecret + "&code=vack.kak@gmail.com" ;
       HttpClient httpclient = new DefaultHttpClient();
       try {
           HttpGet httpget = new HttpGet(newUrl);
           ResponseHandler<String> responseHandler = new BasicResponseHandler();
           String responseBody = httpclient.execute(httpget, responseHandler);
           System.out.println("data is"+responseBody);
           token = StringUtils.removeEnd
                   (StringUtils.removeStart(responseBody, "access_token="),
                                            "&expires=5180795");
       } catch (ClientProtocolException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       } finally {
           httpclient.getConnectionManager().shutdown();
       }
   
   System.out.println("token is"+token);	
   }
}