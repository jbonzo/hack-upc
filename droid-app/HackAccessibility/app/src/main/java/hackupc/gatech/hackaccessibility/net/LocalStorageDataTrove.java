package hackupc.gatech.hackaccessibility.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import hackupc.gatech.hackaccessibility.model.Post;
import hackupc.gatech.hackaccessibility.model.Vote;

/**
 * Created by michael on 7/22/16.
 */
public class LocalStorageDataTrove implements DataTrove {

    public SharedPreferences mSharedPrefs;

    public LocalStorageDataTrove(Context context) {
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public boolean savePost(Post post) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putString("Post_" + post.hashCode(), post.encodeToString());

        return editor.commit();
    }

    @Override
    public boolean deletePost(Post post) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();

        editor.remove("Post_" + post.hashCode());

        return editor.commit();
    }

    @Override
    public int upvotePost(Post post) {
        int res = 0;
        switch (getVote(post)) {
            case UP_VOTE: res = votePost(post, Vote.NO_VOTE.name(), -1); break;
            case DOWN_VOTE: res = votePost(post, Vote.UP_VOTE.name(), 2); break;
            case NO_VOTE: res = votePost(post, Vote.UP_VOTE.name(), 1); break;
        }

        return res;
    }

    @Override
    public int downvotePost(Post post) {
        int res = 0;
        switch (getVote(post)) {
            case UP_VOTE: res = votePost(post, Vote.DOWN_VOTE.name(), -2); break;
            case DOWN_VOTE: res = votePost(post, Vote.NO_VOTE.name(), 1); break;
            case NO_VOTE: res = votePost(post, Vote.DOWN_VOTE.name(), -1); break;
        }

        return res;
    }

    private int votePost(Post post, String voteType, int dir) {
        Post p = getPost(post.hashCode());
        p.addVote(dir);
        savePost(p);

        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putString("Vote_" + post.hashCode(), voteType);
        editor.commit();

        return p.getVotes();
    }

    @Override
    public Vote getVote(Post post) {
        String voteType = mSharedPrefs.getString("Vote_" + post.hashCode(), Vote.NO_VOTE.name());
        Log.d("getVote", "Got Vote! (" + voteType + ")");
        return Vote.valueOf(voteType);
    }

    @Override
    public Post getPost(int hash) {
        String encoded = mSharedPrefs.getString("Post_" + hash, "");
        if (encoded == "") {
            Log.e("getPost", "AWW JEEZ RICK THAT POST DOESN'T EXIST --\n\t " + "Post_" + hash);
        }

        return Post.FromEncodedString(encoded);
    }

    @Override
    public List<Post> loadPosts() {
        LinkedList<Post> posts = new LinkedList<>();

        //get all preferences, if its a post, decode dat bish
        Map<String, ?> allPrefs = mSharedPrefs.getAll();
        for (Map.Entry<String, ?> entry : allPrefs.entrySet()) {
            if (entry.getKey().startsWith("Post_")) {
                posts.add(Post.FromEncodedString((String) entry.getValue()));
            }
        }

        return posts;
    }
}
