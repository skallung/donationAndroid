package com.example.psweeney.donationappandroid.feed;

import com.example.psweeney.donationappandroid.R;
import com.example.psweeney.donationappandroid.charity.CharityDetailData;
import com.example.psweeney.donationappandroid.charity.CharityDetailFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

/**
 * Created by psweeney on 4/22/16.
 *
 * Used for retrieving and editing PostData across different classes and activities
 *
 * During the SplashScreen activity (setup period), this class's init() method is called after
 * CharityDetailFactory.init()
 *
 * A few elements of PostData objects are hardcoded (DonationPostData author names, comment text,
 * CharityPostData body text, etc) but everything else is randomized.
 * generated.
 */
public class PostFactory {
    public static final int DEF_USER_ICON_ID = R.drawable.ic_account_box_black_48dp;
    public static final int DEF_CHARITY_ICON_ID = R.drawable.ic_local_florist_black_48dp;
    public static final int DEF_BODY_IMAGE_ICON_ID = R.drawable.ic_photo_library_black_48dp;
    public static Calendar lastUpdate;

    private static final int NUM_USER_POSTS = 50;
    private static final int NUM_FRIEND_POSTS = 50;
    private static final int MAX_NUM_COMMENTS = 5;
    private static final int MAX_NUM_LIKES = 10;

    private static final Map<Integer, PostData> POST_MAP = new HashMap<>();
    private static final Comparator<PostData> POST_COMPARATOR = new Comparator<PostData>(){
        public int compare(PostData p1, PostData p2){
            if(p1 == null){
                return 1;
            } else if(p2 == null){
                return -1;
            }

            int calendarCompare = p2.getPostTime().compareTo(p1.getPostTime());
            if(calendarCompare != 0){
                return calendarCompare;
            }

            return p2.getPostIdentifier() - p1.getPostIdentifier();
        }
    };

    private static final ArrayList<String> _friendNames = new ArrayList<>();

    public static void init(){
        _friendNames.add("Patrick Sweeney");
        _friendNames.add("Steve Fessler");
        _friendNames.add("Neil Alberg");
        _friendNames.add("Sean Kallungal");
        _friendNames.add("Dr. Jon Froehlich");
        populateUserPosts();
        populateFriendPosts();
        populateCharityPosts();
        lastUpdate = Calendar.getInstance();
    }

