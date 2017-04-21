package playlist.services;

import java.util.List;

import playlist.entity.User;
import playlist.entity.YoutubePlaylist;
import playlist.entity.YoutubePlaylistInfo;

public interface YoutubePlaylistService {

	List<YoutubePlaylistInfo> findYoutubePlaylistsInfo();

	List<YoutubePlaylist> findYoutubePlaylistsByChanellId(String channelId);

	User findbyLink(String link);
	
}
