package com.project.asidesappbe.repositories;

import com.project.asidesappbe.models.Group;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends MongoRepository<Group, ObjectId> {
	
	@Query(value="{'_id' : ?0 }")
	Group findBy_groupId(String _groupId);
	Group findBy_groupId(ObjectId _groupId);
	Group findByGroupName(String groupName);
	@Query(value="{'inviteCode' : ?0 }")
	Group findByInviteCode(String inviteCode);
}
