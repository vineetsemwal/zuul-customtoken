package com.cg.gateway.service;

import com.cg.gateway.dao.CredentialDao;
import com.cg.gateway.entities.UserCredential;
import com.cg.gateway.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CredentialServiceImpl implements CredentialService {

    private CredentialDao dao;

    public CredentialDao getDao() {
        return dao;
    }

    @Autowired
    public void setDao(CredentialDao dao) {
        this.dao = dao;
    }


    @Override
    public UserCredential save(UserCredential credential) {
        credential = dao.save(credential);
        return credential;
    }


    @Override
    public boolean checkCredentials(String username, String password) {
        if(username==null || username.isEmpty() || password==null || password.isEmpty()){
            return false;
        }
        UserCredential user = findByUsername(username);
        if (user == null) {
            return false;
        }
        return user.getUsername().equals(username) && user.getPassword().equals(password);
    }

    @Override
    public UserCredential findByUsername(String username) {
        Optional<UserCredential> optional = dao.findById(username);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new UserNotFoundException("user credentials not found in store");
    }

    @Override
    public boolean isAdmin(String username) {
       UserCredential userCredential= findByUsername(username);
       return "admin".equalsIgnoreCase(userCredential.getRole());
    }
}
