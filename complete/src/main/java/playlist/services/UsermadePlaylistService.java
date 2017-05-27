package playlist.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import playlist.entity.usermadePlaylist.UsermadePlaylist;
import playlist.entity.usermadePlaylist.UsermadePlaylistInfo;

public interface UsermadePlaylistService {

	List<UsermadePlaylist> findByPlaylistName(String playlistName);

	void saveUsermadePlaylist(UsermadePlaylist usermadePlaylist);

	List<UsermadePlaylistInfo> findDistinctPlaylistName();

	void deleteByPlaylistName(String playlistName);

	void deleteById(Long i);
	
	void add(ArrayList<String> arrayList, String addPlaylistName) throws IOException;

	void delete(ArrayList<UsermadePlaylist> userList, String playlistName);

	void addAll(ArrayList<UsermadePlaylist> usermadePlaylists);	
}
