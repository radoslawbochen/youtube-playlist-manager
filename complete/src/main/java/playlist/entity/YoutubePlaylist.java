package playlist.entity;

import java.util.List;

public class YoutubePlaylist {
	
	public YoutubePlaylist(String name, int videosAmount, String id, List<String> playlistItemLinkList) {
		super();
		this.name = name;
		this.videosAmount = videosAmount;
		this.id = id;
		this.playlistItemLinkList = playlistItemLinkList;
	}
	
	public YoutubePlaylist(){
	}	
	
	private String name;
	private int videosAmount;
	private String id;
	private List<String> playlistItemLinkList;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getVideosAmount() {
		return videosAmount;
	}
	public void setVideosAmount(int videosAmount) {
		this.videosAmount = videosAmount;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getPlaylistItemLinkList() {
		return playlistItemLinkList;
	}
	public void setPlaylistItemLinkList(List<String> playlistItemLinkList) {
		this.playlistItemLinkList = playlistItemLinkList;
	}
	
	
	
}