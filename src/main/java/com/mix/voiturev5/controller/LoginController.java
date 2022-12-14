package com.mix.voiturev5.controller;

import com.mix.voiturev5.model.Administrateur;
import com.mix.voiturev5.model.Token;
import com.mix.voiturev5.model.Tutorial;
import com.mix.voiturev5.model.Voiture;
import com.mix.voiturev5.repository.AdministrateurRepository;
import com.mix.voiturev5.repository.TokenRepository;
import com.mix.voiturev5.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Adler32;

@RestController
@CrossOrigin
public class LoginController {
    @Autowired
    AdministrateurRepository administrateurRepository;
    @Autowired
    TokenRepository tokenRepository;


    @PostMapping("/login")
    public ResponseEntity<Token> loginAdmin(@RequestBody Administrateur administrateur) {
        try {
            System.out.println("email request body" + administrateur.getEmail());
            System.out.println("mdp request body" + administrateur.getPsswd());
            List<Administrateur> admin = administrateurRepository.findByEmailAndPsswd(administrateur.getEmail(),administrateur.getPsswd());
            if (admin.size() == 1){
                System.out.println("Admin" + admin.size());
                Token t = new TokenService().createToken(admin.get(0));
                Token saved = tokenRepository.save(t);
                System.out.println("LoginAdmin");
                System.out.println(saved.getId());
                System.out.println(saved.getDateexpiration());
                System.out.println(saved.getToken());
                return new ResponseEntity<>(saved, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

            }
        }
        @GetMapping("/login")
        public ResponseEntity<List<Administrateur>> getListe(){
            try {
                System.out.println("Okee");
                List<Administrateur> admin = new ArrayList<Administrateur>();
                    administrateurRepository.findAll().forEach(admin::add);
                if (admin.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(admin, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
