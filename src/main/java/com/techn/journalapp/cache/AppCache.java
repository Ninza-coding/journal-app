package com.techn.journalapp.cache;


import com.techn.journalapp.entity.ConfigJournalAppEntity;
import com.techn.journalapp.repository.ConfigJournalAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AppCache {

    public enum keys{
        WEATHER_API
    }
    public Map<String, String> APP_CACHE;

    @Autowired
    private ConfigJournalAppRepository configJournalAppRepository;

    @PostConstruct
    public void init(){
        APP_CACHE=new HashMap<>();
        APP_CACHE=configJournalAppRepository.findAll().stream()
                .collect(Collectors.toMap(
                        ConfigJournalAppEntity::getKey,
                        ConfigJournalAppEntity::getValue
                ));
        System.out.println(APP_CACHE);

//        List<ConfigJournalAppEntity> all = configJournalAppRepository.findAll();
//        for(ConfigJournalAppEntity c:all){
//            APP_CACHE.put(c.getKey(),c.getValue());
//        }
    }
}
