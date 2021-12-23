package com.learnspringboot.demo.service.db;

import com.learnspringboot.demo.config.Contants;
import com.learnspringboot.demo.config.RoleEnum;
import com.learnspringboot.demo.dto.mapper.PostMapper;
import com.learnspringboot.demo.dto.mapper.SlugMapper;
import com.learnspringboot.demo.dto.post.*;
import com.learnspringboot.demo.dto.slug.SlugResponseDTO;
import com.learnspringboot.demo.entity.Permission;
import com.learnspringboot.demo.entity.Post;
import com.learnspringboot.demo.entity.Slug;
import com.learnspringboot.demo.entity.User;
import com.learnspringboot.demo.exception.custom_exception.MyException;
import com.learnspringboot.demo.exception.custom_exception.NotPermissionException;
import com.learnspringboot.demo.respository.PostRepository;
import com.learnspringboot.demo.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SlugService slugService;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private SlugMapper slugMapper;

    private void save(Post post){
        post.setUpdateAt(new Date());
        postRepository.save(post);
    }

    public PostResponseDTO findPostById(UUID id) throws Exception {
        Post post = postRepository.findById(id).orElseThrow(() -> new MyException(String.format("post id %s not found.", id.toString())));
        if(!post.getPublished()) throw new MyException(String.format("post id %s is not publish.", id.toString()));

        SlugResponseDTO[] slugs = slugService.getParentSlug(post.getSlug().getTitle()).stream().toArray(SlugResponseDTO[]::new);
        return new PostResponseDTO(postMapper.mapToPostContentDTO(post), slugs);
    }

    public ListPostResponseDTO findPostBySlug(
            HttpServletRequest request,
            Pageable pageable,
            String title) throws Exception {

        Slug s = slugService.findByTitle(title);
        SlugResponseDTO[] slugs = slugService.getParentSlug(title).stream().toArray(SlugResponseDTO[]::new);

        Page<Post> posts = findPostByRole(request, pageable, s);
        List<PostContentResponseDTO> postResponses = posts.stream().map(postMapper::mapToPostContentDTO).collect(Collectors.toList());
        Page<PostContentResponseDTO> pagePosts = new PageImpl<>(postResponses);

        return new ListPostResponseDTO(pagePosts, slugs);
    }

    private Page<Post> findPostByRole(
            HttpServletRequest request,
            Pageable pageable,
            Slug slug) throws Exception {
        UserPrincipal userPrincipal = (UserPrincipal)request.getAttribute(Contants.PRINCIPAL);
        if(userPrincipal == null) return postRepository.findPostBySlugAndPublished(slug, true, pageable);

        User user = userPrincipal.getUser();
        if(user.getRole().getName().equals(RoleEnum.ROLE_ADMIN.getName()))
            return postRepository.findPostBySlug(slug, pageable);

        return postRepository.findPostBySlugAndPublished(slug, true, pageable);
    }

    public PostContentResponseDTO save(
            HttpServletRequest request,
            PostRequestDTO postRequest,
            String slugStr) throws Exception {
        User user = ((UserPrincipal)request.getAttribute(Contants.PRINCIPAL)).getUser();
        if(user == null) throw new Exception("Server Error");

        if(!checkPermission(user, request.getServletPath()))
            throw new NotPermissionException("You not permission.");

        Slug slug = slugService.findByTitle(slugStr);

        Post post = postMapper.mapToPost(postRequest);
        post.setUser(user);
        post.setSlug(slug);
        post.setCreateAt(new Date());
        if(post.getPublished()) post.setPublishAt(new Date());
        save(post);

        return postMapper.mapToPostContentDTO(post);
    }

    private boolean checkPermission(User user, String module){
        if(user == null) return false;
        if(user.getRole().getName().equalsIgnoreCase("ROLE_ADMIN")) return true;
        Set<Permission> permissions = (Set<Permission>) user.getPermission();
        for(Permission permission: permissions){
            if(permission.getModule().equalsIgnoreCase(module) && permission.getAction().equalsIgnoreCase("POST")){
                return true;
            }
        }
        return false;
    }

    public PostContentResponseDTO updatePost(
            HttpServletRequest request,
            UUID id,
            PostUpdateRequestDTO postUpdateRequestDTO) throws Exception {

        User user = ((UserPrincipal)request.getAttribute(Contants.PRINCIPAL)).getUser();
        if(user == null) throw new Exception("Server Error");

        Post post = postRepository.findById(id).orElseThrow(
                () -> new MyException(String.format("Post id %s not found.", id.toString()))
        );

        if(!post.getUser().getId().equals(user.getId()))
            throw new Exception(String.format("You not permission to update post id %s", id.toString()));

        Boolean changePublish = postUpdateRequestDTO.getPublished();
        if(changePublish != null && changePublish && !post.getPublished()) post.setPublishAt(new Date());

        postMapper.updatePostFromPostUpdateRequestDTO(postUpdateRequestDTO, post);

        save(post);
        return postMapper.mapToPostContentDTO(post);
    }

    public Object getMyPost(HttpServletRequest request, Pageable pageable) throws Exception {
        User user = ((UserPrincipal)request.getAttribute(Contants.PRINCIPAL)).getUser();
        if(user == null) throw new Exception("Server Error");

        List<Post> posts = new ArrayList<>(user.getPosts());
        List<MyPostContentResponseDTO> postResponse = posts.stream().map(postMapper::postToMyPostContentResponseDTO).collect(Collectors.toList());

        int start = (int)pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), postResponse.size());
        return new PageImpl<>(postResponse.subList(start, end), pageable, postResponse.size());
    }
}
