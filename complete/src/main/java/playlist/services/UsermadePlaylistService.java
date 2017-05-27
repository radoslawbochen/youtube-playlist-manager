package playlist.services;

import java.util.ArrayList;
import java.util.List;

import playlist.entity.usermadePlaylist.UsermadePlaylist;
import playlist.entity.usermadePlaylist.UsermadePlaylistInfo;

public interface UsermadePlaylistService {

	List<UsermadePlaylist> findByChannelIdAndPlaylistName(String channelId, String playlistName);

	void saveUsermadePlaylist(UsermadePlaylist usermadePlaylist);

	List<UsermadePlaylistInfo> findDistinctPlaylistNameByChannelId(String channelId);

	void deleteByChannelIdAndPlaylistName(String channelId, String playlistName);

	void deleteById(Long i);
	
	void add(ArrayList<String> arrayList, String addPlaylistName, String channelId);

	void delete(ArrayList<UsermadePlaylist> userList, String playlistName);

	void addAll(ArrayList<UsermadePlaylist> usermadePlaylists);	
}
