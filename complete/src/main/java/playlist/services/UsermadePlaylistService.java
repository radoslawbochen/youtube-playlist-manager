package playlist.services;

import java.util.ArrayList;
import java.util.List;

import playlist.entity.PlaylistLink;
import playlist.entity.User;
import playlist.entity.UsermadePlaylist;
import playlist.entity.UsermadePlaylistInfo;

public interface UsermadePlaylistService {

	List<User> findByChannelIdAndPlaylistName(String channelId, String playlistName);

	User saveUser(User user);

	List<User> findDistinctByChannelId(String channelId);

	List<UsermadePlaylistInfo> findDistinctPlaylistNameByChannelId();

	void deleteByChannelIdAndPlaylistName(String channelId, String playlistName);

	void deleteById(Long i);

	void add(String channelId, String playlistName, String link);
	
	ArrayList<User> findAllByLink(List<String> userId);

	User findbyLink(String link);

	void add(List<PlaylistLink> playlistLinkList, String addPlaylistName);

	void delete(ArrayList<User> userList, String playlistName);
	
}
