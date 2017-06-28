package playlist.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.api.client.auth.oauth2.Credential;

import playlist.entity.AddListWrapper;
import playlist.entity.usermadePlaylist.UsermadePlaylist;
import playlist.entity.usermadePlaylist.UsermadePlaylistWrapper;
import playlist.entity.youtubePlaylist.YoutubePlaylist;
import playlist.security.Auth;
import playlist.services.UserService;
import playlist.services.UsermadePlaylistService;
import playlist.services.YoutubeAccountService;
import playlist.services.YoutubePlaylistService;

@Controller
@RequestMapping("/viewPlaylist")
public class ViewPlaylistController {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAutoGrowCollectionLimit(2048);
    }
    
    @Autowired
    private YoutubeAccountService youtubeAccountService;
    @Autowired
    private UserService userService;
	@Autowired
	private UsermadePlaylistService usermadePlaylistService;
	@Autowired
	private YoutubePlaylistService youtubePlaylistService;
	
	@RequestMapping(method = RequestMethod.GET)
    public String playlistView(    		
    		@RequestParam(value = "playlist", required = false) String playlistName,
    		RedirectAttributes redirectAttributes,
			Model model
			) throws IOException{		
				if(Auth.getFlow() == null){
					return "redirect:/login";
				}
				
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				
    			if (playlistName != null){
    				List<UsermadePlaylist> usermadePlaylistListt = usermadePlaylistService.findByPlaylistName(playlistName);
    				List<YoutubePlaylist> youtubePlaylistList = youtubePlaylistService.findYoutubePlaylists(usermadePlaylistListt);
    				ArrayList<UsermadePlaylist> usermadePlaylistList = new ArrayList<UsermadePlaylist>(usermadePlaylistListt);
 				    				
    				model.addAttribute("usermadePlaylist", new UsermadePlaylistWrapper());
    				model.addAttribute("playlistName", playlistName);
    				model.addAttribute("addList" , new AddListWrapper());
    				model.addAttribute("youtubePlaylistList", youtubePlaylistList);
    				model.addAttribute("usermadePlaylistList", usermadePlaylistList);
    				model.addAttribute("filesToCompare", new ArrayList<String>());
    				
    				return "playlistViewA";
    			} else {
    				model.addAttribute("youtubePlaylistInfoList", youtubePlaylistService.findYoutubePlaylistsInfo());
					model.addAttribute("usermadePlaylistInfoList", usermadePlaylistService.findDistinctPlaylistName());
					
					return "playlistView";
    			}
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String modifyUsermadePlaylist(
			@RequestParam(required = false, value = "add") String addPlaylistName,
			@RequestParam(required = false, value = "delete") String deletePlaylistName
	) throws IOException{		
		if (addPlaylistName != null){
			usermadePlaylistService.saveUsermadePlaylist(new UsermadePlaylist(10L, youtubeAccountService.getChannelId(), addPlaylistName));
		    String getPlaylist = "?playlist=" + addPlaylistName;
		    
			return "redirect:/viewPlaylist" + getPlaylist;
		} else if (deletePlaylistName != null) {
			usermadePlaylistService.deleteByPlaylistNameAndChannelId(deletePlaylistName);
			
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

    @RequestMapping(value = "/compare", method = RequestMethod.POST)
    @ResponseBody List<UsermadePlaylist> compareUserFilesToPlaylist(
    		@RequestParam("playlistName") String playlistName,
    		@RequestParam("files[]") String[] files,
    		HttpServletResponse response
    		) throws IOException{
    	List<String> userFilesNamesToCompare = Arrays.asList(files);
    	List<UsermadePlaylist> usermadePlaylist = usermadePlaylistService.findByPlaylistName(playlistName);
    	List<UsermadePlaylist> comparedPlaylist = usermadePlaylistService.compare(userFilesNamesToCompare, usermadePlaylist);
    	
    	return comparedPlaylist;
    }
    
    @RequestMapping(value = "/links", method = RequestMethod.GET)
    String showPlaylistLinks(
    		@RequestParam("playlistName") String playlistName,
    		Model model
    		) throws IOException{
    	
    	model.addAttribute("playlist", usermadePlaylistService.findByPlaylistName(playlistName));
    	
    	return "links";
    }
}
