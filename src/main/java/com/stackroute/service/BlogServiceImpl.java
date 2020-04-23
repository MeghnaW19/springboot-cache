package com.stackroute.service;

import com.stackroute.domain.Blog;

import com.stackroute.repository.BlogRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@CacheConfig(cacheNames = "blog")
@Service
public class BlogServiceImpl implements BlogService {
    private BlogRepository blogRepository;
    public BlogServiceImpl(){}
    @Autowired
    public void setBlogRepository(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    public BlogServiceImpl(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }
    @Override
    @CacheEvict
    public Blog saveBlog(Blog blog) {
        Blog savedBlog = blogRepository.save(blog);
        return savedBlog;
    }

    @Cacheable
    @Override
    public List<Blog> getAllBlogs() {
        return (List<Blog>) blogRepository.findAll();
    }

    @Cacheable(key = "#id")
    @Override
    public Blog getBlogById(int id) {
        Blog retrievedBlog = null;
            retrievedBlog = blogRepository.findById(id).get();
        return retrievedBlog;
    }

    @CacheEvict(key = "#id")
    @Override
    public Blog deleteBlogById(int id) {

        Blog blog = null;
        Optional optional = blogRepository.findById(id);
        if (optional.isPresent()) {
            blogRepository.deleteById(id);
        }
        return blog;
    }

    @CachePut(key = "#blog.blogId")
    public Blog updateBlog(Blog blog) {
        Blog getBlog = null;
        if (blogRepository.existsById(blog.getBlogId())) {
            getBlog = blogRepository.findById(blog.getBlogId()).get();
            getBlog.setBlogContent(blog.getBlogContent());
        }
        return blogRepository.save(getBlog);
    }

}
