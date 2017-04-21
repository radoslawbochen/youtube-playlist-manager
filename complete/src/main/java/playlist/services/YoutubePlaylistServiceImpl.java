package playlist.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import playlist.controller.MyUploads;
import playlist.entity.User;
import playlist.entity.YoutubePlaylist;
import playlist.entity.YoutubePlaylistInfo;
import playlist.repositories.YoutubeUserRepository;

@Service
public class YoutubePlaylistServiceImpl implements YoutubePlaylistService {
	
	@Override
	public List<YoutubePlaylistInfo> findYoutubePlaylistsInfo() {
		return MyUploads.fetchPlaylistsInfoList();
	}

	@Override
	public List<YoutubePlaylist> findYoutubePlaylistsByChanellId(String channelId) {
		return MyUploads.fetchPlaylistList(channelId);
	}

	@Override
	public User findbyLink(String link) {
		User user = new User();
		
		return user;
	}

}
