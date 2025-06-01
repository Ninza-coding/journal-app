package com.techn.journalapp.entity;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
@Data
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    @NonNull
    private String userName;
    @NonNull
    private String password;
}
