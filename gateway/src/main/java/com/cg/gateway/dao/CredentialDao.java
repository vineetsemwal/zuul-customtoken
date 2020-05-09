package com.cg.gateway.dao;

import com.cg.gateway.entities.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialDao extends JpaRepository<UserCredential,String> {

}
