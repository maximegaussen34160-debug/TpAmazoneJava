package com.example.demo.User.repository;

import com.example.demo.User.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository {

    private final ConcurrentHashMap<Long, User> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final List<User> users = new ArrayList<>();

    public Optional<User> findByEmail(String email) {
        Optional<User> userFound = users.stream()
                .filter(user -> {
                    boolean emailExists = user.getEmail() != null;
                    boolean emailMatches = user.getEmail().equals(email);
                    
                    return emailExists && emailMatches;

                })
                .findFirst();

        
        return userFound;
    }

    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    public User addNewUser(String name, String email, String pswd, String salt) {
        Long id = System.currentTimeMillis() / 1000;

        User user = new User(id, name, email, pswd, salt);

        this.users.add(user);

        return user;
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idGenerator.getAndIncrement());
        }
        users.add(user);
        return user;
    }

    public List<User> findAlls() {
        return users;
    }

    public List<User> findAll() {
        return new ArrayList<>(store.values());
    }
}
