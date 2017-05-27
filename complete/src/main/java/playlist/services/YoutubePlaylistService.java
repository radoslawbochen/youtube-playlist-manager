package playlist.services;

import java.io.IOException;
import java.util.List;

import playlist.entity.usermadePlaylist.UsermadePlaylist;
import playlist.entity.youtubePlaylist.YoutubePlaylist;
import playlist.entity.youtubePlaylist.YoutubePlaylistInfo;

public interface YoutubePlaylistService {

	List<YoutubePlaylistInfo> findYoutubePlaylistsInfo();

	List<YoutubePlaylist> findYoutubePlaylists(String channelId, List<UsermadePlaylist> usermadePlaylistList) throws IOException;

	UsermadePlaylist findbyLink(String link);

}
