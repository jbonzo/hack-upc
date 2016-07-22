package hackupc.gatech.hackaccessibility.net;

import java.util.List;

import hackupc.gatech.hackaccessibility.model.Post;

/**
 * Created by michael on 7/22/16.
 */
public interface DataTrove {

    boolean savePost(Post post);
    boolean deletePost(Post post);

    boolean upvotePost(Post post);
    boolean downvotePost(Post post);

    Post getPost(int hash);


    List<Post> loadPosts();

}
