package playlist.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import playlist.entity.PlaylistItemInfo;
import playlist.entity.usermadePlaylist.UsermadePlaylist;
import playlist.entity.usermadePlaylist.UsermadePlaylistInfo;
import playlist.repositories.UsermadePlaylistRepository;
import playlist.repositories.YoutubeUserRepository;
import playlist.security.Auth;

@Service
public class UsermadePlaylistServiceImpl implements UsermadePlaylistService {

	@Autowired
	private UsermadePlaylistRepository usermadePlaylistRepo;
	@Autowired
	private UserService userService;
	
	
	@Override
	public List<UsermadePlaylist> findByPlaylistName(String playlistName) {
		String channelId = YoutubeUserRepository.getChannelId(Auth.getUserId(userService));

		List<UsermadePlaylist> usermadePlaylistList = new ArrayList<UsermadePlaylist>();
		
		for (UsermadePlaylist usermadePlaylist : this.usermadePlaylistRepo.findByChannelIdAndPlaylistName(channelId, playlistName)) {
			if(usermadePlaylist.getLink() != null){
				usermadePlaylistList.add(usermadePlaylist);
			}
		}
		
		return usermadePlaylistList;
	}
	
	@Override
	public void saveUsermadePlaylist(UsermadePlaylist usermadePlaylist){
		this.usermadePlaylistRepo.save(usermadePlaylist);
	}

	@Override
	public List<UsermadePlaylistInfo> findDistinctPlaylistName() {
		String channelId = YoutubeUserRepository.getChannelId(Auth.getUserId(userService));

		List<UsermadePlaylist> userList = usermadePlaylistRepo.findDistinctPlaylistNameByChannelId(channelId);
		List<UsermadePlaylistInfo> userPlaylistList = new ArrayList<>();
		Iterator<UsermadePlaylist> itr = userList.iterator();
		Set<String> namesUniqueSet = new HashSet<>();
		List<String> userNameList = new ArrayList<String>();
				
		while(itr.hasNext()){
			UsermadePlaylist u = itr.next();
			userNameList.add(u.getPlaylistName());			
			if(!namesUniqueSet.contains(u.getPlaylistName())){
				namesUniqueSet.add(u.getPlaylistName());
			}
		}
		
		for(String playlistName : namesUniqueSet){
			userPlaylistList.add(new UsermadePlaylistInfo(playlistName, Collections.frequency(userNameList, playlistName)));
		}

		return userPlaylistList;
	}

	@Override
	public void deleteByPlaylistName(String playlistName) {
		String channelId = YoutubeUserRepository.getChannelId(Auth.getUserId(userService));

		this.usermadePlaylistRepo.deleteByChannelIdAndPlaylistName(channelId, playlistName);
	}

	@Override
	public void deleteById(Long id) {
		this.usermadePlaylistRepo.deleteById(id);
	}

	@Override
	public void add(ArrayList<String> itemsInfoList, String playlistName){		
		String channelId = YoutubeUserRepository.getChannelId(Auth.getUserId(userService));
		
		List<PlaylistItemInfo> playlistItemInfoList = new ArrayList<>();
		List<UsermadePlaylist> usermadePlaylists = new ArrayList<UsermadePlaylist>();
		
		for (String itemInfo : itemsInfoList){
				if(itemInfo != null){
					String[] lines = itemInfo.split("\n");
					playlistItemInfoList.add(new PlaylistItemInfo(lines[0], lines[1]));
					usermadePlaylists.add(new UsermadePlaylist(
							10L,
							channelId,
							playlistName, 
							lines[1],
							0,
							lines[0]
							));
			}			
		}	
		
 		for (UsermadePlaylist usermadePlaylist : usermadePlaylists){
			this.usermadePlaylistRepo.save(usermadePlaylist);
		}
	}
	
	@Override
	public void delete(ArrayList<UsermadePlaylist> usermadePlaylists, String playlistName) {
		for (UsermadePlaylist usermadePlaylist : usermadePlaylists){
			if(usermadePlaylist.getId() != null){
				this.usermadePlaylistRepo.deleteById(usermadePlaylist.getId());
			}
		}
	}

	@Override
	public void addAll(ArrayList<UsermadePlaylist> usermadePlaylists) {
		for (UsermadePlaylist usermadePlaylist : usermadePlaylists){
			this.saveUsermadePlaylist(usermadePlaylist);
		}
	}
	
}
