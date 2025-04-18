package com._K.SnippetManager.persistence.dao;

import com._K.SnippetManager.persistence.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogDao extends JpaRepository<Log,Long> {

}
