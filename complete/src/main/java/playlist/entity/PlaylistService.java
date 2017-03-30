package playlist.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import playlist.controller.MyUploads;
import playlist.controller.UserContents;


@Service("playlistService")
public class PlaylistService {
	
	@Autowired
	UserContents userContents;
	
	private List<Object> playlistInfoHelper = new ArrayList<Object>();
	private List<Object> usermadeAppPlaylistHelper = new ArrayList<Object>();
	
	public void setPlaylistInfoModel(){
		prepareMyUploadsInfo();
		prepareUserPlaylistsInfo();
		//PlaylistInfoModel playlistInfoModel = new PlaylistInfoModel();
		PlaylistInfoModel.setPlaylistInfoHelper(playlistInfoHelper);
	}
	
	public void prepareMyUploadsInfo(){
		playlistInfoHelper.add(new PlaylistInfoHelper(
				userContents.getId(),
				userContents.getName(),
				userContents.getSize()
				));
	}
	
	public void prepareUserPlaylistsInfo(){
		if (userContents.getPlaylistsAmount() != 0){
			int i = userContents.getPlaylistsAmount();
			while (i > 0){
				i--;
				playlistInfoHelper.add(new PlaylistInfoHelper(
						userContents.getUserPlaylistIdList().get(i),
						userContents.getUserPlaylistNameList().get(i),
						userContents.getUserPlaylistSizeList().get(i)
						));
			}
		}
	}
	
	public void prepareUsermadeAppPlaylistInfo(){
		Map<String, Integer> map = userContents.getUsermadeAppPlaylistInfoMap();
		Iterator itr = map.entrySet().iterator();
		while (itr.hasNext()){
			Map.Entry pair = (Map.Entry)itr.next();
			usermadeAppPlaylistHelper.add(new UsermadeAppPlaylistHelper(
					pair.getKey().toString(),
					(int) pair.getValue()
					));
		}
		UsermadeAppPlaylistModel.setUsermadeAppPlaylistHelper(usermadeAppPlaylistHelper);
	}
}