    private static Calendar getRandomCalendar(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, (int) (Math.random() * 12f));
        calendar.set(Calendar.DAY_OF_MONTH, 1 + (int) (Math.random() * 27f));
        calendar.set(Calendar.HOUR_OF_DAY, 1 + (int) (Math.random() * 23f));
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR_OF_DAY) % 12);
        calendar.set(Calendar.MINUTE, (int) (Math.random() * 60f));

        if(calendar.compareTo(Calendar.getInstance()) >= 0){
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
        }
        return calendar;
    }

    private static DonationPostData generateRandomUserPost(){
        List<CharityDetailData> charities = CharityDetailFactory.getCharityList();

        Calendar calendar = getRandomCalendar();

        CharityDetailData charity = charities.get((int) (Math.random() * ((float) charities.size())));
        int donationAmount = (int) (Math.random() * 999f);

        int numLikes = (int) (Math.random() * (float) MAX_NUM_LIKES);

        return new DonationPostData(DEF_USER_ICON_ID, null, charity.getIdentifier(), calendar, donationAmount, numLikes,
                false, generateRandomCommentList());
    }

    private static void populateUserPosts(){
        List<CharityDetailData> charities = CharityDetailFactory.getCharityList();

        for(int i = 0; i < NUM_USER_POSTS; i++){
            PostData post = generateRandomUserPost();
            POST_MAP.put(post.getPostIdentifier(), post);
        }
    }

    private static ArrayList<CommentData> generateRandomCommentList(){
        ArrayList<String> commentStrings = new ArrayList<>();

        commentStrings.add("Nice");
        commentStrings.add("Super cool");
        commentStrings.add("hi");
        commentStrings.add("nice donation");
        commentStrings.add("good job");
        commentStrings.add("this is great");

        ArrayList<CommentData> comments = new ArrayList<>();
        int numComments = (int) (Math.random() * ((float) MAX_NUM_COMMENTS));
        for(int i = 0; i < numComments; i++){
            String currCommentString = commentStrings.get((int) (Math.random() * ((float) commentStrings.size())));
            String author = _friendNames.get((int) (Math.random() * ((float) _friendNames.size())));

            comments.add(new CommentData(author, currCommentString));
        }
        return comments;
    }

    private static DonationPostData generateRandomFriendPost(){
        List<CharityDetailData> charities = CharityDetailFactory.getCharityList();

        String authorName = _friendNames.get((int) (Math.random() * ((float) _friendNames.size())));

        Calendar calendar = getRandomCalendar();

        CharityDetailData charity = charities.get((int) (Math.random() * ((float) charities.size())));
        int donationAmount = (int) (Math.random() * 999f);

        int numLikes = (int) (Math.random() * (float) MAX_NUM_LIKES);
        boolean likedByUser = Math.random() > 0.75;
        if(likedByUser)
            numLikes++;

        return new DonationPostData(DEF_USER_ICON_ID, authorName, charity.getIdentifier(), calendar, donationAmount, numLikes,
                likedByUser, generateRandomCommentList());
    }

    private static void populateFriendPosts(){

        for(int i = 0; i < NUM_FRIEND_POSTS; i++){
            PostData post = generateRandomFriendPost();
            POST_MAP.put(post.getPostIdentifier(), post);
        }

    }

    private static void populateCharityPosts(){
        List<CharityDetailData> charityList = CharityDetailFactory.getCharityList();
        PostData newPost = new CharityPostData(charityList.get(0).getIdentifier(),
                "Hello everyone,\nThank you for donating to Charity A this month. We're grateful for " +
                        "every donation we get.\n\nSincerely,\nThe Charity A Staff");
        POST_MAP.put(newPost.getPostIdentifier(), newPost);

        newPost = new CharityPostData(charityList.get(1).getIdentifier(), DEF_BODY_IMAGE_ICON_ID,
                "Hello everyone,\nCheck out our new photos from the Charity B volunteer event last Sunday. We appreciate " +
                        "the help as well as your continued support through donations.\n\nSincerely,\nThe Charity B Staff");

        newPost.getPostTime().set(Calendar.DAY_OF_MONTH, Math.max(1, newPost.getPostTime().get(Calendar.DAY_OF_MONTH) - 1));
        POST_MAP.put(newPost.getPostIdentifier(), newPost);

        newPost = new CharityPostData(charityList.get(2).getIdentifier(), DEF_BODY_IMAGE_ICON_ID,
                "Hello everyone,\nCheck out our new photos from the Charity A volunteer event last Saturday. We appreciate " +
                        "the help as well as your continued support through donations.\n\nSincerely,\nThe Charity A Staff");

        newPost.getPostTime().set(Calendar.DAY_OF_MONTH, Math.max(1, newPost.getPostTime().get(Calendar.DAY_OF_MONTH) - 1));
        POST_MAP.put(newPost.getPostIdentifier(), newPost);
    }

    public static PostData getPostById(int key){
        if(POST_MAP.containsKey(key)){
            return POST_MAP.get(key);
        }
        return null;
    }

    public static List<PostData> getAllPostsByAuthor(String authorDisplayName){
        Set<PostData> postsByAuthor = new TreeSet<>(POST_COMPARATOR);
        for(PostData p : POST_MAP.values()){
            if(p.getAuthorDisplayName().equals(authorDisplayName)){
                postsByAuthor.add(p);
            }
        }

        return new ArrayList<>(postsByAuthor);
    }

    public static int getAuthorDonationTotal(String authorDisplayName){
        List<PostData> authorPosts = getAllPostsByAuthor(authorDisplayName);
        int total = 0;
        for(PostData p : authorPosts){
            if(p instanceof DonationPostData)
                total += ((DonationPostData) p).getDonationAmountCents();
        }
        return total;
    }

    public static int getAuthorDonationTotalForRecipient(String authorDisplayName, String recipientDisplayName){
        List<DonationPostData> posts = getAllDonationsFromAuthorToRecipient(authorDisplayName, recipientDisplayName);
        int total = 0;
        for(DonationPostData p : posts){
            total += p.getDonationAmountCents();
        }
        return total;
    }

    public static int getDaysBetweenFirstAndLastPost(String authorDisplayName){
        List<PostData> posts = getAllPostsByAuthor(authorDisplayName);
        if(posts == null || posts.size() <= 0){
            return 0;
        }
        Calendar firstPostTime = posts.get(posts.size() - 1).getPostTime();
        Calendar lastPostTime = posts.get(0).getPostTime();

        Date firstDate = new Date(firstPostTime.get(Calendar.YEAR), firstPostTime.get(Calendar.MONTH), firstPostTime.get(Calendar.DAY_OF_MONTH));
        Date lastDate = new Date(lastPostTime.get(Calendar.YEAR), lastPostTime.get(Calendar.MONTH), lastPostTime.get(Calendar.DAY_OF_MONTH));
        long diff = lastDate.getTime() - firstDate.getTime();

        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static int getDaysBetweenFirstAndLastPost(String authorDisplayName, String recipientDisplayName){
        List<DonationPostData> posts = getAllDonationsFromAuthorToRecipient(authorDisplayName, recipientDisplayName);
        if(posts == null || posts.size() <= 0){
            return 0;
        }
        Calendar firstPostTime = posts.get(posts.size() - 1).getPostTime();
        Calendar lastPostTime = posts.get(0).getPostTime();

        Date firstDate = new Date(firstPostTime.get(Calendar.YEAR), firstPostTime.get(Calendar.MONTH), firstPostTime.get(Calendar.DAY_OF_MONTH));
        Date lastDate = new Date(lastPostTime.get(Calendar.YEAR), lastPostTime.get(Calendar.MONTH), lastPostTime.get(Calendar.DAY_OF_MONTH));
        long diff = lastDate.getTime() - firstDate.getTime();

        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static List<DonationPostData> getAllDonationsFromAuthorToRecipient(String authorDisplayName, String recipientDisplayName){
        Set<DonationPostData> posts = new TreeSet<>(POST_COMPARATOR);
        for(PostData p : POST_MAP.values()){
            if(p instanceof DonationPostData && p.getAuthorDisplayName().equals(authorDisplayName) &&
                    ((DonationPostData) p).getRecipientDisplayName().equals(recipientDisplayName)){
                posts.add((DonationPostData) p);
            }
        }
        return new ArrayList<DonationPostData>(posts);
    }

    public static float getUserDonationRatioForRecipient(String authorDisplayName, String recipientDisplayName){
        float donationTotal = getAuthorDonationTotal(authorDisplayName);
        float recipientTotal = getAuthorDonationTotalForRecipient(authorDisplayName, recipientDisplayName);

        if(donationTotal <= 0){
            return 0;
        }

        return recipientTotal/donationTotal;
    }

    public static void addPost(PostData postData){
        if(postData != null){
            POST_MAP.put(postData.getPostIdentifier(), postData);
        }
        lastUpdate = Calendar.getInstance();
    }

    public static List<PostData> getAllUserPosts(){
        Set<PostData> userPosts = new TreeSet<>(POST_COMPARATOR);

        for(PostData p : POST_MAP.values()){
            if(p.getAuthorDisplayName().equals(DonationPostData.USER_POST_NAME)){
                userPosts.add(p);
            }
        }

        return new ArrayList<>(userPosts);
    }

    public static List<PostData> getAllFriendPosts(){
        Set<PostData> friendPosts = new TreeSet<>(POST_COMPARATOR);

        for(PostData p : POST_MAP.values()){
            if(p.getPostType() == PostData.PostType.DONATION && !p.getAuthorDisplayName().equals(DonationPostData.USER_POST_NAME)){
                friendPosts.add(p);
            }
        }

        return new ArrayList<>(friendPosts);
    }

    public static List<PostData> getAllCharityPosts(){
        Set<PostData> charityPosts = new TreeSet<>(POST_COMPARATOR);

        for(PostData p : POST_MAP.values()){
            if(p.getPostType() == PostData.PostType.CHARITY){
                charityPosts.add(p);
            }
        }

        return new ArrayList<>(charityPosts);
    }
}
