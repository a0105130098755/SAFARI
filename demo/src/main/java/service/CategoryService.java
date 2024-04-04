package service;

public class CategoryService {
    private CategoryRepository categoryRepository;

    // 의존성 주입을 위한 생성자
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category getCategoryByName(String name) {
        Category category = categoryRepository.findByName(name);
        if (category == null) {
            throw new EntityNotFoundException("Category not found with name: " + name);
        }
        return category;
    }
}
