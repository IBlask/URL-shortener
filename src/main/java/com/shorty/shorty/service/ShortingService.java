package com.shorty.shorty.service;

import com.shorty.shorty.dto.request.RequestShort;
import com.shorty.shorty.dto.response.ResponseShort;
import com.shorty.shorty.entity.Url;
import com.shorty.shorty.repository.UrlRepository;
import org.springframework.stereotype.Service;

@Service
public class ShortingService {

    public ResponseShort shorting(RequestShort requestShort, UrlRepository urlRepository) {
        ResponseShort responseShort = new ResponseShort();

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
                Url url = new Url(requestShort.getUrl(), shortUrlId, requestShort.getRedirectType());
                urlRepository.save(url);
                responseShort.setShortUrl(url.getShortUrl());
                responseShort.setDescription(null);
            }
        }

        return responseShort;
    }

}
