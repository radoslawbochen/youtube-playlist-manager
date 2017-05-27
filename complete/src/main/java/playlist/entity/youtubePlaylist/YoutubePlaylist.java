package playlist.entity.youtubePlaylist;

import java.util.List;

import playlist.entity.PlaylistItemInfo;

public class YoutubePlaylist {
	
	public YoutubePlaylist(String name, String id, List<PlaylistItemInfo> playlistItemsInfoList) {
		super();
		this.name = name;
		this.id = id;
		this.playlistItemsInfoList = playlistItemsInfoList;
	}

	public YoutubePlaylist(){
	}	
	
	private String name;
	private String id;
	private List<PlaylistItemInfo> playlistItemsInfoList;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}	
	public List<PlaylistItemInfo> getPlaylistItemsInfoList() {
		return playlistItemsInfoList;
	}

	public void setPlaylistItemsInfoList(List<PlaylistItemInfo> playlistItemsInfoList) {
		this.playlistItemsInfoList = playlistItemsInfoList;
	}
			
}