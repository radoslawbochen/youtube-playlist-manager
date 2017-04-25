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
import playlist.entity.PlaylistLink;
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
		Iterator<User> itr = this.usermadePlaylistRepo.findByChannelIdAndPlaylistName(channelId, playlistName).iterator();
		List<User> usermadePlaylistList = new ArrayList<User>();
		System.out.println(usermadePlaylistList.size());
		while(itr.hasNext()){
			User user = itr.next();
			if(user.getLink() != null){
				usermadePlaylistList.add(user);
			}
		}
		System.out.println(usermadePlaylistList.size());

		return usermadePlaylistList;
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

	@Override
	public void deleteById(Long id) {
		this.usermadePlaylistRepo.deleteById(id);
	}

	@Override
	public void add(String channelId, String playlistName, String link) {
		User user = new User(10L, channelId, playlistName, link, 0);
		this.usermadePlaylistRepo.save(user);
	}

	@Override
	public ArrayList<User> findAllByLink(List<String> userId) {		
		return this.usermadePlaylistRepo.findAllByLink(userId);
	}

	@Override
	public User findbyLink(String link){
		return this.usermadePlaylistRepo.findByLink(link);
	}

	@Override
	public void add(List<PlaylistLink> playlistLinkList, String playlistName) {
		String channelId = MyUploads.getChannelId();
		ListIterator<PlaylistLink> itr = playlistLinkList.listIterator();
		List<User> userList = new ArrayList<User>();
		while(itr.hasNext()){
			PlaylistLink p = itr.next();
			if(p.getLink() != null){
				userList.add(new User(
						10L,
						channelId,
						playlistName, 
						p.getLink(),
						0
						));
			}
		}
		
		ListIterator<User> userListItr = userList.listIterator();
 		while(userListItr.hasNext()){
			this.usermadePlaylistRepo.save(userListItr.next());
		}
	}

	@Override
	public void delete(ArrayList<User> userList, String playlistName) {
		Iterator<User> itr = userList.iterator();
		
		while(itr.hasNext()){
			User u = itr.next();
			if(u.getId() != null){
				this.usermadePlaylistRepo.deleteById(u.getId());
			}
		}
	}
	
}
