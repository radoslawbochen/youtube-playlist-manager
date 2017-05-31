package playlist.services;

import java.util.ArrayList;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;

import playlist.entity.usermadePlaylist.UsermadePlaylist;
import playlist.entity.usermadePlaylist.UsermadePlaylistInfo;

public interface UsermadePlaylistService {

	List<UsermadePlaylist> findByPlaylistName(Credential credential, String playlistName);

	UsermadePlaylist saveUsermadePlaylist(UsermadePlaylist usermadePlaylist);

	List<UsermadePlaylistInfo> findDistinctPlaylistName(Credential credential);

	void deleteById(Long i);
	
	void delete(ArrayList<UsermadePlaylist> userList, String playlistName);

	void addAll(ArrayList<UsermadePlaylist> usermadePlaylists);

	void deleteByPlaylistNameAndChannelId(Credential credential, String playlistName);

	void add(Credential credential, ArrayList<String> itemsInfoList, String playlistName);	
}
