package hackupc.gatech.hackaccessibility.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import hackupc.gatech.hackaccessibility.model.Post;

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
