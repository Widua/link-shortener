package me.widua.linkshortener.repository;

import me.widua.linkshortener.entity.LinkModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepository extends CrudRepository<LinkModel, String> {

}
