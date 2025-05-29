package com.innogrid.controller;

import com.innogrid.model.Document;
import com.innogrid.service.DocumentService;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@AllArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @MutationMapping("document")
    public Document createDocument(@Argument String title, @Argument String description ) {
        return documentService.create(new Document()
                .setTitle(title)
                .setDescription(description)
                .setCreatedDate(LocalDateTime.now()));
    }


    @QueryMapping("document")
    public Document getDocument(@Argument Integer id) {
       return documentService.getDocument(id);
    }
}
