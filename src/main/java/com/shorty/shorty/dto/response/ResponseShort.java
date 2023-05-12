package com.shorty.shorty.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shorty.shorty.repository.UrlRepository;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ResponseShort {
    private String shortUrl;
    private String description = "Error occurred! Please try again.";

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String generateShortUrlId(String lastShortUrlId, UrlRepository urlRepository) {
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

        if (urlRepository.shortUrlIdExistsInDatabase(newShortUrlId)) {
            return this.generateShortUrlId(newShortUrlId, urlRepository);
        }
        //if shortUrlId exceed DB limit (8 characters) -> start from beginning
        if (newShortUrlId.length() > 8) {
            newShortUrlId = "abcde";
            if (urlRepository.shortUrlIdExistsInDatabase(newShortUrlId)) {
                if (urlRepository.deleteUrlByShortUrlId(newShortUrlId) == 0) {
                    this.description = "Error occurred while accessing the database. Please try again.";
                    return null;
                }
            }
        }

        return newShortUrlId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
