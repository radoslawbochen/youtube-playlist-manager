package playlist.entity;

import java.util.ArrayList;
import java.util.List;

public class PlaylistInfoModel {
	
	private static List<Object> playlistInfoHelper = new ArrayList<Object>();

	public static List<Object> getPlaylistInfoHelper() {
		return playlistInfoHelper;
	}

	public static void setPlaylistInfoHelper(List<Object> playlistInfoHelper) {
		PlaylistInfoModel.playlistInfoHelper = playlistInfoHelper;
	}
}
