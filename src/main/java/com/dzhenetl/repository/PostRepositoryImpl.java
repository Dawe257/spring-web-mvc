package com.dzhenetl.repository;

import com.dzhenetl.exception.NotFoundException;
import com.dzhenetl.model.Post;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final Map<Long, Post> repository;
    private AtomicLong postCounter;

    public PostRepositoryImpl() {
        this.repository = new ConcurrentHashMap<>();
        postCounter = new AtomicLong(0);
    }

    public List<Post> all() {
        return new ArrayList<>(repository.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(repository.get(id));
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            post.setId(postCounter.incrementAndGet());
            repository.put(post.getId(), post);
        } else {
            Post postToUpdate = Optional.ofNullable(repository.get(post.getId())).orElseThrow(NotFoundException::new);
            postToUpdate.setContent(post.getContent());
        }
        return post;
    }

    public void removeById(long id) {
        Optional.ofNullable(repository.remove(id)).orElseThrow(NotFoundException::new);
    }
}