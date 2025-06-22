package com.techn.journalapp.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "config_journal_app")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfigJournalAppEntity {


    private String key;
    private String value;
}
