package com.bankstatementaggregator.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bankstatementaggregator.models.Company;
import com.bankstatementaggregator.models.User;
import com.bankstatementaggregator.repositories.CompanyRepository;
import com.bankstatementaggregator.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerUser(User user) {
        if (user.getCompany() == null || user.getCompany().getCompanyId() == null) {
            System.out.println("Company information is required");
            return "Company information is required";
        }

        // Check if the company exists
        Company company = companyRepository.findById(user.getCompany().getCompanyId()).orElse(null);

        if (company == null) {
            System.out.println("Company does not exist");
            return "Company does not exist";
        }

        // Check if the user already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            System.out.println("User with username " + user.getUsername() + " already exists");
            return "User with username " + user.getUsername() + " already exists";
        }

        user.setCompany(company);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        System.out.println("User registered successfully: " + user.getUsername());
        return "User registered successfully!";
    }

    public String loginUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            System.out.println("Login successful for user: " + username);
            return "Login successful!";
        } else {
            System.out.println("Invalid username or password for user: " + username);
            return "Invalid username or password.";
        }
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
