package playlist.entity;

public class UsermadeAppPlaylistHelper {

	private String name;
	private int videosAmount;

	public UsermadeAppPlaylistHelper(String name, int videosAmount) {
		this.name = name;
		this.videosAmount = videosAmount;
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
}
