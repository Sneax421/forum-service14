package ait.cohort55.post.service;

import ait.cohort55.post.dao.PostRepository;
import ait.cohort55.post.dto.NewCommentDto;
import ait.cohort55.post.dto.NewPostDto;
import ait.cohort55.post.dto.PostDto;
import ait.cohort55.post.dto.exception.PostNotFoundException;
import ait.cohort55.post.model.Comment;
import ait.cohort55.post.model.Post;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    @Override
    public PostDto addNewPost(String author, NewPostDto newPostDto) {
        Post post = modelMapper.map(newPostDto, Post.class);
        post.setAuthor(author);
        post = postRepository.save(post);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public PostDto findPostById(String id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public void addLike(String id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        post.addLike();
        postRepository.save(post);
    }

    @Override
    public PostDto updatePost(String id, NewPostDto newPostDto) {
//        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
//        if(newPostDto.getTitle() != null) {
//            post.setTitle(newPostDto.getTitle());
//        }
//        if(newPostDto.getContent() != null) {
//            post.setContent(newPostDto.getContent());
//        }
//        if(newPostDto.getTags() != null) {
//            post.getTags().addAll(newPostDto.getTags());
//        }
//        post = postRepository.save(post);
//        return modelMapper.map(post, PostDto.class);
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        String content = newPostDto.getContent();
        if (content != null) {
            post.setContent(content);
        }
        String title = newPostDto.getTitle();
        if (title != null) {
            post.setTitle(title);
        }
        Set<String> tags = newPostDto.getTags();
        if (tags != null) {
            tags.forEach(post::addTag);
        }
        post = postRepository.save(post);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public PostDto deletePost(String id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        postRepository.delete(post);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public PostDto addComment(String id, String author, NewCommentDto newCommentDto) {
//        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
//        Comment comment = modelMapper.map(newCommentDto, Comment.class);
//        post.addComment(comment);
//        postRepository.save(post);
//        return modelMapper.map(post, PostDto.class);
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        Comment comment = new Comment(author, newCommentDto.getMessage());
        post.addComment(comment);
        post = postRepository.save(post);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public Iterable<PostDto> findPostsByAuthor(String author) {
//        List<Post> posts = postRepository.findAll().stream()
//                .filter(post -> post.getAuthor().equalsIgnoreCase(author))
//                .collect(Collectors.toList());
//        return posts.stream()
//                .map(post -> modelMapper.map(post, PostDto.class))
//                .collect(Collectors.toList());
        return postRepository.findPostsByAuthorIgnoreCase(author)
                .map(post -> modelMapper.map(post, PostDto.class))
                .toList();

    }

    @Override
    public Iterable<PostDto> findPostsByTags(List<String> tags) {
//        List<Post> posts = postRepository.findAll().stream()
//                .filter(post -> post.getTags().equals(tags))
//                .collect(Collectors.toList());
//        return posts.stream()
//                .map(post -> modelMapper.map(post, PostDto.class))
//                .collect(Collectors.toList());
        return postRepository.findPostsByTagsInIgnoreCase(tags)
                .map(post -> modelMapper.map(post, PostDto.class))
                .toList();
    }

    @Override
    public Iterable<PostDto> findPostsByPeriod(LocalDate dateFrom, LocalDate dateTo) {
        return postRepository.findPostsByDateCreatedBetween(dateFrom, dateTo.plusDays(1))
                .map(post -> modelMapper.map(post, PostDto.class))
                .toList();
    }
}
