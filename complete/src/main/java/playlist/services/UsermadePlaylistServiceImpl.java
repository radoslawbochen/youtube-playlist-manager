package playlist.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import playlist.controller.MyUploads;
import playlist.entity.User;
import playlist.entity.UsermadePlaylist;
import playlist.entity.UsermadePlaylistInfo;
import playlist.repositories.UsermadePlaylistRepository;

@Service
public class UsermadePlaylistServiceImpl implements UsermadePlaylistService {

	@Autowired
	private UsermadePlaylistRepository usermadePlaylistRepo;
		
	@Override
	public List<User> findByChannelIdAndPlaylistName(String channelId, String playlistName) {
		return this.usermadePlaylistRepo.findByChannelIdAndPlaylistName(channelId, playlistName);
	}
	
	@Override
	public User saveUser(User user){
		return this.usermadePlaylistRepo.save(user);
	}

	@Override
	public List<User> findDistinctByChannelId(String channelId) {
		return this.usermadePlaylistRepo.findDistinctByChannelId(channelId);
	}

	@Override
	public List<UsermadePlaylistInfo> findDistinctPlaylistNameByChannelId() {
		List<User> userList = usermadePlaylistRepo.findDistinctPlaylistNameByChannelId(MyUploads.getChannelId());
		System.out.print("ChannelId: " +  MyUploads.getChannelId());
		List<UsermadePlaylistInfo> userPlaylistList = new ArrayList<>();
		Iterator<User> itr = userList.iterator();
		Set<String> namesUniqueSet = new HashSet<>();
		List<String> userNameList = new ArrayList<String>();
				
		while(itr.hasNext()){
			User u = itr.next();
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
	public void deleteByChannelIdAndPlaylistName(String channelId, String playlistName) {
		this.usermadePlaylistRepo.deleteByChannelIdAndPlaylistName(channelId, playlistName);
	}

}
