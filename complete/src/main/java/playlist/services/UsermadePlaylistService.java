package playlist.services;

import java.util.List;
import playlist.entity.User;
import playlist.entity.UsermadePlaylist;
import playlist.entity.UsermadePlaylistInfo;

public interface UsermadePlaylistService {

	List<User> findByChannelIdAndPlaylistName(String channelId, String playlistName);

	User saveUser(User user);

	List<User> findDistinctByChannelId(String channelId);

	List<UsermadePlaylistInfo> findDistinctPlaylistNameByChannelId();

	void deleteByChannelIdAndPlaylistName(String channelId, String playlistName);
	
}
