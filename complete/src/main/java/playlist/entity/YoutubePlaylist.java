package playlist.entity;

import java.util.List;

public class YoutubePlaylist {
	
	public YoutubePlaylist(String name, int videosAmount, String id, List<String> playlistItemsLinksList, List<String> playlistItemsTitlesList) {
		super();
		this.name = name;
		this.videosAmount = videosAmount;
		this.id = id;
		this.playlistItemsLinksList = playlistItemsLinksList;
		this.playlistItemsTitlesList = playlistItemsTitlesList;
	}
	
	public YoutubePlaylist(){
	}	
	
	private String name;
	private int videosAmount;
	private String id;
	private List<String> playlistItemsLinksList;
	private List<String> playlistItemsTitlesList;
	
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
	public List<String> getPlaylistItemsLinksList() {
		return playlistItemsLinksList;
	}
	public void setPlaylistItemsLinksList(List<String> playlistItemsLinksList) {
		this.playlistItemsLinksList = playlistItemsLinksList;
	}

	public List<String> getPlaylistItemsTitlesList() {
		return playlistItemsTitlesList;
	}

	public void setPlaylistItemsTitlesList(List<String> playlistItemsTitlesList) {
		this.playlistItemsTitlesList = playlistItemsTitlesList;
	}
	
	
	
}