package com.mawadda.service.impl;

import com.mawadda.entity.MedicalService;
import com.mawadda.entity.User;
import com.mawadda.exception.ConflictException;
import com.mawadda.exception.ResourceNotFoundException;
import com.mawadda.repository.MedicalServiceRepository;
import com.mawadda.repository.UserRepository;
import com.mawadda.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements IUserService, UserDetailsService {

    private final UserRepository userRepository;
    private final MedicalServiceRepository serviceRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User add(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ConflictException("User with email " + user.getEmail() + " already exists");
        }

        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        if (user.getService() != null && user.getService().getId() != null) {
            MedicalService service = serviceRepository.findById(user.getService().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Medical Service not found with ID: " + user.getService().getId()));
            user.setService(service);
        }

        return userRepository.save(user);
    }

    @Override
    public User modify(Integer id, User user) {
        User existing = show(id);
        existing.setFirstName(user.getFirstName());
        existing.setLastName(user.getLastName());
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (user.getRole() != null) {
            existing.setRole(user.getRole());
        }
        if (user.getService() != null && user.getService().getId() != null) {
            MedicalService service = serviceRepository.findById(user.getService().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Medical Service not found with ID: " + user.getService().getId()));
            existing.setService(service);
        }
        return userRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public User show(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public User showByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> showAll() {
        return userRepository.findAll();
    }

    @Override
    public void delete(Integer id) {
        User user = show(id);
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
}
