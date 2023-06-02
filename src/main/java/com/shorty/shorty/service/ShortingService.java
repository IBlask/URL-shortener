package com.shorty.shorty.service;

import com.shorty.shorty.dto.response.ResponseStatistics;
import com.shorty.shorty.entity.Url;
import com.shorty.shorty.entity.User;
import com.shorty.shorty.repository.UrlRepository;
import com.shorty.shorty.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class ShortingService {

    public List<ResponseStatistics> getStatistics(String authToken, UserRepository userRepository, UrlRepository urlRepository) {
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
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (user == null || !bCryptPasswordEncoder.matches(password, user.getPassword())) {
            return null;
        }

        //GENERATING RESPONSE
        List<ResponseStatistics> returnList = new ArrayList<>();
        List<Url> listOfUrls = urlRepository.findAllByUserId(user.getUser_id());

        for (Url url : listOfUrls) {
            ResponseStatistics responseStatistics = new ResponseStatistics(url.getFullUrl(), url.getShortUrl(), url.getRedirects());
            returnList.add(responseStatistics);
        }

        return returnList;
    }

}
