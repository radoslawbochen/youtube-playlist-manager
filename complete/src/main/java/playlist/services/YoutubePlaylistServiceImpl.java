package playlist.services;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;

import playlist.entity.User;
import playlist.entity.usermadePlaylist.UsermadePlaylist;
import playlist.entity.youtubePlaylist.YoutubePlaylist;
import playlist.entity.youtubePlaylist.YoutubePlaylistInfo;
import playlist.repositories.YoutubeUserRepository;
import playlist.security.Auth;

@Service
public class YoutubePlaylistServiceImpl implements YoutubePlaylistService {
	
	
	@Autowired 
	UsermadePlaylistService usermadePlaylistService;
	
	@Autowired 
	UserService userService;
	
	@Override
	public List<YoutubePlaylistInfo> findYoutubePlaylistsInfo(Credential credential) {		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		String userId = String.valueOf(user.getId());
		String channelId = YoutubeUserRepository.getChannelId(userId);
		System.out.println("DUPADUPADUPADUPADUPADUPADUPADUPADUPADUPADUPADUPA " + userId);
		return YoutubeUserRepository.fetchPlaylistsInfoList(credential, channelId, userId);
	}

	@Override
	public List<YoutubePlaylist> findYoutubePlaylists(Credential credential, String channelId, List<UsermadePlaylist> usermadePlaylists) {	
		//String channelId = YoutubeUserRepository.getChannelId(Auth.getUserId(userService));
		List<YoutubePlaylist> youtubePlaylists = YoutubeUserRepository.fetchPlaylistList(credential, channelId);
				
		for (UsermadePlaylist usermadePlaylist : usermadePlaylists){
			for (YoutubePlaylist youtubePlaylist : youtubePlaylists){
				for (int i3 = 0; i3 < youtubePlaylist.getPlaylistItemsInfoList().size(); ++i3){
					if (usermadePlaylist.getLink().equals(youtubePlaylist.getPlaylistItemsInfoList().get(i3).getVideoId())){
						youtubePlaylist.getPlaylistItemsInfoList().remove(i3);
					}
				}
			}
		}

		return youtubePlaylists;
	}

	@Override
	public UsermadePlaylist findbyLink(String link) {
		UsermadePlaylist usermadePlaylist = new UsermadePlaylist();
		
		return usermadePlaylist;
	}

}
