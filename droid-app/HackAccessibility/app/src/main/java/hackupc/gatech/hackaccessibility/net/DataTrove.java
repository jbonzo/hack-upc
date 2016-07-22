package hackupc.gatech.hackaccessibility.net;

import java.util.List;

import hackupc.gatech.hackaccessibility.model.Post;
import hackupc.gatech.hackaccessibility.model.Vote;

/**
 * Created by michael on 7/22/16.
 */
public interface DataTrove {

    boolean savePost(Post post);
    boolean deletePost(Post post);

    int upvotePost(Post post);
    int downvotePost(Post post);

    Vote getVote(Post post);

    Post getPost(int hash);


    List<Post> loadPosts();

}
