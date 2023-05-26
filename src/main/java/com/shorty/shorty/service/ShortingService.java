package com.shorty.shorty.service;

import com.shorty.shorty.dto.request.RequestShort;
import com.shorty.shorty.dto.response.ResponseShort;
import com.shorty.shorty.entity.Url;
import com.shorty.shorty.entity.User;
import com.shorty.shorty.repository.UrlRepository;
import com.shorty.shorty.repository.UserRepository;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class ShortingService {

    public ResponseShort shorting(RequestShort requestShort, UrlRepository urlRepository, String authToken, UserRepository userRepository) {
        ResponseShort responseShort = new ResponseShort();

        //CHECK AUTHORIZATION TOKEN
        Pair<String, User> returnPair = checkAuthToken(authToken, userRepository);
        String errorMessage = returnPair.getFirst();
        User user = returnPair.getSecond();

        if (!errorMessage.isEmpty()) {
            responseShort.setDescription(errorMessage);
            return responseShort;
        }

        //CHECK IF THE REQUEST IS VALID
        errorMessage = findRequestError(requestShort);
        if (errorMessage != null) {
            responseShort.setDescription(errorMessage);
            return responseShort;
        }

        //GENERATE SHORT URL
        return generateShortUrl(requestShort, responseShort, user, urlRepository);
    }


    private Pair<String, User> checkAuthToken(String authToken, UserRepository userRepository) {
        User user = new User();

        if (authToken == null) {
            return Pair.of("Access denied! Please log in.", user);
        }

        String basicToken = authToken.substring(5).trim();
        byte[] decodedCredentials = Base64.getDecoder().decode(basicToken);
        String[] authPair = (new String(decodedCredentials, StandardCharsets.UTF_8)).split(":", 2);
        final String username = authPair[0];
        final String password = authPair[1];

        if (username.isBlank() && password.isBlank()) {
            return Pair.of("Please enter your username and password.", user);
        }
        if (username.isBlank()) {
            return Pair.of("Please enter your username.", user);
        }
        if (password.isBlank()) {
            return Pair.of("Please enter your password.", user);
        }

        user = userRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            user = new User();
            return Pair.of("Access denied! Wrong username and/or password.", user);
        }

        return Pair.of("", user);
    }

    private String findRequestError(RequestShort requestShort) {
        String errorMessage;

        //is request empty
        if (requestShort.isEmpty()) {
            return "Error occurred! Please try again.";
        }
        //is url sent
        if (requestShort.urlIsBlank()) {
            return "Please enter your URL!";
        }
        //checking if entered URL is valid
        else if ((errorMessage = requestShort.EnteredUrlIsNotValid()) != null) {
            return errorMessage;
        }

        return null;
    }

    private ResponseShort generateShortUrl(RequestShort requestShort, ResponseShort responseShort, User user, UrlRepository urlRepository) {
        Url lastUrl = urlRepository.findFirstByOrderByUrlIdDesc();
        String lastShortUrlId = null;
        if (lastUrl != null) {
            lastShortUrlId = lastUrl.getShortUrlId();
        }

        String shortUrlId = responseShort.generateShortUrlId(lastShortUrlId, urlRepository);
        if (shortUrlId != null) {
            Url url = new Url(requestShort.getUrl(), shortUrlId, requestShort.getRedirectType(), user.getUser_id());
            urlRepository.save(url);
            responseShort.setShortUrl(url.getShortUrlId());
            responseShort.setDescription(null);
        }

        return responseShort;
    }
}
