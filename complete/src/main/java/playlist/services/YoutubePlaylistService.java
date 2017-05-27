package playlist.services;

import java.io.IOException;
import java.util.List;

import playlist.entity.usermadePlaylist.UsermadePlaylist;
import playlist.entity.youtubePlaylist.YoutubePlaylist;
import playlist.entity.youtubePlaylist.YoutubePlaylistInfo;

public interface YoutubePlaylistService {

	List<YoutubePlaylistInfo> findYoutubePlaylistsInfo(String channelId) throws IOException;

<<<<<<< HEAD
	List<YoutubePlaylist> findYoutubePlaylists(String channelId, List<UsermadePlaylist> usermadePlaylistList) throws IOException;
=======
	List<YoutubePlaylist> findYoutubePlaylistsByChanellId(String channelId, List<UsermadePlaylist> usermadePlaylistList) throws IOException;
>>>>>>> parent of 55a2df8... moved leftover logic from controllers to services

	UsermadePlaylist findbyLink(String link);

}
