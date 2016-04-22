package com.example.psweeney.donationappandroid;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.psweeney.donationappandroid.feed.CharityPostData;
import com.example.psweeney.donationappandroid.feed.CommentData;
import com.example.psweeney.donationappandroid.feed.DonationPostData;
import com.example.psweeney.donationappandroid.feed.FeedPostAdapter;
import com.example.psweeney.donationappandroid.feed.PostContainer;
import com.example.psweeney.donationappandroid.feed.PostData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FeedActivity extends AppCompatActivity {
    private enum FeedType{
        USER, FRIEND, CHARITY
    }

    public static String currentUserDisplayName = "Current user";
    public static String commentFieldSelectedKey = "commentFieldSelected";

    private FeedType _currentSelection = FeedType.USER;
    private PostContainer _lastInteraction = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        final ListView listViewUser = (ListView) findViewById(R.id.listViewUser);

        List<PostData> dataListUser = new ArrayList<>();

        int defUserIconId = R.drawable.ic_account_box_black_48dp;
        int defCharityIconId = R.drawable.ic_local_florist_black_48dp;
        int defPhotoLibraryIconId = R.drawable.ic_photo_library_black_48dp;



        DonationPostData newUserDonationPostData1 = new DonationPostData(defUserIconId, null, "Charity A", 509);
        newUserDonationPostData1.getPostTime().set(Calendar.YEAR, newUserDonationPostData1.getPostTime().get(Calendar.YEAR) - 2);

        DonationPostData newUserDonationPostData2 = new DonationPostData(defUserIconId, null, "Charity B", 142);
        newUserDonationPostData2.getPostTime().set(Calendar.MONTH, newUserDonationPostData2.getPostTime().get(Calendar.MONTH) - 5);

        DonationPostData newUserDonationPostData3 = new DonationPostData(defUserIconId, null, "Charity D", 330);
        newUserDonationPostData3.getPostTime().set(Calendar.DAY_OF_MONTH, newUserDonationPostData3.getPostTime().get(Calendar.DAY_OF_MONTH) - 15);

        DonationPostData newUserDonationPostData4 = new DonationPostData(defUserIconId, null, "Charity X", 601);
        newUserDonationPostData4.getPostTime().set(Calendar.DAY_OF_MONTH, newUserDonationPostData4.getPostTime().get(Calendar.DAY_OF_MONTH) - 2);

        DonationPostData newUserDonationPostData5 = new DonationPostData(defUserIconId, null, "Charity A", 49);
        if(newUserDonationPostData5.getPostTime().get(Calendar.AM_PM) == Calendar.PM){
            newUserDonationPostData5.getPostTime().set(Calendar.AM_PM, Calendar.AM);
        } else {
            newUserDonationPostData5.getPostTime().set(Calendar.DAY_OF_MONTH, newUserDonationPostData5.getPostTime().get(Calendar.DAY_OF_MONTH) - 1);
            newUserDonationPostData5.getPostTime().set(Calendar.AM_PM, Calendar.PM);
        }

        DonationPostData newUserDonationPostData6 = new DonationPostData(defUserIconId, null, "Charity Z", 11);
        newUserDonationPostData6.getPostTime().set(Calendar.MINUTE, Math.max(0, newUserDonationPostData6.getPostTime().get(Calendar.MINUTE) - 10));

        DonationPostData newUserDonationPostData7 = new DonationPostData(defUserIconId, null, "Charity B", 15);

        dataListUser.add(newUserDonationPostData7);
        dataListUser.add(newUserDonationPostData6);
        dataListUser.add(newUserDonationPostData5);
        dataListUser.add(newUserDonationPostData4);
        dataListUser.add(newUserDonationPostData3);
        dataListUser.add(newUserDonationPostData2);
        dataListUser.add(newUserDonationPostData1);

        final FeedPostAdapter adapterUser = new FeedPostAdapter(this, R.layout.feed_post_donation, dataListUser);

        listViewUser.setAdapter(adapterUser);

        final ListView listViewFriend = (ListView) findViewById(R.id.listViewFriend);
        String steveName = "Steve Fessler", neilName = "Neil Alberg", seanName = "Sean Kallungal",
                jonName = "Dr. Jon Froehlich";

        List<PostData> dataListFriend = new ArrayList<>();

        ArrayList<CommentData> comments = new ArrayList<>();
        comments.add(new CommentData(steveName, "Nice"));
        comments.add(new CommentData(jonName, "Super cool"));
        comments.add(new CommentData(neilName, "hi"));
        comments.add(new CommentData(neilName, "hi"));
        comments.add(new CommentData(seanName, "that's a whole lot of money jon"));
        comments.add(new CommentData(jonName, "it sure is"));

        DonationPostData newFriendDonationPostData1 = new DonationPostData(defUserIconId, jonName, "The Dr. Jon Froehlich Foundation",
                Calendar.getInstance(), 99999, 2, false, comments);
        newFriendDonationPostData1.getPostTime().set(Calendar.YEAR, newFriendDonationPostData1.getPostTime().get(Calendar.YEAR) - 3);

        DonationPostData newFriendDonationPostData2 = new DonationPostData(defUserIconId, neilName, "Charity A", 142);
        newFriendDonationPostData2.getPostTime().set(Calendar.MONTH, newFriendDonationPostData2.getPostTime().get(Calendar.MONTH) - 3);

        DonationPostData newFriendDonationPostData3 = new DonationPostData(defUserIconId, neilName, "Charity B", 840);
        newFriendDonationPostData3.getPostTime().set(Calendar.DAY_OF_MONTH, newFriendDonationPostData3.getPostTime().get(Calendar.DAY_OF_MONTH) - 17);

        DonationPostData newFriendDonationPostData4 = new DonationPostData(defUserIconId, seanName, "Charity T", 84);
        newFriendDonationPostData4.getPostTime().set(Calendar.DAY_OF_MONTH, newFriendDonationPostData4.getPostTime().get(Calendar.DAY_OF_MONTH) - 4);

        DonationPostData newFriendDonationPostData5 = new DonationPostData(defUserIconId, steveName, "Charity O", 136);
        if(newFriendDonationPostData5.getPostTime().get(Calendar.AM_PM) == Calendar.PM){
            newFriendDonationPostData5.getPostTime().set(Calendar.AM_PM, Calendar.AM);
        } else {
            newFriendDonationPostData5.getPostTime().set(Calendar.DAY_OF_MONTH, newFriendDonationPostData5.getPostTime().get(Calendar.DAY_OF_MONTH) - 1);
            newFriendDonationPostData5.getPostTime().set(Calendar.AM_PM, Calendar.PM);
        }

        DonationPostData newFriendDonationPostData6 = new DonationPostData(defUserIconId, seanName, "Charity E", 487);
        newFriendDonationPostData6.getPostTime().set(Calendar.MINUTE, Math.max(0, newFriendDonationPostData6.getPostTime().get(Calendar.MINUTE) - 20));

        DonationPostData newFriendDonationPostData7 = new DonationPostData(defUserIconId, steveName, "Charity F", 104);

        dataListFriend.add(newFriendDonationPostData7);
        dataListFriend.add(newFriendDonationPostData6);
        dataListFriend.add(newFriendDonationPostData5);
        dataListFriend.add(newFriendDonationPostData4);
        dataListFriend.add(newFriendDonationPostData3);
        dataListFriend.add(newFriendDonationPostData2);
        dataListFriend.add(newFriendDonationPostData1);

        final FeedPostAdapter adapterFriend = new FeedPostAdapter(this, R.layout.feed_post_donation, dataListFriend);

        listViewFriend.setAdapter(adapterFriend);

        final ListView listViewCharity = (ListView) findViewById(R.id.listViewCharity);

        List<PostData> dataListCharity = new ArrayList<>();

        dataListCharity.add(new CharityPostData(defCharityIconId, "Charity A",
                "Hello everyone,\nThank you for donating to Charity A this month. We're grateful for " +
                        "every donation we get.\n\nSincerely,\nThe Charity A Staff"));
        dataListCharity.add(new CharityPostData(defCharityIconId, "Charity B", defPhotoLibraryIconId,
                "Hello everyone,\nCheck out our new photos from the Charity B volunteer event last Sunday. We appreciate" +
                        "the help as well as your continued support through donations.\n\nSincerely,\nThe Charity B Staff"));

        final FeedPostAdapter adapterCharity = new FeedPostAdapter(this, R.layout.feed_post_charity, dataListCharity);

        listViewCharity.setAdapter(adapterCharity);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                Bundle bundle = data.getExtras();
                if(bundle == null || _lastInteraction == null){
                    return;
                }

                _lastInteraction.getData().setNumLikes(bundle.getInt(PostData.numLikesKey));
                _lastInteraction.getData().setLikedByUser(bundle.getBoolean(PostData.likedByUserKey));

                ArrayList<CommentData> newComments = new ArrayList<>();
                int newNumComments = bundle.getInt(PostData.numCommentsKey);
                for(int i = 0; i < newNumComments; i++){
                    newComments.add(CommentData.extractCommentDataFromBundle(bundle, i));
                }

                _lastInteraction.getData().setComments(newComments);
                _lastInteraction.updateViews();
            }
        }
    }

    private void updateFeedSelection(){

        TextView userLabel = (TextView) findViewById(R.id.textViewUserFeedLabel);
        TextView friendLabel = (TextView) findViewById(R.id.textViewFriendFeedLabel);
        TextView charityLabel = (TextView) findViewById(R.id.textViewCharityFeedLabel);

        ListView userFeed = (ListView) findViewById(R.id.listViewUser);
        ListView friendFeed = (ListView) findViewById(R.id.listViewFriend);
        ListView charityFeed = (ListView) findViewById(R.id.listViewCharity);


        switch (_currentSelection){
            case USER:
                userLabel.setTextColor(getResources().getColor(R.color.colorAccent));
                userLabel.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                friendLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
                friendLabel.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                charityLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
                charityLabel.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                userFeed.setVisibility(View.VISIBLE);
                friendFeed.setVisibility(View.GONE);
                charityFeed.setVisibility(View.GONE);

                break;
            case FRIEND:
                friendLabel.setTextColor(getResources().getColor(R.color.colorAccent));
                friendLabel.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                userLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
                userLabel.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                charityLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
                charityLabel.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                userFeed.setVisibility(View.GONE);
                friendFeed.setVisibility(View.VISIBLE);
                charityFeed.setVisibility(View.GONE);

                break;
            case CHARITY:
                charityLabel.setTextColor(getResources().getColor(R.color.colorAccent));
                charityLabel.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                userLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
                userLabel.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                friendLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
                friendLabel.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                userFeed.setVisibility(View.GONE);
                friendFeed.setVisibility(View.GONE);
                charityFeed.setVisibility(View.VISIBLE);

                break;
        }
    }

    public void viewPost(View v, boolean commentRequested){
        if(v == null){
            return;
        }

        PostContainer parentPostContainer = FeedPostAdapter.getParentPostContainer(v);
        _lastInteraction = parentPostContainer;

        if(parentPostContainer == null){
            Toast.makeText(getApplicationContext(), "Something went wrong while trying to view this post (invalid container)", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent singlePostIntent = new Intent(getApplicationContext(), SinglePostActivity.class);
        Bundle bundle = parentPostContainer.getData().convertToBundle();

        if(singlePostIntent == null || bundle == null){
            Toast.makeText(getApplicationContext(), "Something went wrong while trying to view this post (null bundle)", Toast.LENGTH_SHORT).show();
            return;
        }

        bundle.putBoolean(commentFieldSelectedKey, commentRequested);

        singlePostIntent.putExtras(bundle);
        startActivityForResult(singlePostIntent, 1);
    }

    public void onButtonClickUserFeed(View v){
        if(_currentSelection != FeedType.USER){
            _currentSelection = FeedType.USER;
            updateFeedSelection();
        }
    }

    public void onButtonClickFriendFeed(View v){
        if(_currentSelection != FeedType.FRIEND){
            _currentSelection = FeedType.FRIEND;
            updateFeedSelection();
        }
    }

    public void onButtonClickCharityFeed(View v){
        if(_currentSelection != FeedType.CHARITY){
            _currentSelection = FeedType.CHARITY;
            updateFeedSelection();
        }
    }

    public void onButtonClickPostBody(View v){
        v.setAlpha(0);
        v.animate().alpha(1);
        viewPost(v, false);
    }

    public void onButtonClickLike(View v){
        if(v == null || v.getId() != R.id.frameLayoutLikeContainer){
            return;
        }

        View subContainer = v.findViewById(R.id.likeSubContainer);
        PostContainer parentContainer = FeedPostAdapter.getParentPostContainer(v);
        _lastInteraction = parentContainer;
        parentContainer.getData().setLikedByUser(!parentContainer.getData().likedByUser());
        if(parentContainer.getData().likedByUser()){
            parentContainer.getData().setNumLikes(parentContainer.getData().getNumLikes() + 1);
        } else {
            parentContainer.getData().setNumLikes(Math.max(0, parentContainer.getData().getNumLikes() - 1));
        }

        parentContainer.updateViews();

        subContainer.setAlpha(0);
        subContainer.animate().alpha(1);
    }

    public void onButtonClickComment(View v){
        if(v.getId() != R.id.frameLayoutCommentContainer) {
            return;
        }

        View subContainer = v.findViewById(R.id.commentSubContainer);
        subContainer.setAlpha(0);
        subContainer.animate().alpha(1);

        viewPost(v, true);
    }
}
