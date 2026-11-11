package com.mawadda.service.impl;

import com.mawadda.entity.Parent;
import com.mawadda.exception.ConflictException;
import com.mawadda.exception.ResourceNotFoundException;
import com.mawadda.repository.ParentRepository;
import com.mawadda.service.IParentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ParentServiceImpl implements IParentService {

    private final ParentRepository parentRepository;

    @Override
    public Parent add(Parent parent) {
        if (parentRepository.existsByCin(parent.getCin())) {
            throw new ConflictException("Parent with CIN " + parent.getCin() + " already exists");
        }
        return parentRepository.save(parent);
    }

    @Override
    public Parent modify(Integer id, Parent parent) {
        Parent existing = show(id);
        existing.setFirstName(parent.getFirstName());
        existing.setLastName(parent.getLastName());
        existing.setPhone(parent.getPhone());
        return parentRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public Parent show(Integer id) {
        return parentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parent not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Parent showByCin(String cin) {
        return parentRepository.findByCin(cin)
                .orElseThrow(() -> new ResourceNotFoundException("Parent not found with CIN: " + cin));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Parent> showAll() {
        return parentRepository.findAll();
    }

    @Override
    public void delete(Integer id) {
        Parent parent = show(id);
        parentRepository.delete(parent);
    }
}
