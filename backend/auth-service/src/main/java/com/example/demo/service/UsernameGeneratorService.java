package com.example.demo.service;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repository.UserRepository;

@Service
public class UsernameGeneratorService {

    private static final Logger logger = LoggerFactory.getLogger(UsernameGeneratorService.class);

    private final String[] ADJECTIVES = {
            "Happy", "Curious", "Brave", "Smart", "Creative", "Bold", "Wise",
            "Gentle", "Quick", "Strong", "Clever", "Mighty", "Swift", "Eager",
            "Bright", "Calm", "Dazzling", "Eager", "Fancy", "Glamorous", "Jolly",
            "Lively", "Nice", "Obedient", "Polite", "Proud", "Silly", "Thankful",
            "Victorious", "Witty", "Zealous", "Ancient", "Brief", "Clean", "Deep",
            "Fast", "Giant", "Heavy", "Late", "Loud", "Modern", "Odd", "Rapid",
            "Sharp", "Short", "Small", "Soft", "Tall", "Thin", "Warm", "Young",
            "Chill", "Cool", "Epic", "Grand", "Lucky", "Magic", "Prime", "Rare",
            "Sunny", "Urban", "Vivid", "Wild"
    };

    private final String[] ANIMALS = {
            "Panda", "Tiger", "Lion", "Eagle", "Dolphin", "Fox", "Wolf",
            "Bear", "Hawk", "Owl", "Elephant", "Leopard", "Falcon", "Shark",
            "Badger", "Beaver", "Bison", "Camel", "Cat", "Cheetah", "Cobra",
            "Cougar", "Crane", "Crow", "Deer", "Dog", "Dove", "Duck", "Elk",
            "Frog", "Gazelle", "Giraffe", "Goat", "Goose", "Gorilla", "Heron",
            "Horse", "Hyena", "Jaguar", "Koala", "Lemur", "Llama", "Lynx",
            "Monkey", "Mouse", "Otter", "Parrot", "Penguin", "Pigeon", "Rabbit",
            "Ram", "Rat", "Raven", "Rhino", "Salmon", "Seal", "Snake", "Spider",
            "Stork", "Swan", "Toad", "Turkey", "Turtle", "Whale", "Zebra"
    };

    @Autowired
    private UserRepository userRepository;

    public String generateUniqueUsername() {
        String username;
        int attempts = 0;
        do {
            String adjective = ADJECTIVES[new Random().nextInt(ADJECTIVES.length)];
            String animal = ANIMALS[new Random().nextInt(ANIMALS.length)];
            int number = new Random().nextInt(10000); // 0-9999 for larger uniqueness

            username = adjective + animal + number;
            attempts++;

            if (attempts > 10) {
                logger.warn("Multiple attempts to generate unique username: {}", attempts);
            }
        } while (userRepository.findByUsername(username).isPresent());

        logger.debug("Generated unique username: {} after {} attempts", username, attempts);
        return username;
    }
}
