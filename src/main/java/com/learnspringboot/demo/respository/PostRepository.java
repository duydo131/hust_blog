package com.learnspringboot.demo.respository;

import com.learnspringboot.demo.entity.Post;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;


public interface PostRepository extends JpaRepository<Post, UUID> {

    @Query(
            value="SELECT * FROM posts p WHERE p.slug_id IN (?1) ORDER BY p.create_at DESC",
            nativeQuery = true
    )
    Page<Post> findPostByListSlug(List<UUID> ids, Pageable pageable);

    @Query(
            value="SELECT * FROM posts p WHERE p.published = ?2 AND p.slug_id IN (?1) ORDER BY p.publish_at DESC",
            nativeQuery = true
    )
    Page<Post> findPostByListSlugAndPublished(List<UUID> ids, Boolean published, Pageable pageable);

    @Query("select count(id) from Post p where p.user.id = :id")
    Integer countPostByUserId(@Param("id")UUID userId);

    @NotNull
    Page<Post> findAll(@NotNull Pageable pageable);

    @Query(value = "SELECT CAST(s.id as varchar) id, s.title, s.name, count(p.id) AS number_of_post, " +
            "COUNT(p.published) FILTER (WHERE published = true) AS number_of_published, " +
            "COUNT(p.published) FILTER (WHERE published = false) AS number_of_not_published " +
            "FROM slugs s LEFT JOIN posts p ON p.slug_id = s.id " +
            "WHERE s.id in :ids " +
            "GROUP BY s.id ORDER BY number_of_post DESC LIMIT :limit",
            nativeQuery=true)
    List<Object[]> getStatistic(@Param("ids")List<UUID> ids, @Param("limit") Integer limit);

    @Query(value = "WITH group_slug AS (" +
            "   SELECT " +
            "       g.parent_id, " +
            "       COALESCE(SUM(g.number_of_post), 0) AS number_of_post, " +
            "       COALESCE(SUM(g.number_of_published), 0) AS number_of_published, " +
            "       COALESCE(SUM(g.number_of_not_published), 0) AS number_of_not_published " +
            "   FROM" +
            "       ( " +
            "           SELECT " +
            "               s1.id as id,  " +
            "               s1.title as title,  " +
            "               COUNT(p.id) AS number_of_post,  " +
            "               s1.parent_id AS parent_id, " +
            "               COUNT(p.published) FILTER (WHERE published = true) AS number_of_published, " +
            "               COUNT(p.published) FILTER (WHERE published = false) AS number_of_not_published " +
            "           FROM slugs s1 INNER JOIN slugs s2 on s1.parent_id = s2.id  " +
            "               LEFT JOIN posts p ON p.slug_id = s1.id " +
            "           GROUP BY s1.id, s2.id " +
            "       ) g " +
            "   GROUP BY g.parent_id" +
            "), slug_parent AS (" +
            "   SELECT s1.id, s1.name, s1.title " +
            "   FROM slugs s1 INNER JOIN slugs s2 ON s1.parent_id = s2.id AND s2.title = :title" +
            ")" +
            "SELECT CAST(s.id as varchar) id, s.title, s.name, g.number_of_post, g.number_of_published, g.number_of_not_published " +
            "FROM slug_parent s LEFT JOIN group_slug g ON g.parent_id = s.id ORDER BY g.number_of_post DESC LIMIT :limit",
            nativeQuery=true)
    List<Object[]> getHomeStatistic(@Param("title")String title, @Param("limit") Integer limit);

    @Query(value = "SELECT CAST(u.id as varchar) id, u.username, COUNT(p.id) as number_of_post " +
            "FROM users u LEFT JOIN posts p ON p.user_id = u.id " +
            "GROUP BY u.id ORDER BY number_of_post DESC LIMIT :limit",
        nativeQuery = true)
    List<Object[]> getUserStatistic(@Param("limit") Integer limit);
}
