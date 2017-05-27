package playlist.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import playlist.entity.usermadePlaylist.UsermadePlaylist;

@Repository
public interface UsermadePlaylistRepository extends JpaRepository<UsermadePlaylist, String>{
		
	List<UsermadePlaylist> findByChannelIdAndPlaylistName(String channelId, String playlistName);

	@Query("SELECT u FROM usermadePlaylist u WHERE u.channelId = ?1")
	List<UsermadePlaylist> findDistinctPlaylistNameByChannelId(String channelId);

	@Transactional 
	@Modifying
	@Query("DELETE FROM usermadePlaylist u WHERE u.channelId = ?1 AND u.playlistName = ?2")
	void deleteByChannelIdAndPlaylistName(String channelId, String playlistName);

	@Transactional 
	@Modifying
	void deleteById(Long id);
	
}
