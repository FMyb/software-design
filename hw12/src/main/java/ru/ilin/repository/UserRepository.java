package ru.ilin.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import ru.ilin.document.User;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {
}
