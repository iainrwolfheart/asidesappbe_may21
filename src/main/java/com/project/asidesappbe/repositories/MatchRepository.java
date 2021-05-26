package com.project.asidesappbe.repositories;

import com.project.asidesappbe.models.Match;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends MongoRepository<Match, ObjectId> {

    @Query(value="{'_id' : ?0 }")
    Match findBy_matchId(String _matchId);
}
