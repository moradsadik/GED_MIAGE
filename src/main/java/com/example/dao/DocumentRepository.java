package com.example.dao;

import com.example.entity.Document;
import com.example.entity.Groupe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer>{

    public Collection<Document> findByCategorieId(int id);
}
