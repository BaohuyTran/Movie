/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bht.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

/**
 *
 * @author User
 */
@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRespository;
    @Autowired
    private MongoTemplate mongoTemplate;
    
    public Review createReview(String reviewBody, String imdbId) {
        Review review = reviewRespository.insert(new Review(reviewBody));
        
        // Update the movie class where the imdbId in the db is match with the imdbId we received from the user
        // then apply this new change (push to the reviewIds)
        mongoTemplate.update(Movie.class)
                .matching(Criteria.where("imdbId").is(imdbId))
                .apply(new Update().push("reviewIds").value(review))
                .first();
        
        return review;
    }
}
