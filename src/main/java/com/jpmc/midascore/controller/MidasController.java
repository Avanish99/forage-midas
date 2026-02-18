package com.jpmc.midascore.controller;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class MidasController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/balance")
    public Balance getBalance(@RequestParam("userId") long id){
        Optional<UserRecord> user=userRepository.findById(id);
        if(user.isPresent()) {
            UserRecord u = user.get();
            return new Balance(u.getBalance());
        }
        else {
            return new Balance(0.0f);
        }


    }
}
