package com.fdb.mobile.servlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.DefaultWebRequestor;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.WebRequestor;
import com.restfb.types.User;






/**
 * Servlet implementation class EmiratesWebAction
 */
public class FaceBookLogin extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FaceBookLogin() {
	super();
	// TODO Auto-generated constructor stub
    }


    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	System.out.println("Get Called FacebookLogin");
    	doPost(request,response);
    }
    
    

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession();
        System.out.println("Post Called FacebookLogin");
        String faceCode = request.getParameter("code");
        String state = request.getParameter("state");
        String accessToken = getFacebookAccessToken(faceCode);
        User userDetails = getUser(accessToken, httpSession);
        String sessionID = httpSession.getId();
        
        System.out.println("Session ID"+sessionID+" state   "+state);
        httpSession.setAttribute("fname", userDetails.getFirstName());
        httpSession.setAttribute("lname", userDetails.getLastName());
        httpSession.setAttribute("email", userDetails.getEmail());
        httpSession.setAttribute("fullName", userDetails.getName());
        if(userDetails.getPicture()!=null) {
        	 httpSession.setAttribute("userImage", userDetails.getPicture().getUrl());
        }
       
        System.out.println(userDetails.getPicture());
       // System.out.println(userDetails.getPicture().getUrl());
        if("female".equalsIgnoreCase(userDetails.getGender())) {
        	httpSession.setAttribute("gender", "Mrs");
        } else {
        	httpSession.setAttribute("gender", "Mr");
        }
      //  httpSession.setAttribute("phone", userDetails.getM);
        if (state.equals(sessionID)){
            try {
               
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() +"/facebookConnectError.jsf");
                return;
            }
            response.sendRedirect(request.getContextPath() +"/index.jsp#contact");
        } else {
            System.err.println("CSRF protection validation");
        }}

   
    private String getFacebookAccessToken(String faceCode){
        String token = null;
        if (faceCode != null && ! "".equals(faceCode)) {
        	String appId = "1608692689396413";
            String redirectUrl = "http://localhost:8080/decodedubai/change.sec";
            String faceAppSecret = "ae8f919f25cceeebd5c397ec0cb924a7";
        	WebRequestor wr = new DefaultWebRequestor();
            try {
				WebRequestor.Response accessTokenResponse = wr.executeGet(
				        "https://graph.facebook.com/oauth/access_token?client_id=" + appId + "&redirect_uri=" + redirectUrl
				        + "&client_secret=" + faceAppSecret + "&code=" + faceCode);
				
				FacebookClient.AccessToken token2 =	DefaultFacebookClient.AccessToken.fromQueryString(accessTokenResponse.getBody());
				token = token2.getAccessToken();
            
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//	System.out.println("User Details "+user);
        }
        return token;
    }
 
    private User getUser(String accessToken,
             HttpSession httpSession) {
        String email = null;
        HttpClient httpclient = new DefaultHttpClient();
        User user = null;
            if (accessToken != null && ! "".equals(accessToken)) {
             
            	
            	FacebookClient facebookClient = new DefaultFacebookClient(accessToken);
    			 user = facebookClient.fetchObject("me", User.class,Parameter.with("fields", "about,birthday,email,age_range,education,first_name,last_name,gender,location,name,picture"));
    			int count=0;
    			 Connection<User> myFriends = facebookClient.fetchConnection("me/friends", User.class,Parameter.with("fields", "about,birthday,email,age_range,education,first_name,last_name,gender,location,name,picture"));
    			Iterator<List<User>> friends= myFriends.iterator();
    			while (friends.hasNext()) {
    				List<User> userList = friends.next();
    				for(User user1 : userList) {
    					System.out.println("Friend "+count++ +user1);
    				}
    			}
    			 System.out.println(user);
    			 System.out.println(myFriends);
            } else {
                System.err.println("Token for facebook is null");
            }
        
        return user;
    }
}
