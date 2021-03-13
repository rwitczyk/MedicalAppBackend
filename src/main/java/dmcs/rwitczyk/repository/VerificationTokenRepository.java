package dmcs.rwitczyk.repository;

import dmcs.rwitczyk.domains.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {

    VerificationToken findByToken(UUID token);

    void removeByToken(UUID token);
}
