package playlist.entity;

public class UsermadePlaylist {

	private String link;
	private int timesRepeated;
	
	public UsermadePlaylist(String link, int timesRepeated) {
		super();
		this.link = link;
		this.timesRepeated = timesRepeated;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public int getTimesRepeated() {
		return timesRepeated;
	}
	public void setTimesRepeated(int timesRepeated) {
		this.timesRepeated = timesRepeated;
	}
}
