package com.leoni.controllers;

import com.leoni.security.services.DesignService;
import com.leoni.models.Design;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/designs")
public class DesignController {

    @Autowired
    private DesignService designService;

    @GetMapping("/show")
    public ResponseEntity<List<Design>> getAllDesigns() {
        List<Design> designs = designService.getAllDesigns();
        return new ResponseEntity<>(designs, HttpStatus.OK);
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<Design> getDesignById(@PathVariable Long id) {
        Optional<Design> design = designService.getDesignById(id);
        return design.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/insert")
    public ResponseEntity<Design> createDesign(@RequestBody Design design) {
        Design createdDesign = designService.createDesign(design);
        return new ResponseEntity<>(createdDesign, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Design> updateDesign(@PathVariable Long id, @RequestBody Design newDesignData) {
        Design updatedDesign = designService.updateDesign(id, newDesignData);
        if (updatedDesign != null) {
            return new ResponseEntity<>(updatedDesign, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDesign(@PathVariable Long id) {
        designService.deleteDesign(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
