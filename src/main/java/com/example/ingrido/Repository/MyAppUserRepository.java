package com.example.ingrido.Repository;

import java.util.Optional;

import com.example.ingrido.Model.MyAppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyAppUserRepository extends JpaRepository<MyAppUser, Long>{

        Optional<MyAppUser> findByUsername(String username);

}