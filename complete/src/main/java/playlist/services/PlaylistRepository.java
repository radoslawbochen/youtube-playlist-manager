package playlist.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import playlist.entity.User;

public interface PlaylistRepository extends JpaRepository<User, String> {

	@Transactional 
	@Modifying
	<S> S save(User user);
	
}
