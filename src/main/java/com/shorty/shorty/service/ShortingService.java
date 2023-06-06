package com.shorty.shorty.service;

import com.shorty.shorty.dto.request.RequestShort;
import com.shorty.shorty.dto.response.ResponseShort;
import com.shorty.shorty.entity.Url;
import com.shorty.shorty.entity.User;
import com.shorty.shorty.repository.UrlRepository;
import com.shorty.shorty.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class ShortingService {

    @Value("${server.domain}")
    private String serverDomain;

    public ResponseShort shortenUrl(RequestShort requestShort, UrlRepository urlRepository, String authToken, UserRepository userRepository) {
        ResponseShort responseShort = new ResponseShort();

        //CHECK AUTHORIZATION TOKEN (return User if token is valid)
        User user = checkAuthToken(authToken, userRepository);

        //CHECK IF THE REQUEST IS VALID
        String errorMessage = findRequestError(requestShort);
        if (errorMessage != null) {
            responseShort.setDescription(errorMessage);
            return responseShort;
        }

        //CHECK IF ENTERED URL IS ALREADY SHORTENED
        Url url = urlRepository.findByFullUrlAndUserId(requestShort.getUrl(), user.getUser_id());
        if (url != null) {
            responseShort.setShortUrl(url.getShortUrl(), serverDomain);
            responseShort.setDescription(null);
            return responseShort;
        }

        //GENERATE SHORT URL
        return generateShortUrl(requestShort, responseShort, user, urlRepository);
    }



    private User checkAuthToken(String authToken, UserRepository userRepository) throws AccessDeniedException {
        if (authToken == null) {
            throw new AccessDeniedException("Access denied! Please log in.");
        }

        String basicToken = authToken.substring(5).trim();
        byte[] decodedCredentials = Base64.getDecoder().decode(basicToken);
        String[] authPair = (new String(decodedCredentials, StandardCharsets.UTF_8)).split(":", 2);
        final String username = authPair[0];
        final String password = authPair[1];

        if (username.isBlank() && password.isBlank()) {
            throw new AccessDeniedException("Please enter your username and password.");
        }
        if (username.isBlank()) {
            throw new AccessDeniedException("Please enter your username.");
        }
        if (password.isBlank()) {
            throw new AccessDeniedException("Please enter your password.");
        }

        User user = userRepository.findByUsername(username);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (user == null || !bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new AccessDeniedException("Access denied! Wrong username and/or password.");
        }

        return user;
    }

    private String findRequestError(RequestShort requestShort) {
        //is request empty
        if (requestShort.isEmpty()) {
            return "Error occurred! Please try again.";
        }
        //is url sent
        if (requestShort.urlIsBlank()) {
            return "Please enter your URL!";
        }
        //checking if entered URL is valid
        return requestShort.EnteredUrlIsNotValid();
    }

    private ResponseShort generateShortUrl(RequestShort requestShort, ResponseShort responseShort, User user, UrlRepository urlRepository) {
        Url lastUrl = urlRepository.findFirstByOrderByUrlIdDesc();
        String lastShortUrl = null;
        if (lastUrl != null) {
            lastShortUrl = lastUrl.getShortUrl();
        }

        //Pair<newShortUrl, errorMessage>
        Pair<String, String> returnPair = generateNewShortUrl(lastShortUrl, urlRepository);
        String shortUrl = returnPair.getFirst();
        String errorMessage = returnPair.getSecond();

        if (!errorMessage.isEmpty()) {
            responseShort.setDescription(errorMessage);
            return responseShort;
        }

        if (!shortUrl.isEmpty()) {
            Url url = new Url(requestShort.getUrl(), shortUrl, requestShort.getRedirectType(), user.getUser_id());
            urlRepository.save(url);
            responseShort.setShortUrl(url.getShortUrl(), serverDomain);
            responseShort.setDescription(null);
        }

        return responseShort;
    }



    public Pair<String, String> generateNewShortUrl(String lastShortUrl, UrlRepository urlRepository) {
        String newShortUrl = incrementLastShortUrl(lastShortUrl);

        //if shortUrl exceed DB limit (8 characters) -> start from beginning
        if (newShortUrl.length() > 8) {
            newShortUrl = "abcde";
        }

        //if shortUrl exists in the database -> replace it with the new url
        if (shortUrlExistsInDB(newShortUrl, urlRepository)) {
            String errorMessage = deleteExistingUrlFromDB(newShortUrl, urlRepository);
            if (errorMessage != null) {
                return Pair.of("", errorMessage);
            }
        }

        return Pair.of(newShortUrl, "");
    }

    private String incrementLastShortUrl(String lastShortUrl) {
        String newShortUrl = "abcde";

        if (lastShortUrl != null) {
            char[] l = lastShortUrl.toCharArray();
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
                newShortUrl = String.valueOf(l);
                //if all characters are 'z' -> append 'a' on default ShortUrl
                if (len == 0) {
                    newShortUrl = "abcde";
                    for (int i = 0; i <= l.length - 5; i++) {
                        newShortUrl += "a";
                    }
                }
            }
            else {
                l[l.length-1]++;
                newShortUrl = String.valueOf(l);
            }
        }

        return newShortUrl;
    }

    private boolean shortUrlExistsInDB(String shortUrl, UrlRepository urlRepository) {
        Url url = urlRepository.findByShortUrl(shortUrl);
        return url != null;
    }

    private String deleteExistingUrlFromDB(String shortUrl, UrlRepository urlRepository) {
        if (urlRepository.findByShortUrl(shortUrl) != null) {
            if (urlRepository.deleteByShortUrl(shortUrl) == 0) {
                return "Error occurred while accessing the database. Please try again.";
            }
        }
        return null;
    }
}
