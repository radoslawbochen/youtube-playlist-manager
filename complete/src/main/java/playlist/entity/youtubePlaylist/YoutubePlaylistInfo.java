package playlist.entity.youtubePlaylist;

public class YoutubePlaylistInfo {

	private String name;
	private int videosAmount;
	private String playlistId;
	
	public YoutubePlaylistInfo(String name, int videosAmount, String playlistId) {
		this.name = name;
		this.videosAmount = videosAmount;
		this.playlistId = playlistId;
	}
	
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
	public String getPlaylistId() {
		return playlistId;
	}
	public void setPlaylistId(String playlistId) {
		this.playlistId = playlistId;
	}
	
}
