package playlist.services;

import java.io.IOException;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import playlist.entity.User;
import playlist.entity.usermadePlaylist.UsermadePlaylist;
import playlist.entity.youtubePlaylist.YoutubePlaylist;
import playlist.entity.youtubePlaylist.YoutubePlaylistInfo;
import playlist.repositories.YoutubeUserRepository;

@Service
public class YoutubePlaylistServiceImpl implements YoutubePlaylistService {
	
	
	@Autowired 
	UsermadePlaylistService usermadePlaylistService;
	
	@Autowired 
	UserService userService;
	
	@Override
	public List<YoutubePlaylistInfo> findYoutubePlaylistsInfo(String channelId) throws IOException {		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
<<<<<<< HEAD
		String userId = String.valueOf(user.getId());
		String channelId = YoutubeUserRepository.getChannelId(userId);
		System.out.println("DUPADUPADUPADUPADUPADUPADUPADUPADUPADUPADUPADUPA " + userId);
		return YoutubeUserRepository.fetchPlaylistsInfoList(channelId, userId);
	}

	@Override
	public List<YoutubePlaylist> findYoutubePlaylists(String channelId, List<UsermadePlaylist> usermadePlaylists) {	
		//String channelId = YoutubeUserRepository.getChannelId(Auth.getUserId(userService));
=======
		String userId = String.valueOf(user.getId()); 
		
		return youtubeUserRepo.fetchPlaylistsInfoList(channelId, userId);
	}

	@Override
	public List<YoutubePlaylist> findYoutubePlaylistsByChanellId(String channelId, List<UsermadePlaylist> usermadePlaylists) {		
>>>>>>> parent of 55a2df8... moved leftover logic from controllers to services
		List<YoutubePlaylist> youtubePlaylists = YoutubeUserRepository.fetchPlaylistList(channelId);
				
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
