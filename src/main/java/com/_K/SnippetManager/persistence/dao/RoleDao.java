package com._K.SnippetManager.persistence.dao;

import com._K.SnippetManager.persistence.entity.Log;
import com._K.SnippetManager.persistence.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleDao extends JpaRepository<Role, Long> {
    Role findByRoleName(String roleName);
}
