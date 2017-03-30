package playlist.security;


import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;
import com.google.api.client.util.Preconditions;

import playlist.controller.PlaylistController;
import playlist.entity.PlaylistService;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.web.servlet.view.RedirectView;

/**
 * OAuth 2.0 authorization code flow for an installed Java application that persists end-user
 * credentials.
 *
 * <p>
 * Implementation is thread-safe.
 * </p>
 *
 * @since 1.11
 * @author Yaniv Inbar
 */
public class AuthorizationCodeInstalledApp {

  static TokenResponse response; 
	
  static CountDownLatch userAuthenticationLatch;
	
  public static Credential credential;
	
  /** URL user is given to authorize himself. */
  public static String url;
  
  /** Authorization code flow. */
  private static AuthorizationCodeFlow flow;

  /** Verification code receiver. */
  private static VerificationCodeReceiver receiver;

  private static final Logger LOGGER =
      Logger.getLogger(AuthorizationCodeInstalledApp.class.getName());

  public static String userId;

  /**
   * @param flow authorization code flow
   * @param receiver verification code receiver
   */
  public AuthorizationCodeInstalledApp(
      AuthorizationCodeFlow flow, VerificationCodeReceiver receiver) {
    this.flow = Preconditions.checkNotNull(flow);
    this.receiver = Preconditions.checkNotNull(receiver);
  }

  /**
   * Authorizes the installed application to access user's protected data.
   *
   * @param userId user ID or {@code null} if not using a persisted credential store
   * @return credential
   */
  
  public Credential authorize(String userId) throws IOException {
    try {
      credential = flow.loadCredential(userId);
      if (credential != null
          && (credential.getRefreshToken() != null || credential.getExpiresInSeconds() > 60)) {
        return credential;
      }
      // open in browser
      String redirectUri = receiver.getRedirectUri();
      AuthorizationCodeRequestUrl authorizationUrl =
          flow.newAuthorizationUrl().setRedirectUri(redirectUri);
      onAuthorization(authorizationUrl);
      //PlaylistService.test(url);
      String code = receiver.waitForCode();
      TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectUri).execute();
      // store credential and return it
      return flow.createAndStoreCredential(response, userId);
    } finally {
      receiver.stop();
    }
  }

  /**
   * Handles user authorization by redirecting to the OAuth 2.0 authorization server.
   *
   * <p>
   * Default implementation is to call {@code browse(authorizationUrl.build())}. Subclasses may
   * override to provide optional parameters such as the recommended state parameter. Sample
   * implementation:
   * </p>
   *
   * <pre>
  &#64;Override
  protected void onAuthorization(AuthorizationCodeRequestUrl authorizationUrl) throws IOException {
    authorizationUrl.setState("xyz");
    super.onAuthorization(authorizationUrl);
  }
   * </pre>
   *
   * @param authorizationUrl authorization URL
 * @return 
   * @throws IOException I/O exception
   */
  protected String onAuthorization(AuthorizationCodeRequestUrl authorizationUrl) throws IOException {
	return url = authorizationUrl.build();
  }

  /**
   * Open a browser at the given URL using {@link Desktop} if available, or alternatively output the
   * URL to {@link System#out} for command-line applications.
   *
   * @param url URL to browse
   */
  public static void browse() {
    Preconditions.checkNotNull(url);
    // Ask user to open their browser using copy-paste
    System.out.println("Please open the following address in your browser:");
    System.out.println("  " + url);
  }
   // PlaylistController.login(url);
    //new RedirectView(url);
    // Attempt to open it in the browser
    /*
    try {
      if (Desktop.isDesktopSupported()) {
        Desktop desktop = Desktop.getDesktop();
        if (desktop.isSupported(Action.BROWSE)) {
          System.out.println("Attempting to open that address in the default browser now...");
          desktop.browse(URI.create(authUrl));
        }
      }
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, "Unable to open browser", e);
    } catch (InternalError e) {
      // A bug in a JRE can cause Desktop.isDesktopSupported() to throw an
      // InternalError rather than returning false. The error reads,
      // "Can't connect to X11 window server using ':0.0' as the value of the
      // DISPLAY variable." The exact error message may vary slightly.
      LOGGER.log(Level.WARNING, "Unable to open browser", e);
    }
  }

  /** Returns the authorization code flow. */
  public final AuthorizationCodeFlow getFlow() {
    return flow;
  }

  /** Returns the verification code receiver. */
  public final VerificationCodeReceiver getReceiver() {
    return receiver;
  }

  public Credential authorizeTest(String userId) throws IOException{
	  try{
		  this.userId = userId;
		  credential = flow.loadCredential(userId);
		  if (credential != null
			  && (credential.getRefreshToken() != null || credential.getExpiresInSeconds() > 60)) {
			  return credential;
		  } else {
			  prepareUrl();
			  PlaylistController.redirectToLogin(url);
			  //userAuthenticationLatch = new CountDownLatch(1);
			  /*try {
        		boolean userAuthentication = userAuthenticationLatch.await(10,TimeUnit.SECONDS);
        	 } catch (InterruptedException e) {
        		 e.printStackTrace();
        	 }
        	 */
        	 return flow.createAndStoreCredential(response, userId);
		}
	 } finally {
          receiver.stop();
     }
  }
  
  public void prepareUrl() throws IOException{
	  String redirectUri = "https://accounts.google.com/o/oauth2/auth?client_id=252777349769-slb56loumaivmrri04oqfua5mfu16oi8.apps.googleusercontent.com&redirect_uri=http://localhost:8080/oauth2callback&scope=https://gdata.youtube.com&response_type=code&access_type=offline";
	  //redirectUri = receiver.getRedirectUri();
	  
      AuthorizationCodeRequestUrl authorizationUrl =
      flow.newAuthorizationUrl().setRedirectUri(redirectUri);
	  //return onAuthorization(authorizationUrl);
      url = redirectUri;
  }
  
  public void loginRedirect(){
	  try {
		prepareUrl();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  PlaylistController.redirectToLogin(url);
  }

  public static void receiveCode(String code) throws IOException {
	  //userAuthenticationLatch.countDown();
      response = flow.newTokenRequest(code).setRedirectUri("http://localhost:8080/oauth2callback").execute();
      
  }
  
}
