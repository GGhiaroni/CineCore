package com.CineCore.mapper;

import com.CineCore.entity.Category;
import com.CineCore.request.CategoryRequest;
import com.CineCore.response.CategoryResponse;
import lombok.experimental.UtilityClass;

@UtilityClass //notação do Lombok para impedir que uma classe seja instanciada
public class CategoryMapper {

    public static Category toCategory(CategoryRequest categoryRequest){
        return Category
                .builder()
                .name(categoryRequest.name())
                .build();
    }

    public static CategoryResponse toCategoryResponse(Category category){
        return CategoryResponse
                .builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

}
