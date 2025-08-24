package com.example.blog.service;
import com.example.blog.domain.Category;
import com.example.blog.repository.CategoryRepository;
import com.example.blog.web.exception.BusinessException;
import com.example.blog.web.exception.NotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.text.Normalizer;
import java.util.List;
import java.util.Locale;


@Service
public class CategoryService {
    private final CategoryRepository categoryRepo;
    public CategoryService(CategoryRepository categoryRepo){ this.categoryRepo = categoryRepo; }


    @Transactional(readOnly = true)
    public List<Category> findAll(){ return categoryRepo.findAll(); }


    @Transactional(readOnly = true)
    public Category findById(Long id){
        return categoryRepo.findById(id).orElseThrow(() -> new NotFoundException("Categoria não encontrada"));
    }


    @Transactional(readOnly = true)
    public Category findBySlug(String slug){
        return categoryRepo.findBySlug(slug).orElseThrow(() -> new NotFoundException("Categoria não encontrada"));
    }


    @Transactional
    public Category create(Category category){
        // Se slug não vier, gerar a partir do nome
        if (category.getSlug() == null || category.getSlug().isBlank()) {
            category.setSlug(slugify(category.getName()));
        } else {
            category.setSlug(slugify(category.getSlug()));
        }

        // Verifica unicidade do slug
        categoryRepo.findBySlug(category.getSlug()).ifPresent(c -> { throw new BusinessException("Slug de categoria já em uso"); });
        try {
            return categoryRepo.save(category);
        } catch (DataIntegrityViolationException e){
            throw new BusinessException("Não foi possível salvar a categoria (conflito de chave única ou dados inválidos)");
        }
    }

    @Transactional
    public Category update(Long id, Category category){
        Category c = findById(id);
        c.setName(category.getName());
        // Atualiza - normaliza slug
        String newSlug = category.getSlug();
        if (newSlug == null || newSlug.isBlank()) newSlug = slugify(category.getName());
        newSlug = slugify(newSlug);

        // Garante que o slug não colida com outra categoria
        categoryRepo.findBySlug(newSlug).ifPresent(existing -> {
            if (!existing.getId().equals(c.getId())) {
                throw new BusinessException("Slug de categoria já em uso");
            }
        });

        c.setSlug(newSlug);
        try {
            return categoryRepo.save(c);
        } catch (DataIntegrityViolationException e){
            throw new BusinessException("Não foi possível atualizar a categoria");
        }
    }

    @Transactional
    public void delete(Long id){
        Category c = findById(id);
        categoryRepo.delete(c);
    }

    private String slugify(String input){
        String noAccents = Normalizer.normalize(input == null ? "" : input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return noAccents.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+","-")
                .replaceAll("(^-|-$)", "");
    }
}
