package playlist.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.api.client.auth.oauth2.Credential;

import playlist.entity.AddListWrapper;
import playlist.entity.User;
import playlist.entity.usermadePlaylist.UsermadePlaylist;
import playlist.entity.usermadePlaylist.UsermadePlaylistWrapper;
import playlist.entity.youtubePlaylist.YoutubePlaylist;
import playlist.repositories.YoutubeUserRepository;
import playlist.security.Auth;
import playlist.services.UserService;
import playlist.services.UsermadePlaylistService;
import playlist.services.YoutubePlaylistService;

@Controller
@RequestMapping("/viewPlaylist")
public class ViewPlaylistController {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAutoGrowCollectionLimit(2048);
    }
    
    @Autowired
    private UserService userService;
	@Autowired
	private UsermadePlaylistService usermadePlaylistService;
	@Autowired
	private YoutubePlaylistService youtubePlaylistService;
	
	@RequestMapping(method = RequestMethod.GET)
    public String playlistView(
    		
    		@RequestParam(value = "playlist", required = false) String playlistName,
			Model model
			) throws IOException{		
				if(Auth.getFlow() == null){
					return "redirect:/login";
				}
				Credential credential = Auth.getFlow().loadCredential(Auth.getUserId(userService)); 
				
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				User user = userService.findUserByEmail(auth.getName());
				String channelId = YoutubeUserRepository.getChannelId(credential, Integer.toString(user.getId()));
				
    			if (playlistName != null){
    				List<UsermadePlaylist> usermadePlaylistList = usermadePlaylistService.findByPlaylistName(credential, playlistName);
    				List<YoutubePlaylist> youtubePlaylistList = youtubePlaylistService.findYoutubePlaylists(credential, channelId, usermadePlaylistList);
    				
    				model.addAttribute("playlistName", playlistName);
    				model.addAttribute("usermadePlaylist", new UsermadePlaylistWrapper());
    				model.addAttribute("addList" , new AddListWrapper());
    				model.addAttribute("youtubePlaylistList", youtubePlaylistList);
    				model.addAttribute("usermadePlaylistList", usermadePlaylistList);
    				
    				return "playlistViewA";
    			} else {
    				model.addAttribute("youtubePlaylistInfoList", youtubePlaylistService.findYoutubePlaylistsInfo(credential));
					model.addAttribute("usermadePlaylistInfoList", usermadePlaylistService.findDistinctPlaylistName(credential));
					
					return "playlistView";
    			}
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String modifyUsermadePlaylist(
			@RequestParam(required = false, value = "add") String addPlaylistName,
			@RequestParam(required = false, value = "delete") String deletePlaylistName
	) throws IOException{		
		String userId = Auth.getUserId(userService);
		Credential credential = Auth.getFlow().loadCredential(Auth.getUserId(userService)); 

		String channelId = YoutubeUserRepository.getChannelId(credential, userId);		
		if (addPlaylistName != null){
			usermadePlaylistService.saveUsermadePlaylist(new UsermadePlaylist(10L, channelId, addPlaylistName));
		    String getPlaylist = "?playlist=" + addPlaylistName;
		    
			return "redirect:/viewPlaylist" + getPlaylist;
		} else if (deletePlaylistName != null) {
			usermadePlaylistService.deleteByPlaylistNameAndChannelId(credential, deletePlaylistName);
			
			return "redirect:/viewPlaylist";
		}
		
		return "redirect:/viewPlaylist";
	}
    
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(
    		@ModelAttribute(value = "users") UsermadePlaylistWrapper usermadePlaylistWrapper,
    		@RequestParam(value = "playlistName") String playlistName
    		){
    	usermadePlaylistService.delete(usermadePlaylistWrapper.getUsermadePlaylists(), playlistName);
        	
    	return "redirect:/viewPlaylist?playlist=" + playlistName;
    }
    
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(
    		@ModelAttribute(value = "addList") AddListWrapper addListWrapper,
    		@RequestParam(value = "playlistName", required = false) String playlistName
    		) throws IOException{		  
			Credential credential = Auth.getFlow().loadCredential(Auth.getUserId(userService)); 
			usermadePlaylistService.add(credential, addListWrapper.getAddList(), playlistName);
		
    	return "redirect:/viewPlaylist?playlist=" + playlistName;
    }    
}
