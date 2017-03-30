package playlist.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import playlist.entity.PlaylistInfoHelper;
import playlist.entity.PlaylistInfoModel;

@Service("userUploadsPlaylist")
public class UserUploadsPlaylist implements Playlist {

	@Autowired
	UserContents userContents;
	
	@Override
	public void playlistInfo(List<Object> playlistInfoHelper) {
		playlistInfoHelper.add(new PlaylistInfoHelper(
				userContents.getId(),
				userContents.getName(),
				userContents.getSize()
				));
		
		PlaylistInfoModel.setPlaylistInfoHelper(playlistInfoHelper);
	}
}
