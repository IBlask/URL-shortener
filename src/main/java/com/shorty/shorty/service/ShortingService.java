package com.shorty.shorty.service;

import com.shorty.shorty.entity.Url;
import com.shorty.shorty.entity.User;
import com.shorty.shorty.repository.UrlRepository;
import com.shorty.shorty.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShortingService {

    public Map<String, Integer> getStatistics(String authToken, UserRepository userRepository, UrlRepository urlRepository) {
        //CHECKING USERNAME AND PASSWORD
        if (authToken == null) {
            return null;
        }

        String basicToken = authToken.substring(5).trim();
        byte[] decodedCredentials = Base64.getDecoder().decode(basicToken);
        String[] authPair = (new String(decodedCredentials, StandardCharsets.UTF_8)).split(":", 2);
        final String username = authPair[0];
        final String password = authPair[1];

        if (username.isBlank() && password.isBlank()) {
            return null;
        }

        User user = userRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            return null;
        }

        //GENERATING RESPONSE
        Map<String, Integer> responseMap = new HashMap<>();
        List<Url> listOfUrls = urlRepository.selectStatistics(user.getUser_id());

        for (Url url : listOfUrls) {
            responseMap.put(url.getFullUrl(), url.getRedirects());
        }

        return responseMap;
    }

}
