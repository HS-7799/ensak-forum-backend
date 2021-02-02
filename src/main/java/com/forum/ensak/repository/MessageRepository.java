package com.forum.ensak.repository;

import com.forum.ensak.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message,Long> {

    Message getById(Long id);
}
