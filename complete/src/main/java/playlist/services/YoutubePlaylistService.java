package playlist.services;

import java.util.List;

import playlist.entity.YoutubePlaylistInfo;

public interface YoutubePlaylistService {

	List<YoutubePlaylistInfo> findYoutubePlaylistsInfo();
	
}
