package playlist.repositories;

import java.util.List;

import playlist.controller.MyUploads;
import playlist.entity.YoutubePlaylistInfo;

public class YoutubeUserRepository {

	List<YoutubePlaylistInfo> youtubePlaylistInfoList = MyUploads.fetchPlaylistsInfoList();
		
}
