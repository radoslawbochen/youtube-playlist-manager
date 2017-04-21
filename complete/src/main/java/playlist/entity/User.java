package playlist.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "playlist")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	@Column
	private String channelId;
	@Column
	private String playlistName;
	@Column
	private String link;
	@Column
	private int timesRepeated;
	
	public User() { }
	
	public User(Long id){
		super();
		this.id = id;
	}
	
	public User(Long id, String channelId, String playlistName, String link, int timesRepeated) {
		super();
		this.id = id;
		this.channelId = channelId;
		this.playlistName = playlistName;
		this.link = link;
		this.timesRepeated = timesRepeated;
	}
	
	public User(Long id, String channelId, String playlistName){
		super();
		this.id = id;
		this.channelId = channelId;
		this.playlistName = playlistName;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPlaylistName() {
		return playlistName;
	}
	public void setPlaylistName(String playlistName) {
		this.playlistName = playlistName;
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
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
}
