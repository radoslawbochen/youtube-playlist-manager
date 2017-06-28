package playlist.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;

import playlist.entity.usermadePlaylist.UsermadePlaylist;
import playlist.entity.usermadePlaylist.UsermadePlaylistInfo;

public interface UsermadePlaylistService {

	List<UsermadePlaylist> findByPlaylistName(String playlistName) throws IOException;

	UsermadePlaylist saveUsermadePlaylist(UsermadePlaylist usermadePlaylist);

	List<UsermadePlaylistInfo> findDistinctPlaylistName() throws IOException;

	void deleteById(Long i);
	
	void delete(ArrayList<UsermadePlaylist> userList, String playlistName);

	void addAll(ArrayList<UsermadePlaylist> usermadePlaylists);

	void deleteByPlaylistNameAndChannelId(String playlistName) throws IOException;

	void add(Credential credential, ArrayList<String> itemsInfoList, String playlistName);	
	
	List<UsermadePlaylist> compare(List<String> userLocalFilesNames, List<UsermadePlaylist> usermadePlaylist);
}
