package com.example.service;

import com.example.DemoApplication;
import com.example.dao.CategorieRepository;
import com.example.dao.TagRepository;
import com.example.dao.DocumentRepository;
import com.example.dao.GroupeRepository;
import com.example.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class DocumentRestService {

    @Autowired
    private DocumentRepository dr;
    @Autowired
    private GroupeRepository gr;
    @Autowired
    private CategorieRepository cr;
    @Autowired
    private TagRepository clr;


    @RequestMapping(value = "/document", method = RequestMethod.GET)
    public List<Document> documents() {
        return dr.findAll();
    }

    @RequestMapping(value = "/document/{id}", method = RequestMethod.GET)
    public Document oneDocument(@PathVariable int id) {

        return dr.findOne(id);
    }

    @RequestMapping(value = "/document/{id}", method = RequestMethod.DELETE)
    public ResponseEntity documents(@PathVariable int id) {
        Document document = dr.findOne(id);
        File file = new File(document.getPath());
        file.delete();
        dr.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity uploadFile(@RequestParam("files") MultipartFile file,
                                     @RequestParam("name") String name,
                                     @RequestParam("description") String description,
                                     @RequestParam("group") List<Integer> groupes,
                                     @RequestParam("cles") List<String> cles,
                                     @RequestParam("categorie") int categorie
    ) {
        Document doc = null;
        try {
            if (file != null) {

                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(DemoApplication.UPLOADS_DIR + "/" + file.getOriginalFilename())));
                FileCopyUtils.copy(file.getInputStream(), bos);
                bos.close();
                String path = DemoApplication.UPLOADS_DIR + "/" + file.getOriginalFilename();
                doc = new Document(name, description, file.getContentType(), file.getSize(), file.getBytes(), path);

                Categorie cat = cr.findOne(categorie);
                doc.setCategorie(cat);

                Set<Groupe> g = new HashSet<>();
                for (int id : groupes) {
                    Groupe gp = gr.findOne(id);
                    g.add(gp);
                }
                doc.setGroupes(g);

                dr.save(doc);

                Set<Tag> c = new HashSet<>();
                for (String cle : cles) {
                    Tag cl = new Tag(cle);
                    cl.setDocument(doc);
                    clr.save(cl);
                    c.add(cl);
                }
                doc.setCles(c);

                bos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(doc, HttpStatus.OK);
    }

    @RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
    public void download(@PathVariable int id, final HttpServletResponse response) {
        Document document = dr.findOne(id);
        System.out.println(document);

        try {
            File file = new File(document.getPath());
            InputStream in = new FileInputStream(file);
            OutputStream out = response.getOutputStream();

            response.setContentType(document.getType());
            response.setContentLength((int) document.getSize());
            response.setHeader("Content-Disposition", "attachment;filename=\"" + file.getName() + "\"");

            FileCopyUtils.copy(in, out);

            out.flush();
            in.close();
            out.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
