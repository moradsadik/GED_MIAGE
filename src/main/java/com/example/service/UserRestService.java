package com.example.service;

import com.example.DemoApplication;
import com.example.dao.DocumentRepository;
import com.example.dao.GroupeRepository;
import com.example.dao.RoleRepository;
import com.example.dao.UserRepository;
import com.example.entity.Document;
import com.example.entity.Groupe;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.metier.UserMetier;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import elemental.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resources;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;


@RestController
public class UserRestService {

    @Autowired
    private UserMetier ur;
    @Autowired
    private GroupeRepository gr;
    @Autowired
    private RoleRepository rr;

    @RequestMapping(value = "/")
    public String home(){
        return "index";
    }

    @RequestMapping(value="/user", method= RequestMethod.GET)
    public List<User> users(){
        return ur.listUser();
    }

    @RequestMapping(value="/user/{id}", method = RequestMethod.GET)
    public User userByName(@PathVariable(value="id") int id){
        return ur.getUser(id);
    }

    @RequestMapping(value="/user/{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable(value="id") int id){
         ur.deleteUser(id);
    }

    @RequestMapping(value="/user", method = RequestMethod.POST)
    User addUser(@RequestParam("user") String u,
                 @RequestParam("groupes") Collection<Integer> groupes,
                 @RequestParam("roles") Collection<Integer> roles){

        User user = new Gson().fromJson(u, User.class);

        Set<Groupe> g = new HashSet<>();
        for (int j : groupes) {
            Groupe gp = gr.findOne(j);
            g.add(gp);
        }
        user.setGroupes(g);

        Set<Role> rs = new HashSet<>();
        for (int i : roles) {
            Role r = rr.findOne(i);
            rs.add(r);
        }
        user.setRoles(rs);

        return ur.saveUser(user);
    }

    @RequestMapping(value="/user/{id}", method=RequestMethod.PUT)
    public User userUpdate(@PathVariable(value="id") int id,@RequestBody User user ){
        return  ur.updateUser(id, user);
    }

    @RequestMapping(value = "/role", method = RequestMethod.POST)
    public Role addRole(@RequestBody Role r){
        return rr.save(r);
    }

    @RequestMapping(value = "/role", method = RequestMethod.GET)
    public Collection<Role> getAllRoles(){
        return rr.findAll();
    }
    @RequestMapping(value = "/role/{id}", method = RequestMethod.GET)
    public void getRole(@PathVariable int id){
        rr.findOne(id);
    }

    @RequestMapping(value = "/role/{id}", method = RequestMethod.DELETE)
    public void deleteRole(@PathVariable int id){
        rr.delete(id);
    }


    @RequestMapping(value = "/connection", method = RequestMethod.POST)
    public User authentification(@RequestParam("mail") String mail, @RequestParam("password") String password) {
//        Gson json = new GsonBuilder().create();
//        System.out.println(json.fromJson(mail,String[].class));
        return ur.findUser(mail, password);
    }
}
