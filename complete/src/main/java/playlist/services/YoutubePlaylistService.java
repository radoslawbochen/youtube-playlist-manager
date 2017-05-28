package playlist.services;

import java.io.IOException;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;

import playlist.entity.usermadePlaylist.UsermadePlaylist;
import playlist.entity.youtubePlaylist.YoutubePlaylist;
import playlist.entity.youtubePlaylist.YoutubePlaylistInfo;

public interface YoutubePlaylistService {

	List<YoutubePlaylistInfo> findYoutubePlaylistsInfo(Credential credential);

	List<YoutubePlaylist> findYoutubePlaylists(Credential credential, String channelId, List<UsermadePlaylist> usermadePlaylistList) throws IOException;

	UsermadePlaylist findbyLink(String link);

}
