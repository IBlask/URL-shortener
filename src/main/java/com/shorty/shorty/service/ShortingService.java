package com.shorty.shorty.service;

import com.shorty.shorty.ShortyApplication;
import com.shorty.shorty.entity.Url;
import com.shorty.shorty.entity.User;
import com.shorty.shorty.repository.UrlRepository;
import com.shorty.shorty.repository.UserRepository;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class ShortingService {

    public LinkedHashMap<String, Pair<String, Integer>> getStatistics(String authToken, UserRepository userRepository, UrlRepository urlRepository) {
        //CHECKING USERNAME AND PASSWORD
        if (authToken == null) {
            return null;
        }

        String basicToken = authToken.substring(5).trim();
        byte[] decodedCredentials = Base64.getDecoder().decode(basicToken);
        String[] authPair = (new String(decodedCredentials, StandardCharsets.UTF_8)).split(":", 2);
        final String username = authPair[0];
        final String password = authPair[1];

        if (username.isBlank() || password.isBlank()) {
            return null;
        }

        User user = userRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            return null;
        }

        //GENERATING RESPONSE
        LinkedHashMap<String, Pair<String, Integer>> responseMap = new LinkedHashMap<>();
        List<Url> listOfUrls = urlRepository.selectStatistics(user.getUser_id());

        for (Url url : listOfUrls) {
            String shortUrl = ShortyApplication.getAddress() + url.getShortUrlId();
            Pair<String, Integer> pair = Pair.of(shortUrl, url.getRedirects());
            responseMap.put(url.getFullUrl(), pair);
        }

        return responseMap;
    }

}
