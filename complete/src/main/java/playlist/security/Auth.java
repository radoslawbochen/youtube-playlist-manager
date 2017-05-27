package playlist.security;


import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.common.collect.Lists;

import playlist.entity.User;
import playlist.services.UserService;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


public class Auth {

  HttpSession session;
	
  static TokenResponse response; 
	
  static CountDownLatch userAuthenticationLatch;
	
  public static Credential credential;
	
  /** URL user is given to authorize himself. */
  public static String url;
  
  /** Authorization code flow. */
  private static AuthorizationCodeFlow flow;
  
  public static JsonFactory JSON_FACTORY;
  
  public static HttpTransport HTTP_TRANSPORT;
  
  public final static AuthorizationCodeFlow getFlow() {
    return flow;
  }

  public static void receiveCode(
		  UserService userService,
		  String code
		  ) throws IOException {
	  	 	
		  List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.readonly");
		  HTTP_TRANSPORT = new NetHttpTransport();
		  JSON_FACTORY = new JacksonFactory();
		  String CREDENTIALS_DIRECTORY = ".oauth-credentials";
		  Reader clientSecretReader = new InputStreamReader(Auth.class.getResourceAsStream("/client_secrets.json"));
		  GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,clientSecretReader);
		  FileDataStoreFactory fileDataStoreFactory = new FileDataStoreFactory(new File(System.getProperty("user.home") + "/" + CREDENTIALS_DIRECTORY));
		  DataStore<StoredCredential> datastore = fileDataStoreFactory.getDataStore("myRating");
		  GoogleAuthorizationCodeFlow oauthFlow = new GoogleAuthorizationCodeFlow.Builder(
				  HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, scopes).setCredentialDataStore(datastore)
				 .build();
		  flow = Preconditions.checkNotNull(oauthFlow);
		  response = getFlow().newTokenRequest(code).setRedirectUri("http://localhost:8080/oauth2callback").execute();	
		  String userId = getUserId(userService);
		  credential = flow.createAndStoreCredential(response, userId);
    }
  
  public static String getUserId(UserService userService){
	  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	  User user = userService.findUserByEmail(auth.getName());
	  
	  return String.valueOf(user.getId()); 
  }
  
}

