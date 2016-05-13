package com.example.service;

import com.example.dao.CategorieRepository;
import com.example.dao.DocumentRepository;
import com.example.dao.GroupeRepository;
import com.example.entity.Categorie;
import com.example.entity.Groupe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class GroupCategorieService {
    @Autowired
    GroupeRepository gr;
    @Autowired
    CategorieRepository cr;
    @Autowired
    DocumentRepository dr;

    @RequestMapping(value = "/groupe", method = RequestMethod.GET)
    public Collection<Groupe> groupes(){
        return gr.findAll();
    }

    @RequestMapping(value = "/groupe/{id}", method = RequestMethod.GET)
    public Groupe groupById(@PathVariable int id){
        return gr.findOne(id);
    }

    @RequestMapping(value = "/groupe/{id}", method = RequestMethod.DELETE)
    public void deletegroupeById(@PathVariable int id){
         gr.delete(id);
    }
    @RequestMapping(value = "/groupe", method = RequestMethod.POST)
    public Groupe addGroupe(@RequestBody Groupe groupe){
        return gr.save(groupe);
    }

    @RequestMapping(value = "/groupe/document/{id}")
    public int listDocumentInGroup(@PathVariable int id){
//        return dr.findByGroupId(id).size();
        return 0;
    }




    @RequestMapping(value = "/categorie", method = RequestMethod.GET)
    public Collection<Categorie> categories(){
        return cr.findAll();
    }

    @RequestMapping(value = "/categorie/{id}", method = RequestMethod.GET)
    public Categorie categorieById(@PathVariable int id){
        return cr.findOne(id);
    }
    @RequestMapping(value = "/categorie/{id}", method = RequestMethod.DELETE)
    public void deleteCategorieById(@PathVariable int id){
        cr.delete(id);
    }

    @RequestMapping(value = "/categorie", method = RequestMethod.POST)
    public Categorie addCategorie(@RequestBody Categorie categorie){
        return cr.save(categorie);
    }

    @RequestMapping(value = "/categorie/document/{id}")
    public int listDocumentInCategories(@PathVariable int id){

        return dr.findByCategorieId(id).size();
    }
}
