package playlist.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.common.collect.Lists;

import playlist.entity.PlaylistInfoModel;
import playlist.entity.PlaylistModel;
import playlist.entity.PlaylistService;
import playlist.entity.User;
import playlist.entity.UsermadeAppPlaylistModel;
import playlist.entity.UsermadePlaylistInfo;
import playlist.entity.YoutubePlaylistInfo;
import playlist.security.Auth;
import playlist.security.AuthorizationCodeInstalledApp;
import playlist.services.UsermadePlaylistService;
import playlist.services.UsermadePlaylistServiceImpl;
import playlist.services.YoutubePlaylistService;

@Controller
public class PlaylistController {
		
	public static Credential credential;
	
	@Autowired
	private UsermadePlaylistService usermadePlaylistService;
	@Autowired YoutubePlaylistService youtubePlaylistService;
	
	@RequestMapping(value = "/getPhp")
	public String getPhp(){
		return "example";
	}
	
	@RequestMapping(value = "/")
	public String index(Model model){	
		if(credential != null){
	    		if(credential.getClientAuthentication() != null){
	    	    	model.addAttribute("playlist", new PlaylistModel());
	    	    	model.addAttribute("playlistInfo", new PlaylistInfoModel());
	    		return "index";}
	    	}
	    		return "redirect:/login";
	}
	
    @RequestMapping(value = "/login")
    public String authorizeUser(){
    	List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.readonly");

        try {
            credential = Auth.authorize(scopes, "myuploads");
        }catch (GoogleJsonResponseException e) {
                e.printStackTrace();
                System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                        + e.getDetails().getMessage());

        }catch (Throwable t) {
                t.printStackTrace();
            }
        
    	String url = playlist.security.AuthorizationCodeInstalledApp.url;
    	System.out.println(url + "/login ");
		return "redirect:" + url;
		//return "redirect:https://accounts.google.com/o/oauth2/auth?client_id=252777349769-slb56loumaivmrri04oqfua5mfu16oi8.apps.googleusercontent.com&redirect_uri=http://localhost:8080&scope=https://gdata.youtube.com&response_type=code&access_type=offline";
    }
	
    @RequestMapping(value = "/viewPlaylist")
    public String playlistView(@RequestParam(value = "playlist", required = false) String playlistName,
			PlaylistModel playlistTest,
			PlaylistInfoModel playlistInfo,
			Model model,
			@Autowired
			UserContents userContents
			){
    	
    	if(credential != null){
    		if(credential.getClientAuthentication() != null){
        
    			if (playlistName != null){
    				MyUploads myUploads = new MyUploads(userContents);
    				String channelId = userContents.getChannelId();
			
			//usermadePlaylistService.saveUser(new User(10L, "UCODHgrgA5lVE5htZ1ffpOg", "playlistNameU1", "testLink3", 24));
    				List<User> userPlaylistList = usermadePlaylistService.findByChannelIdAndPlaylistName(channelId, playlistName);
    				model.addAttribute("userPlaylistList", userPlaylistList);
    				return "playlistViewA";
    			}else {
    				List<YoutubePlaylistInfo> youtubePlaylistInfoList = youtubePlaylistService.findYoutubePlaylistsInfo();
    				List<UsermadePlaylistInfo> usermadePlaylistInfoList = usermadePlaylistService.findDistinctPlaylistNameByChannelId();
					model.addAttribute("youtubePlaylistInfoList", youtubePlaylistInfoList);
					model.addAttribute("usermadePlaylistInfoList", usermadePlaylistInfoList);
					return "playlistView";
			}
    		}}
    	
		return "redirect:/redirection";
    }
    
    /*
	@RequestMapping(value = "/viewPlaylist", method = RequestMethod.GET)
	public String playlistView(
			@RequestParam(value = "playlist", required = false) String playlistName,
			PlaylistModel playlistTest,
			PlaylistInfoModel playlistInfo,
			Model model,
			@Autowired
			UserContents userContents
			){
		if(credential != null){
    		if(credential.getClientAuthentication() != null){
    			if (playlistName != null){
    				MyUploads myUploads = new MyUploads(userContents);
    				String channelId = userContents.getChannelId();
    				
    				//usermadePlaylistService.saveUser(new User(10L, "UCODHgrgA5lVE5htZ1ffpOg", "playlistNameU1", "testLink3", 24));
    				List<User> userPlaylistList = usermadePlaylistService.findByChannelIdAndPlaylistName(channelId, playlistName);
    				model.addAttribute("userPlaylistList", userPlaylistList);
    				return "playlistViewA";
    			}else {
    					List<YoutubePlaylistInfo> youtubePlaylistInfoList = youtubePlaylistService.findYoutubePlaylistsInfo();
    					List<UsermadePlaylistInfo> usermadePlaylistInfoList = usermadePlaylistService.findDistinctPlaylistNameByChannelId();
    				
    					System.out.println(usermadePlaylistInfoList.size());
    					model.addAttribute("youtubePlaylistInfoList", youtubePlaylistInfoList);
    					model.addAttribute("usermadePlaylistInfoList", usermadePlaylistInfoList);
    		    	  	return "playlistView";
    				}
    			}
    		}
    	return "redirect:/login";
	}
	*/
    
	@RequestMapping(value = "/viewPlaylist", method = RequestMethod.POST)
	public String addPlaylist(
			@RequestParam(required = false, value = "name") String playlistName,
			@RequestParam(required = false, value = "delete") String delete
	){

		String channelId = MyUploads.getChannelId();
		if (playlistName != null){
			usermadePlaylistService.saveUser(new User(10L, channelId, playlistName, "", 0));
		    String getPlaylist = "?playlist=" + playlistName;
			return "redirect:/viewPlaylist" + getPlaylist;
		}else if(delete != null){
			usermadePlaylistService.deleteByChannelIdAndPlaylistName(channelId, delete);
			return "redirect:/viewPlaylist";
			
		}
		return "redirect:/viewPlaylist";
	}
	
	@RequestMapping(value = "/viewPlaylist", method = RequestMethod.DELETE)
	public String deletePlaylist(
			@RequestParam("name") String playlistName
	){
		String channelId = MyUploads.getChannelId();
		usermadePlaylistService.deleteByChannelIdAndPlaylistName(channelId, playlistName);
		
		return "redirect:/viewPlaylist";
	}
		
	@RequestMapping(value = "/redirection")
	String redirect(){
		return "redirect:https://accounts.google.com/o/oauth2/auth?client_id=252777349769-slb56loumaivmrri04oqfua5mfu16oi8.apps.googleusercontent.com&redirect_uri=http://localhost:8080/oauth2callback&scope=https://gdata.youtube.com&response_type=code&access_type=offline";
	}
	
    @RequestMapping(value = "/oauth2callback", method = RequestMethod.GET)
    public String recieveCode(@RequestParam("code") String code) throws IOException{
    	AuthorizationCodeInstalledApp.receiveCode(code);
    	return "redirect:/";
    }
    
	public static RedirectView redirectToLogin(String url){
		String redirectUri = "redirect:" + url;
		System.out.println("testing");
		System.out.println(redirectUri);
		return new RedirectView(url);
	}
	
	public static RedirectView login(){
		return new RedirectView("/login");
	}
}
    