package com._K.SnippetManager.persistence.dao;

import com._K.SnippetManager.persistence.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingDao extends JpaRepository<Rating,Long> {

}
