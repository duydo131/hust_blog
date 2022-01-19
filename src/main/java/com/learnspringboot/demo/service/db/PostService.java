package com.learnspringboot.demo.service.db;

import com.learnspringboot.demo.config.Contants;
import com.learnspringboot.demo.config.RoleEnum;
import com.learnspringboot.demo.dto.mapper.PostMapper;
import com.learnspringboot.demo.dto.mapper.SlugMapper;
import com.learnspringboot.demo.dto.post.*;
import com.learnspringboot.demo.dto.post.statistic.PostStatisticByCategoryResponseObject;
import com.learnspringboot.demo.dto.post.statistic.PostStatisticByUserResponseObject;
import com.learnspringboot.demo.dto.post.statistic.StatisticPostByCategoryResponse;
import com.learnspringboot.demo.dto.post.statistic.StatisticPostByUserResponse;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Predicate;
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

    public PostResponseDTO findPostById(HttpServletRequest request, UUID id) throws Exception {
        UserPrincipal userPrincipal = ((UserPrincipal)request.getAttribute(Contants.PRINCIPAL));
        Post post = postRepository.findById(id).orElseThrow(() -> new MyException(String.format("post id %s not found.", id.toString())));

        if(userPrincipal == null){
            if(!post.getPublished()) throw new MyException(String.format("post id %s is not publish.", id.toString()));
            SlugResponseDTO[] slugs = slugService.getParentSlug(post.getSlug().getTitle()).stream().toArray(SlugResponseDTO[]::new);
            return new PostResponseDTO(postMapper.mapToPostContentDTO(post), slugs);
        }

        User user = userPrincipal.getUser();
        if(user == null) throw new Exception("Server Error");
        if(!user.getRole().getName().equals("ROLE_ADMIN") && !post.getUser().getId().equals(user.getId()))
            if(!post.getPublished())
                throw new BadCredentialsException(String.format("post id %s is not publish.", id.toString()));

        SlugResponseDTO[] slugs = slugService.getParentSlug(post.getSlug().getTitle()).stream().toArray(SlugResponseDTO[]::new);
        return new PostResponseDTO(postMapper.mapToPostContentDTO(post), slugs);
    }

    public ListPostResponseDTO findPostBySlug(
            HttpServletRequest request,
            Pageable pageable,
            String title) throws Exception {
        Page<Post> posts = null;
        SlugResponseDTO[] slugs = null;

        if(title.equals("home")){
            posts = postRepository.findAll(pageable);
        }else{
            Slug s = slugService.findByTitle(title);
            slugs = slugService.getParentSlug(title).stream().toArray(SlugResponseDTO[]::new);
            posts = findPostByRole(request, pageable, s);
        }
        if(posts == null) throw new Exception("Database Error");
        List<PostContentResponseDTO> postResponses = posts.getContent().stream().map(postMapper::mapToPostContentDTO).collect(Collectors.toList());
        Page<PostContentResponseDTO> pagePosts = new PageImpl<>(
                postResponses,
                posts.getPageable(),
                posts.getTotalElements()
            );

        return new ListPostResponseDTO(pagePosts, slugs);
    }

    private Page<Post> findPostByRole(
            HttpServletRequest request,
            Pageable pageable,
            Slug slug) {
        List<UUID> slugIds = slugService.getAllChild(slug).stream().map(Slug::getId).collect(Collectors.toList());
        UserPrincipal userPrincipal = (UserPrincipal)request.getAttribute(Contants.PRINCIPAL);
        if(userPrincipal == null)
            return postRepository.findPostByListSlugAndPublished(slugIds, true, pageable);

        User user = userPrincipal.getUser();
        if(user.getRole().getName().equals(RoleEnum.ROLE_ADMIN.getName()))
            return postRepository.findPostByListSlug(slugIds, pageable);

        return postRepository.findPostByListSlugAndPublished(slugIds, true, pageable);
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
        UserPrincipal userPrincipal = ((UserPrincipal)request.getAttribute(Contants.PRINCIPAL));
        if(userPrincipal == null) throw new Exception("Server Error");
        User user = userPrincipal.getUser();
        if(user == null) throw new Exception("Server Error");

        List<Post> posts = new ArrayList<>(user.getPosts());
        posts.sort((p1, p2) -> (int) (-p1.getCreateAt().getTime() + p2.getCreateAt().getTime()));
        List<MyPostContentResponseDTO> postResponse = posts.stream().map(postMapper::postToMyPostContentResponseDTO).collect(Collectors.toList());

        int start = (int)pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), postResponse.size());
        return new PageImpl<>(postResponse.subList(start, end), pageable, postResponse.size());
    }

    public StatisticPostByCategoryResponse getStatisticByCategory(String title, Integer limit) throws Exception{
        List<PostStatisticByCategoryResponseObject> data = null;
        Integer all = 100;
        if(title.equals("trang-chu")) data = getHomeStatistic(title, all);
        else data = getStatistic(title, all);

        return buildStatisticPostByCategoryResponse(data, limit);
    }

    private StatisticPostByCategoryResponse buildStatisticPostByCategoryResponse(
            List<PostStatisticByCategoryResponseObject> data,
            Integer limit
        ) throws Exception {
        if(data == null) throw new Exception("Database Error");
        Integer numberOfPost = 0;
        Integer numberOfPostPublished = 0;
        Integer numberOfPostNotPublished = 0;
        List<PostStatisticByCategoryResponseObject> realData = new ArrayList<>();

        int i = 0;
        for(; i < data.size() && i < limit; ++i){
            numberOfPost += data.get(i).getNumberOfPost();
            numberOfPostPublished += data.get(i).getNumberOfPublished();
            numberOfPostNotPublished += data.get(i).getNumberOfNotPublished();
            realData.add(data.get(i));
        }

        for(;i < data.size(); ++i){
            numberOfPost += data.get(i).getNumberOfPost();
            numberOfPostPublished += data.get(i).getNumberOfPublished();
            numberOfPostNotPublished += data.get(i).getNumberOfNotPublished();
        }

        return new StatisticPostByCategoryResponse(
                numberOfPost,
                numberOfPostPublished,
                numberOfPostNotPublished,
                realData
        );
    }

    private List<PostStatisticByCategoryResponseObject> getStatistic(String title, Integer limit) throws Exception {
        List<UUID> slugIds = slugService.getSlugStatisticWithoutParent(title).stream().map(Slug::getId).collect(Collectors.toList());
        List<Object[]> information = postRepository.getStatistic(slugIds, limit);
        List<PostStatisticByCategoryResponseObject> responses = new ArrayList<>();
        for(Object[] o : information){
            responses.add(mapToPostStatisticResponse(o));
        }
        return responses;
    }

    private List<PostStatisticByCategoryResponseObject> getHomeStatistic(String title, Integer limit) throws Exception {
        List<Object[]> information = postRepository.getHomeStatistic(title, limit);
        List<PostStatisticByCategoryResponseObject> responses = new ArrayList<>();
        for(Object[] o : information){
            responses.add(mapToPostStatisticResponse(o));
        }
        return responses;
    }

    private PostStatisticByCategoryResponseObject mapToPostStatisticResponse(Object[] o) throws Exception {
        if(o.length != 6)
            throw new Exception("Database Error");
        return new PostStatisticByCategoryResponseObject(
                UUID.fromString(String.valueOf(o[0])),
                String.valueOf(o[1]),
                String.valueOf(o[2]),
                Integer.valueOf(String.valueOf(o[3])),
                Integer.valueOf(String.valueOf(o[4])),
                Integer.valueOf(String.valueOf(o[5]))
        );
    }

    public StatisticPostByUserResponse getStatisticByUser(Integer limit) throws Exception{
        Integer all = 100;
        List<Object[]> information = postRepository.getUserStatistic(all);
        List<PostStatisticByUserResponseObject> data = new ArrayList<>();
        for(Object[] o : information){
            data.add(mapToPostStatisticByUserResponseObject(o));
        }
        return buildStatisticPostByUserResponse(data, limit);
    }

    private StatisticPostByUserResponse buildStatisticPostByUserResponse(
            List<PostStatisticByUserResponseObject> data,
            Integer limit
    ) throws Exception {
        if(data == null) throw new Exception("Database Error");
        Integer numberOfPost = 0;
        Integer numberOfUser = data.size();
        List<PostStatisticByUserResponseObject> realData = new ArrayList<>();

        int i = 0;
        for(; i < data.size() && i < limit; ++i){
            numberOfPost += data.get(i).getNumberOfPost();
            realData.add(data.get(i));
        }

        for(;i < data.size(); ++i){
            numberOfPost += data.get(i).getNumberOfPost();
        }

        return new StatisticPostByUserResponse(
                numberOfPost,
                numberOfUser,
                realData
        );
    }

    private PostStatisticByUserResponseObject mapToPostStatisticByUserResponseObject(Object[] o) throws Exception {
        if(o.length != 3)
            throw new Exception("Database Error");
        return new PostStatisticByUserResponseObject(
                UUID.fromString(String.valueOf(o[0])),
                String.valueOf(o[1]),
                Integer.valueOf(String.valueOf(o[2]))
        );
    }

    public void deletePost(HttpServletRequest request, UUID id) throws Exception {
        UserPrincipal userPrincipal = ((UserPrincipal) request.getAttribute(Contants.PRINCIPAL));
        if (userPrincipal == null) throw new BadCredentialsException("You not permissions");
        User user = userPrincipal.getUser();
        if (user == null) throw new BadCredentialsException("You not permissions");

        Post post = postRepository.findById(id).orElseThrow(
                () -> new BadCredentialsException(String.format("Post id %s not found", id.toString()))
        );

        if(!user.getRole().getName().equals("ROLE_ADMIN") && !post.getUser().getId().equals(user.getId()))
            throw new BadCredentialsException("You not permissions");

        postRepository.deleteById(id);
    }
}
