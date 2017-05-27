package playlist.repositories;

import org.springframework.stereotype.Repository;

import playlist.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long>{

	User findByEmail(String email);
	
}
