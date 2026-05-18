package com.vietlocal.app.service;

import com.vietlocal.app.dto.response.BlogPostResponse;
import com.vietlocal.app.dto.response.PageResponse;
import com.vietlocal.app.exception.ResourceNotFoundException;
import com.vietlocal.app.repository.BlogPostRepository;
import com.vietlocal.app.utils.EntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlogService {

	private final BlogPostRepository blogPostRepository;

	@Transactional(readOnly = true)
	public PageResponse<BlogPostResponse> findAll(Pageable pageable) {
		Page<BlogPostResponse> page = blogPostRepository.findAll(pageable)
				.map(EntityMapper::toBlogPostResponse);
		return PageResponse.from(page);
	}

	@Transactional(readOnly = true)
	public BlogPostResponse findBySlug(String slug) {
		return blogPostRepository.findBySlugWithDestination(slug)
				.map(EntityMapper::toBlogPostResponse)
				.orElseThrow(() -> new ResourceNotFoundException("Blog not found: " + slug));
	}
}
