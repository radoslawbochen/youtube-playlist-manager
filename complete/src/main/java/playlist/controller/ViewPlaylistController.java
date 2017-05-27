package playlist.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import playlist.entity.AddListWrapper;
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
	private UsermadePlaylistService usermadePlaylistService;
	@Autowired
	private YoutubePlaylistService youtubePlaylistService;
	@Autowired
	private UserService userService;
	
	@RequestMapping(method = RequestMethod.GET)
    public String playlistView(
    		@RequestParam(value = "playlist", required = false) String playlistName,
			Model model
			) throws IOException{		
				String userId = Auth.getUserId(userService);
				if(Auth.getFlow() == null){
					return "redirect:/login";
				}
				String channelId = YoutubeUserRepository.getChannelId(userId);				
				
    			if (playlistName != null){
    				List<UsermadePlaylist> usermadePlaylistList = usermadePlaylistService.findByChannelIdAndPlaylistName(channelId, playlistName);
    				List<YoutubePlaylist> youtubePlaylistList = youtubePlaylistService.findYoutubePlaylistsByChanellId(channelId, usermadePlaylistList);
    				
    				model.addAttribute("playlistName", playlistName);
    				model.addAttribute("usermadePlaylist", new UsermadePlaylistWrapper());
    				model.addAttribute("addList" , new AddListWrapper());
    				model.addAttribute("youtubePlaylistList", youtubePlaylistList);
    				model.addAttribute("usermadePlaylistList", usermadePlaylistList);
    				
    				return "playlistViewA";
    			} else {
    				model.addAttribute("youtubePlaylistInfoList", youtubePlaylistService.findYoutubePlaylistsInfo(channelId));
					model.addAttribute("usermadePlaylistInfoList", usermadePlaylistService.findDistinctPlaylistNameByChannelId(channelId));
					
					return "playlistView";
    			}
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String addPlaylist(
			@RequestParam(required = false, value = "name") String addPlaylistName,
			@RequestParam(required = false, value = "delete") String deletePlaylistName
	) throws IOException{		
		String userId = Auth.getUserId(userService);
		String channelId = YoutubeUserRepository.getChannelId(userId);		
		if (addPlaylistName != null){
			usermadePlaylistService.saveUsermadePlaylist(new UsermadePlaylist(10L, channelId, addPlaylistName));
		    String getPlaylist = "?playlist=" + addPlaylistName;
		    
			return "redirect:/viewPlaylist" + getPlaylist;
		} else if (deletePlaylistName != null) {
			usermadePlaylistService.deleteByChannelIdAndPlaylistName(channelId, deletePlaylistName);
			
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
		  
		String channelId = YoutubeUserRepository.getChannelId(Auth.getUserId(userService));
		usermadePlaylistService.add(addListWrapper.getAddList(), playlistName, channelId);

    	return "redirect:/viewPlaylist?playlist=" + playlistName;
    }    
}