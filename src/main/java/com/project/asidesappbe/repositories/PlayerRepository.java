package com.project.asidesappbe.repositories;

import com.project.asidesappbe.models.Player;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends MongoRepository<Player, ObjectId> {

	Player findByEmail(String email);
    @Query(value="{'_id' : ?0 }")
    Player findBy_playerId(String _playerId);
    Player findBy_playerId(ObjectId _playerId);
    Player findByUsername(String username);
}
