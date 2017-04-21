package playlist.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import playlist.entity.User;

@Repository
public interface UsermadePlaylistRepository extends JpaRepository<User, String>{
		
	List<User> findByChannelIdAndPlaylistName(String channelId, String playlistName);

	List<User> findDistinctByChannelId(String channelId);

	@Query("SELECT u FROM User u WHERE u.channelId = ?1")
	List<User> findDistinctPlaylistNameByChannelId(String channelId);

	@Transactional 
	@Modifying
	@Query("DELETE FROM User u WHERE u.channelId = ?1 AND u.playlistName = ?2")
	void deleteByChannelIdAndPlaylistName(String channelId, String playlistName);

	@Transactional 
	@Modifying
	void deleteById(Long id);

	//@Transactional 
	//@Modifying
	//<S> S save(User user);
	
	User findByLink(String link);
			
	ArrayList<User> findAllByLink(List<String> userId);

}
