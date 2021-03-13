package dmcs.rwitczyk.repository;

import dmcs.rwitczyk.domains.UserLoginDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginDataRepository extends JpaRepository<UserLoginDataEntity, Integer> {

    UserLoginDataEntity findUserLoginDataEntityByEmail(String email);
}
