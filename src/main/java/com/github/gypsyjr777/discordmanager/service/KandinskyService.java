package com.github.gypsyjr777.discordmanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gypsyjr777.discordmanager.model.KandinskyBody;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class KandinskyService {
    @Value("${segmindApiToken}")
    private String token;

    public InputStream generateImage(KandinskyBody kandinsky) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        ObjectMapper mapper = new ObjectMapper();
        RequestBody body = RequestBody.create(mediaType, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(kandinsky));
        Request request = new Request.Builder()
                .url("https://api.segmind.com/v1/kandinsky2.2-txt2img")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .addHeader("x-api-key", token)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException(response.message());
        return response.body().source().inputStream();
    }
}
