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

        //CHECK IF ENTERED URL IS ALREADY SHORTENED
        Url url = urlRepository.findByFullUrl(requestShort.getUrl());
        if (url != null) {
            responseShort.setShortUrl(url.getShortUrlId());
            responseShort.setDescription(null);
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

        //Pair<newShortUrlId, errorMessage>
        Pair<String, String> returnPair = generateShortUrlId(lastShortUrlId, urlRepository);
        String shortUrlId = returnPair.getFirst();
        String errorMessage = returnPair.getSecond();

        if (!errorMessage.isEmpty()) {
            responseShort.setDescription(errorMessage);
            return responseShort;
        }

        if (!shortUrlId.isEmpty()) {
            Url url = new Url(requestShort.getUrl(), shortUrlId, requestShort.getRedirectType(), user.getUser_id());
            urlRepository.save(url);
            responseShort.setShortUrl(url.getShortUrlId());
            responseShort.setDescription(null);
        }

        return responseShort;
    }



    public Pair<String, String> generateShortUrlId(String lastShortUrlId, UrlRepository urlRepository) {
        String newShortUrlId = incrementLastShortUrlId(lastShortUrlId);

        //if shortUrlId exceed DB limit (8 characters) -> start from beginning
        if (newShortUrlId.length() > 8) {
            newShortUrlId = "abcde";
        }

        //if shortUrlId exists in the database -> replace it with the new url
        if (shortUrlIdExistsInDB(newShortUrlId, urlRepository)) {
            String errorMessage = deleteExistingUrlFromDB(newShortUrlId, urlRepository);
            if (errorMessage != null) {
                return Pair.of("", errorMessage);
            }
        }

        return Pair.of(newShortUrlId, "");
    }

    private String incrementLastShortUrlId(String lastShortUrlId) {
        String newShortUrlId = "abcde";

        if (lastShortUrlId != null) {
            char[] l = lastShortUrlId.toCharArray();
            if (l[l.length-1] == 'z') {
                int len = l.length - 1;
                while (len > 0) {
                    if (l[len-1] != 'z') {
                        l[len-1]++;
                        for (int i = len; i < l.length; i++) {
                            l[i] = 'a';
                        }
                        break;
                    }
                    len--;
                }
                newShortUrlId = String.valueOf(l);
                //if all characters are 'z' -> append 'a' on default ShortUrlId
                if (len == 0) {
                    newShortUrlId = "abcde";
                    for (int i = 0; i <= l.length - 5; i++) {
                        newShortUrlId += "a";
                    }
                }
            }
            else {
                l[l.length-1]++;
                newShortUrlId = String.valueOf(l);
            }
        }

        return newShortUrlId;
    }

    private boolean shortUrlIdExistsInDB(String shortUrlId, UrlRepository urlRepository) {
        Url url = urlRepository.findByShortUrlId(shortUrlId);
        return url != null;
    }

    private String deleteExistingUrlFromDB(String shortUrlId, UrlRepository urlRepository) {
        if (urlRepository.findByShortUrlId(shortUrlId) != null) {
            if (urlRepository.deleteByShortUrlId(shortUrlId) == 0) {
                return "Error occurred while accessing the database. Please try again.";
            }
        }
        return null;
    }
}
