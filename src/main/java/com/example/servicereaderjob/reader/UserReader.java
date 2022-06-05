package com.example.servicereaderjob.reader;

import com.example.servicereaderjob.domain.ResponseUser;
import com.example.servicereaderjob.domain.User;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

@Component
public class UserReader implements ItemReader<User> {

    private RestTemplate restTemplate = new RestTemplate();
    private int page = 1;
    private List<User> users = new ArrayList<>();
    private int userIndex = 0;
    private int total = 0;

    @Value("${chunkSize}")
    private int chunkSize;

    @Value("${pageSize}")
    private int pageSize;

    @Value("${limit}")
    private int limit;

    @Override
    public User read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (!(userIndex < users.size())) {
            return null;
        }
        final var user = users.get(userIndex);
        userIndex++;
        return user;
    }

    private List<User> fetchUserDataFromAPI() {
        final var url = format("https://gorest.co.in/public/v1/users?page=%d", page);
        final var response = restTemplate.getForEntity(url, ResponseUser.class);
        return response.getBody().getData();
    }

    @BeforeChunk
    private void beforeChunck(ChunkContext context) {
        for(int i = 0; i < chunkSize; i += pageSize){
            if(total >= limit) return;
            users.addAll(fetchUserDataFromAPI());
            total += pageSize;
            page++;
        }
    }

    @AfterChunk
    private void afterChunck(ChunkContext context) {
        System.out.println("Fim do Chunck");
        System.out.println(userIndex);
        userIndex = 0;
        users = new ArrayList<>();
    }

}
