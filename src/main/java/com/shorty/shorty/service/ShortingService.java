package com.shorty.shorty.service;

import com.shorty.shorty.dto.request.RequestShort;
import com.shorty.shorty.dto.response.ResponseShort;
import com.shorty.shorty.entity.Url;
import com.shorty.shorty.entity.User;
import com.shorty.shorty.repository.UrlRepository;
import com.shorty.shorty.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class ShortingService {

    public ResponseShort shorting(RequestShort requestShort, UrlRepository urlRepository, String authToken, UserRepository userRepository) {
        ResponseShort responseShort = new ResponseShort();

        //CHECKING USERNAME AND PASSWORD
        if (authToken == null) {
            responseShort.setDescription("Access denied! Please log in.");
            return responseShort;
        }

        String basicToken = authToken.substring(5).trim();
        byte[] decodedCredentials = Base64.getDecoder().decode(basicToken);
        String[] authPair = (new String(decodedCredentials, StandardCharsets.UTF_8)).split(":", 2);
        final String username = authPair[0];
        final String password = authPair[1];

        if (username.isBlank() && password.isBlank()) {
            responseShort.setDescription("Please enter your username and password.");
            return responseShort;
        }
        else if (username.isBlank()) {
            responseShort.setDescription("Please enter your username.");
            return responseShort;
        }
        else if (password.isBlank()) {
            responseShort.setDescription("Please enter your password.");
            return responseShort;
        }

        User user = userRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            responseShort.setDescription("Access denied! Wrong username and/or password.");
            return responseShort;
        }

        //SHORTING
        //is request empty
        if (requestShort.isEmpty()) {
            return responseShort;
        }
        //is url sent
        if (requestShort.urlIsBlank()) {
            responseShort.setDescription("Please enter your URL!");
        }
        //generating short URL
        else {
            String lastShortUrlId = urlRepository.findLastShortUrlId();;
            String shortUrlId = responseShort.generateShortUrlId(lastShortUrlId, urlRepository);
            if (shortUrlId != null) {
                Url url = new Url(requestShort.getUrl(), shortUrlId, requestShort.getRedirectType(), user.getUser_id());
                urlRepository.save(url);
                responseShort.setShortUrl(url.getShortUrlId());
                responseShort.setDescription(null);
            }
        }

        return responseShort;
    }

}
