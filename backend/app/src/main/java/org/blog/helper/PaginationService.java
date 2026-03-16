package org.blog.helper;

import org.blog.model.Pagination;

import java.util.ArrayList;
import java.util.List;

public class PaginationService <T>{
    private final List<T> posts;
    private final int pageNumber;  // примитив вместо Integer
    private final int pageSize;

    public PaginationService(List<T> posts, int pageNumber, int pageSize) {
        this.posts = posts;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    private int calculateTotalPages(int totalElements) {
        if (totalElements == 0) return 1;
        return (int) Math.ceil((double) totalElements / pageSize);
    }

    public Pagination<T> getPagination() {
        int totalElements = posts.size();
        int totalPages = calculateTotalPages(totalElements);

        int currentPage = Math.min(pageNumber, totalPages > 0 ? totalPages : 1);

        boolean hasPrev = currentPage > 1;
        boolean hasNext = currentPage < totalPages;

        int startIndex = (currentPage - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalElements);

        List<T> currentPageItems = startIndex < totalElements ?
                posts.subList(startIndex, endIndex) :
                new ArrayList<T>();

        return new Pagination<T>(currentPageItems, hasPrev, hasNext, totalPages);
    }
}
