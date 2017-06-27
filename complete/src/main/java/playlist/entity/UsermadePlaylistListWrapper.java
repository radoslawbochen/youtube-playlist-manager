package playlist.entity;

import java.util.List;

import playlist.entity.usermadePlaylist.UsermadePlaylist;

public class UsermadePlaylistListWrapper {

	public List<UsermadePlaylist> usermadePlaylistList;

	public List<UsermadePlaylist> getUsermadePlaylistList() {
		return usermadePlaylistList;
	}

	public void setUsermadePlaylistList(List<UsermadePlaylist> usermadePlaylistList) {
		this.usermadePlaylistList = usermadePlaylistList;
	}
	
}
