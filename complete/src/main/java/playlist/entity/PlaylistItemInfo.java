package playlist.entity;

public class PlaylistItemInfo {

	private String title;
	private String videoId;
		
	public PlaylistItemInfo() {	}
	
	public PlaylistItemInfo(String title, String videoId) {
		super();
		this.title = title;
		this.videoId = videoId;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getVideoId() {
		return videoId;
	}
	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	
	@Override
	public String toString(){
		return title + "\n" + videoId;
	}
	
}
