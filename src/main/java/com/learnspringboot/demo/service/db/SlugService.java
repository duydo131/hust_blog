package com.learnspringboot.demo.service.db;

import com.learnspringboot.demo.config.Contants;
import com.learnspringboot.demo.dto.mapper.SlugMapper;
import com.learnspringboot.demo.dto.slug.SlugAdditionalRequestDTO;
import com.learnspringboot.demo.dto.slug.SlugResponseInfomationDTO;
import com.learnspringboot.demo.entity.Permission;
import com.learnspringboot.demo.entity.Slug;
import com.learnspringboot.demo.entity.User;
import com.learnspringboot.demo.exception.custom_exception.MyException;
import com.learnspringboot.demo.respository.SlugRepository;
import com.learnspringboot.demo.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SlugService {

    @Autowired
    private SlugRepository slugRepository;

    @Autowired
    private SlugMapper slugMapper;

    public void save(Slug slug) {slugRepository.save(slug);}

    public List<Slug> findByParent(Slug slug){
        return slugRepository.findByParent(slug);
    }

    public List<String> findAllSlugTitle(){
        return slugRepository.findAllTitle();
    }

    public Collection<Object> getParentSlug(String title) throws Exception {
        Slug slug = slugRepository.findByTitle(title).orElseThrow(
                () -> new MyException(String.format("slug title %s not define.", title))
        );
        return getAllParent(slug).stream().map(slugMapper::mapToSlugResponseDTO).collect(Collectors.toList());
    }

    public Collection<Object> save(SlugAdditionalRequestDTO slugAdditional) throws Exception {
        Slug slugParent = slugRepository.findById(slugAdditional.getParentID()).orElseThrow(
                () -> new MyException(String.format("Slug parent ID %s is not found", slugAdditional.getParentID().toString()))
        );

        Slug slug = new Slug(
                slugAdditional.getTitle(),
                slugAdditional.getName(),
                slugParent
        );
        slugRepository.save(slug);
        return getAllParent(slug).stream().map(slugMapper::mapToSlugResponseDTO).collect(Collectors.toList());
    }

    public List<Slug> getAllParent(Slug slug){
        List<Slug> slugParent = new ArrayList<>();
        while(slug != null){
            slugParent.add(slug);
            slug = slug.getParent();
        }
        return slugParent;
    }

    public List<Slug> getAllParent(String slugStr) throws Exception {
        Slug slug = findByTitle(slugStr);
        return getAllParent(slug);
    }

    public List<Slug> getAllChild(Slug slug){
        List<Slug> slugChild = new ArrayList<>();
        Queue<Slug> queueSlug = new LinkedList<>();
        queueSlug.add(slug);

        while((slug = queueSlug.poll())!= null){
            slugChild.add(slug);
            queueSlug.addAll(findByParent(slug));
        }
        return slugChild;
    }

    public List<Slug> getAllChild(String slugStr) throws Exception {
        Slug slug = findByTitle(slugStr);
        return getAllChild(slug);
    }

    public Slug findByTitle(String title) throws Exception {
        return slugRepository.findByTitle(title).orElseThrow(
                () -> new MyException(String.format("Slug %s not define", title))
        );
    }

    public List<?> getMySlug(HttpServletRequest request){
        UserPrincipal userPrincipal = ((UserPrincipal)request.getAttribute(Contants.PRINCIPAL));
        if(userPrincipal == null)
            throw new AccessDeniedException("You not permissions");
        User user = userPrincipal.getUser();
        if(user == null)
            throw new AccessDeniedException("You not permissions");

        if(user.getRole().getName().equals("ROLE_NAME")){
            return slugRepository.findAll().stream().map(slugMapper::toSlugResponseInfomationDto).collect(Collectors.toList());
        }
        List<SlugResponseInfomationDTO> slugs = new ArrayList<>();
        Set<Permission> permissions = user.getPermission();
        for(Permission permission: permissions){
            slugs.add(new SlugResponseInfomationDTO(permission.getSlugId(), null, permission.getSlug()));
        }
        return slugs;
    }

    public List<?> getSlugs(String slug, String type) throws Exception {
        List<Slug> slugList = null;
        if(type.equals("CHILDREN")) {
            slugList = getAllChild(slug);
            slugList.remove(0);
        }
        else if(type.equals("PARENT")) slugList = getAllParent(slug);
        else throw new BadCredentialsException(String.format("Type %s not exists!", type));
        List<SlugResponseInfomationDTO> slugs = new ArrayList<>();
        for(Slug s: slugList){
            slugs.add(slugMapper.toSlugResponseInfomationDto(s));
        }
        return slugs;
    }

    public List<?> getSlugStatisticWithParent(String parentTitle) throws Exception {
        Slug parent = findByTitle(parentTitle);
        List<Slug> slugs = new ArrayList<>();
        slugs.add(parent);
        slugs.addAll(findByParent(parent));
        return slugs.stream().map(slugMapper::toSlugResponseInfomationDto).collect(Collectors.toList());
    }

    public List<Slug> getSlugStatisticWithoutParent(String parentTitle) throws Exception {
        Slug parent = findByTitle(parentTitle);
        return new ArrayList<>(findByParent(parent));
    }
}
