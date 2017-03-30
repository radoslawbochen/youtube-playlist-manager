package playlist.entity;

public class YoutubePlaylistInfo {

	private String name;
	private int videosAmount;
	private String link;
	
	public YoutubePlaylistInfo(String name, int videosAmount, String link) {
		this.name = name;
		this.videosAmount = videosAmount;
		this.link = link;
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
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
}
