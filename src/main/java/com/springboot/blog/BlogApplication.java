package com.springboot.blog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.blog.entities.Category;
import com.springboot.blog.entities.Comment;
import com.springboot.blog.entities.Post;
import com.springboot.blog.entities.User;
import com.springboot.blog.repositories.CategoryRepository;
import com.springboot.blog.repositories.CommentRepository;
import com.springboot.blog.repositories.PostRepository;
import com.springboot.blog.repositories.UserRepository;
import com.springboot.blog.security.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
@Slf4j
public class BlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
		log.info("Blog Application started running...");
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	@Bean
	CommandLineRunner commandLineRunner(UserRepository userRepository,
										PostRepository postRepository,
										CommentRepository commentRepository,
										CategoryRepository categoryRepository) {
		return args -> {
			PasswordEncoder passEncoder = new BCryptPasswordEncoder();

			User johnDoe = new User("john.doe", "john.doe@gmail.com", User.Gender.MALE, LocalDate.of(1990, 5, 15), passEncoder.encode("john123"), UserRole.USER, LocalDateTime.now());
			User sarahLee = new User("sarah.lee", "sarah.lee@gmail.com", User.Gender.FEMALE, LocalDate.of(1995, 2, 28), passEncoder.encode("sarah123"), UserRole.USER, LocalDateTime.now());
			User chrisBrown = new User("chris.brown", "chris.brown@gmail.com", User.Gender.MALE, LocalDate.of(1985, 7, 20), passEncoder.encode("chris123"), UserRole.AUTHOR, LocalDateTime.now());
			User bobSmith = new User("bob.smith", "bob.smith@gmail.com", User.Gender.MALE, LocalDate.of(1975, 12, 10), passEncoder.encode("bob123"), UserRole.ADMIN, LocalDateTime.now());
			List<User> users = Arrays.asList(johnDoe, sarahLee, chrisBrown, bobSmith);
			userRepository.saveAll(users);

			Category foodCategory = new Category("Food", "Recipes and cooking tips", LocalDateTime.now());
			Category fitnessCategory = new Category("Fitness", "Workouts and health tips", LocalDateTime.now());
			List<Category> categories = Arrays.asList(foodCategory, fitnessCategory);
			categoryRepository.saveAll(categories);

			List<Post> posts = Arrays.asList(
					new Post("How to make a perfect omelette", "A guide to making the perfect omelette every time", "This guide will show you how to make a perfect omelette every time. You'll need eggs, milk, butter, salt, pepper, and your choice of fillings, such as cheese, ham, or vegetables. Start by whisking together the eggs, milk, salt, and pepper in a bowl. Heat butter in a pan over medium heat, then pour in the egg mixture. Add your chosen fillings to one half of the omelette and use a spatula to fold the other half over the fillings. Cook until the omelette is set, then slide it onto a plate and serve.", LocalDateTime.of(2022, 2, 3, 14, 45)),
					new Post("Easy homemade pizza recipe", "Make your own delicious pizza from scratch", "Make your own delicious pizza from scratch with this easy recipe. You'll need pizza dough, tomato sauce, cheese, and your choice of toppings, such as pepperoni, mushrooms, or onions. Preheat your oven to 425°F, then roll out the pizza dough on a floured surface. Spread tomato sauce on the dough and add cheese and toppings. Bake in the oven for 10-12 minutes or until the crust is golden brown. Serve hot and enjoy your homemade pizza!", LocalDateTime.of(2022, 3, 21, 8, 0)),
					new Post("Your Fitness Journey Starts Here", "Tips and tricks for getting started with your fitness journey", "Embark on your fitness journey with these useful tips. Whether you're a beginner or looking to level up your workouts, we've got you covered. Learn about effective workout routines, nutrition, and staying motivated on your path to a healthier lifestyle.", LocalDateTime.now()) // Fitness category post
			);
			postRepository.saveAll(posts);

			List<Comment> comments = Arrays.asList(
					// Comments for Omelette
					new Comment("I've tried this omelette recipe, and it's amazing!", LocalDateTime.of(2023, 1, 1, 12, 0), posts.get(0), users.get(0)),
					new Comment("What fillings do you recommend for the omelette?", LocalDateTime.of(2023, 1, 2, 15, 30), posts.get(0), users.get(1)),

					// Comments for Pizza
					new Comment("Homemade pizza is the best! I'm definitely trying this recipe.", LocalDateTime.of(2023, 1, 5, 10, 15), posts.get(1), users.get(2)),
					new Comment("Any tips for getting a crispy crust on the pizza?", LocalDateTime.of(2023, 1, 6, 14, 0), posts.get(1), users.get(0)),

					// Comments for Fitness Journey
					new Comment("Excited to start my fitness journey with these tips!", LocalDateTime.of(2023, 1, 10, 8, 45), posts.get(2), users.get(1)),
					new Comment("Do you have recommendations for beginner-friendly workouts?", LocalDateTime.of(2023, 1, 11, 11, 20), posts.get(2), users.get(2))
			);
			commentRepository.saveAll(comments);
		};
	}
}
