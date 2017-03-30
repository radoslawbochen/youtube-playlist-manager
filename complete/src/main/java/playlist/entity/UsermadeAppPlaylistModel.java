package playlist.entity;

import java.util.ArrayList;
import java.util.List;

public class UsermadeAppPlaylistModel {

	private static List<Object> usermadeAppPlaylistHelper = new ArrayList<Object>();

	public static List<Object> getUsermadeAppPlaylistHelper() {
		return usermadeAppPlaylistHelper;
	}

	public static void setUsermadeAppPlaylistHelper(List<Object> usermadeAppPlaylistHelper) {
		UsermadeAppPlaylistModel.usermadeAppPlaylistHelper = usermadeAppPlaylistHelper;
	}

}
