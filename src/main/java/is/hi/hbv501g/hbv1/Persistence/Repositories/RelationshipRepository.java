package is.hi.hbv501g.hbv1.Persistence.Repositories;

import is.hi.hbv501g.hbv1.Persistence.Entities.Relationship;
import is.hi.hbv501g.hbv1.Persistence.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RelationshipRepository extends JpaRepository<Relationship,Long> {
    List<Relationship> findBySender(User sender);
    List<Relationship> findByReciever(User reciever);
    Relationship save(Relationship relationship);
}
