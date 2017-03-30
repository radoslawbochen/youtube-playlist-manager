package playlist.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class UserContents {

	private int playlistSize;
	private String channelId;
	private String id;
	private String name;
	private int playlistsAmount;
	private List<String> userPlaylistIdList = new ArrayList<String>();
	private List<Integer> userPlaylistSizeList = new ArrayList<Integer>();
	private List<String> userPlaylistNameList = new ArrayList<String>();
	
	// Usermade in-app playlists
	Map<String, Integer> usermadeAppPlaylistInfoMap = new HashMap<String, Integer>();
	
	private List<String> usermadeAppPlaylistNameList = new ArrayList<String>();
	private List<Integer> usermadeAppPlaylistVideosAmountList = new ArrayList<Integer>();

	
    public int getSize() {
		return playlistSize;
	}
	public void setPlaylistSize(int playlistSize) {
		this.playlistSize = playlistSize;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
        channelId = channelId.replace("-", "");
        this.channelId = channelId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPlaylistsAmount() {
		return playlistsAmount;
	}
	public void setPlaylistsAmount(int playlistsAmount) {
		this.playlistsAmount = playlistsAmount;
	}
	public List<String> getUserPlaylistIdList() {
		return userPlaylistIdList;
	}
	public void setUserPlaylistIdList(List<String> userPlaylistIdList) {
		this.userPlaylistIdList = userPlaylistIdList;
	}
	public List<Integer> getUserPlaylistSizeList() {
		return userPlaylistSizeList;
	}
	public void setUserPlaylistSizeList(List<Integer> userPlaylistSizeList) {
		this.userPlaylistSizeList = userPlaylistSizeList;
	}
	public List<String> getUserPlaylistNameList() {
		return userPlaylistNameList;
	}
	public void setUserPlaylistNameList(List<String> userPlaylistNameList) {
		this.userPlaylistNameList = userPlaylistNameList;
	}
	public List<String> getUsermadeAppPlaylistNameList() {
		return usermadeAppPlaylistNameList;
	}
	public void setUsermadeAppPlaylistNameList(List<String> usermadeAppPlaylistNameList) {
		this.usermadeAppPlaylistNameList = usermadeAppPlaylistNameList;
	}
	public List<Integer> getUsermadeAppPlaylistVideosAmountList() {
		return usermadeAppPlaylistVideosAmountList;
	}
	public void setUsermadeAppPlaylistVideosAmountList(List<Integer> usermadeAppPlaylistVideosAmountList) {
		this.usermadeAppPlaylistVideosAmountList = usermadeAppPlaylistVideosAmountList;
	}
	public Map<String, Integer> getUsermadeAppPlaylistInfoMap() {
		return usermadeAppPlaylistInfoMap;
	}
	public void setUsermadeAppPlaylistInfoMap(Map<String, Integer> usermadeAppPlaylistInfoMap) {
		this.usermadeAppPlaylistInfoMap = usermadeAppPlaylistInfoMap;
	}
	
}
