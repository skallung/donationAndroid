package com.example.psweeney.donationappandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.psweeney.donationappandroid.charity.CharityDetailData;
import com.example.psweeney.donationappandroid.charity.CharityDetailFactory;
import com.example.psweeney.donationappandroid.charity.CharityProfileAdapter;
import com.example.psweeney.donationappandroid.charity.CharityUserAdapter;
import com.example.psweeney.donationappandroid.feed.DonationPostData;
import com.example.psweeney.donationappandroid.feed.FeedPostAdapter;
import com.example.psweeney.donationappandroid.feed.PostContainer;
import com.example.psweeney.donationappandroid.feed.PostFactory;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.Calendar;

/**
 * Created by psweeney
 *
 * This activity is where the user will view the different sets of detailed data the app
 * has stored for a specific charity.
 *
 * There are three sections used to store this information, denoted by the CharityInfoList enum:
 *
 *      PROFILE -   contains the charity's contact information, display image, bio, and recent posts,
 *                  as well as controls for setting the charity as the user's auto-donate recipient
 *                  and making immediate one-time donations
 *
 *      DATA -      contains the charity's CharityNavigator rating and a pie chart detailing how
 *                  it spends its donations
 *
 *      USER -      contains a summary of the user's history with the charity being viewed as well
 *                  as a list of all past donations the user has made to the charity
 *
 */
public class CharityDetailActivity extends AppCompatActivity {
    public enum CharityInfoList{
        PROFILE, DATA, USER
    }


    private CharityInfoList _currentSelection = CharityInfoList.PROFILE;
    private CharityDetailData _data;
    private PostContainer _lastInteraction = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity_detail);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            finish();
            return;
        }

        try{
            _data = CharityDetailFactory.getCharityById(bundle.getInt(CharityDetailData.CHARITY_IDENTIFIER_KEY));
        } catch (NullPointerException e){
            finish();
            return;
        }

        if(_data == null){
            finish();
            return;
        }

        setTitle(_data.getDisplayName());

        final ListView profileInfoListView = (ListView) findViewById(R.id.listViewCharityProfileItems);
        profileInfoListView.setDivider(null);
        profileInfoListView.setDividerHeight(0);

        CharityProfileAdapter adapter = new CharityProfileAdapter(this, R.layout.feed_post_charity, _data);
        profileInfoListView.setAdapter(adapter);

        updateFeedSelection();
        generateDataContents();
        generateUserContents();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                _lastInteraction.updateViews();
            }
        }
    }

    public static void applyRatingToStarList(LinearLayout parent, Resources resources, float rating){
        if(parent == null || resources == null){
            return;
        }

        ImageView starIcon1 = (ImageView) parent.findViewById(R.id.imageViewStar1);
        ImageView starIcon2 = (ImageView) parent.findViewById(R.id.imageViewStar2);
        ImageView starIcon3 = (ImageView) parent.findViewById(R.id.imageViewStar3);
        ImageView starIcon4 = (ImageView) parent.findViewById(R.id.imageViewStar4);
        ImageView starIcon5 = (ImageView) parent.findViewById(R.id.imageViewStar5);

        Drawable halfStar = resources.getDrawable(R.drawable.ic_star_white_72pt_border_half_filled);
        Drawable filledStar = resources.getDrawable(R.drawable.ic_star_white_72pt_border_filled);

        int iconEffectiveRating = (int) Math.ceil(rating * 10);

        switch (iconEffectiveRating){
            case 0:
                starIcon1.setImageDrawable(filledStar);
                starIcon2.setImageDrawable(filledStar);
                starIcon3.setImageDrawable(filledStar);
                starIcon4.setImageDrawable(filledStar);
                starIcon5.setImageDrawable(filledStar);
                break;
            case 1:
                starIcon1.setImageDrawable(halfStar);
                starIcon2.setImageDrawable(filledStar);
                starIcon3.setImageDrawable(filledStar);
                starIcon4.setImageDrawable(filledStar);
                starIcon5.setImageDrawable(filledStar);
                break;
            case 2:
                starIcon2.setImageDrawable(filledStar);
                starIcon3.setImageDrawable(filledStar);
                starIcon4.setImageDrawable(filledStar);
                starIcon5.setImageDrawable(filledStar);
                break;
            case 3:
                starIcon2.setImageDrawable(halfStar);
                starIcon3.setImageDrawable(filledStar);
                starIcon4.setImageDrawable(filledStar);
                starIcon5.setImageDrawable(filledStar);
                break;
            case 4:
                starIcon3.setImageDrawable(filledStar);
                starIcon4.setImageDrawable(filledStar);
                starIcon5.setImageDrawable(filledStar);
                break;
            case 5:
                starIcon3.setImageDrawable(halfStar);
                starIcon4.setImageDrawable(filledStar);
                starIcon5.setImageDrawable(filledStar);
                break;
            case 6:
                starIcon4.setImageDrawable(filledStar);
                starIcon5.setImageDrawable(filledStar);
                break;
            case 7:
                starIcon4.setImageDrawable(halfStar);
                starIcon5.setImageDrawable(filledStar);
                break;
            case 8:
                starIcon5.setImageDrawable(filledStar);
                break;
            case 9:
                starIcon5.setImageDrawable(halfStar);
                break;
        }
    }

    private void generateDataContents(){
        LinearLayout dataItemContainer1 = (LinearLayout) findViewById(R.id.containerCharityDataItem1);

        applyRatingToStarList(dataItemContainer1, getResources(), _data.getRating());

        TextView fractionText = (TextView) dataItemContainer1.findViewById(R.id.textViewRatingFraction);

        fractionText.setText(_data.getRatingDisplayString(5));

        LinearLayout breakdownContainer = (LinearLayout) findViewById(R.id.containerCharityDataItem2);
        TextView breakdownTitle = (TextView) breakdownContainer.findViewById(R.id.textViewBreakdownTitle);
        String breakdownTitleString = "This is how " + _data.getDisplayName() + " spends its donations:";
        breakdownTitle.setText(breakdownTitleString);

        PieData pieData = _data.getPieData(getApplicationContext());

        pieData.setValueFormatter(new PercentFormatter());

        pieData.setValueTextColor(getResources().getColor(R.color.colorPrimaryDark));

        PieChart pieChart = new PieChart(getApplicationContext());

        pieChart.setHoleRadius(60);
        pieChart.setTransparentCircleRadius(65);

        pieChart.setDescription("");
        pieChart.getLegend().setEnabled(false);

        pieChart.setClickable(false);

        FrameLayout pieChartContainer = (FrameLayout) breakdownContainer.findViewById(R.id.frameLayoutPieChartContainer);

        float valueTextSize = getResources().getDimension(R.dimen.charity_pie_chart_text_size) / getResources().getDisplayMetrics().density;

        pieData.setValueTextSize(valueTextSize);
        pieChart.setData(pieData);
        pieChartContainer.addView(pieChart);
    }

    private void generateUserContents(){
        final ListView userListView = (ListView) findViewById(R.id.listViewCharityUserItems);
        userListView.setDivider(null);
        userListView.setDividerHeight(0);

        CharityUserAdapter adapter = new CharityUserAdapter(this, R.layout.charity_detail_user_history, _data.getDisplayName());
        userListView.setAdapter(adapter);
    }

    private void updateFeedSelection(){
        TextView profileLabel = (TextView) findViewById(R.id.textViewCharityProfileLabel);
        TextView dataLabel = (TextView) findViewById(R.id.textViewCharityDataLabel);
        TextView userLabel = (TextView) findViewById(R.id.textViewCharityUserLabel);

        View profileItems = findViewById(R.id.listViewCharityProfileItems);
        View dataItems = findViewById(R.id.linearLayoutCharityDataItems);
        View userItems = findViewById(R.id.listViewCharityUserItems);


        switch (_currentSelection){
            case PROFILE:
                profileLabel.setTextColor(getResources().getColor(R.color.colorAccent));
                profileLabel.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                dataLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
                dataLabel.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                userLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
                userLabel.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                profileItems.setVisibility(View.VISIBLE);
                dataItems.setVisibility(View.GONE);
                userItems.setVisibility(View.GONE);

                break;
            case DATA:
                dataLabel.setTextColor(getResources().getColor(R.color.colorAccent));
                dataLabel.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                profileLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
                profileLabel.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                userLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
                userLabel.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                profileItems.setVisibility(View.GONE);
                dataItems.setVisibility(View.VISIBLE);
                userItems.setVisibility(View.GONE);

                break;
            case USER:
                userLabel.setTextColor(getResources().getColor(R.color.colorAccent));
                userLabel.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                profileLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
                profileLabel.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                dataLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
                dataLabel.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                profileItems.setVisibility(View.GONE);
                dataItems.setVisibility(View.GONE);
                userItems.setVisibility(View.VISIBLE);

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

        bundle.putBoolean(FeedActivity.COMMENT_FIELD_SELECTED_KEY, commentRequested);

        singlePostIntent.putExtras(bundle);
        startActivityForResult(singlePostIntent, 1);
    }

    public void onClickCharityIcon(View v){
        Animation.defaultButtonAnimation(v);
    }

    public void onButtonClickCharityProfile(View v){
        if(_currentSelection != CharityInfoList.PROFILE){
            _currentSelection = CharityInfoList.PROFILE;
            updateFeedSelection();
        }
    }

    public void onButtonClickCharityData(View v){
        if(_currentSelection != CharityInfoList.DATA){
            _currentSelection = CharityInfoList.DATA;
            updateFeedSelection();
        }
    }

    public void onButtonClickCharityUser(View v){
        if(_currentSelection != CharityInfoList.USER){
            _currentSelection = CharityInfoList.USER;
            updateFeedSelection();
        }
    }

    public void onClickAutoDonateButton(View v){
        if(v == null || _data.isCurrentRecipient()){
            return;
        }
        View parentContainer = (View) v.getParent();
        if(parentContainer == null){
            return;
        }

        Animation.defaultButtonAnimation(parentContainer);

        CharityDetailFactory.setUserAutoDonateCharity(_data.getIdentifier());
        CharityProfileAdapter.updateAutoDonateViews(parentContainer, _data, getResources());
        parentContainer.invalidate();
    }

    public void onClickDonateNowButton(View v){
        if(v == null){
            return;
        }

        View parentContainer = (View) v.getParent();
        Animation.defaultButtonAnimation(parentContainer);
        View controlContainer = findViewById(R.id.donateNowControlContainer);
        if(controlContainer == null){
            return;
        }

        if(controlContainer.getVisibility() != View.VISIBLE)
            controlContainer.setVisibility(View.VISIBLE);
        else
            controlContainer.setVisibility(View.GONE);
    }

    public void onClickDonateNowSubmit(View v){
        Animation.defaultButtonAnimation(v);

        final View container = findViewById(R.id.donateNowControlContainer);
        final EditText donateNowAmountField = (EditText) container.findViewById(R.id.editTextDonateNowAmount);
        if(donateNowAmountField == null){
            return;
        }

        String donationAmountString = donateNowAmountField.getText().toString().replaceAll("[^\\d.]+", "");
        final int donationAmountCents;
        try{
            donationAmountCents = (int) (Float.parseFloat(donationAmountString) * 100);
        } catch (NumberFormatException e){
            Toast.makeText(this, "Please enter a valid donation amount", Toast.LENGTH_SHORT).show();
            return;
        }

        final String donationAmountDisplayString = DonationPostData.getDonationAmountDisplayString(donationAmountCents);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to donate " + donationAmountDisplayString + " to " + _data.getDisplayName() + "?");

        builder.setPositiveButton(getResources().getString(R.string.charity_submit_donation_label), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DonationPostData post = new DonationPostData(PostFactory.DEF_USER_ICON_ID, DonationPostData.USER_POST_NAME, _data.getIdentifier(),
                        Calendar.getInstance(), donationAmountCents, 0, false, null);
                PostFactory.addPost(post);
                donateNowAmountField.setText("");
                container.setVisibility(View.GONE);
                Toast.makeText(CharityDetailActivity.this, "You donated " + donationAmountDisplayString + " to " +
                        _data.getDisplayName() + "!", Toast.LENGTH_SHORT).show();
                ListView userDonations = (ListView) findViewById(R.id.listViewCharityUserItems);
                if(userDonations != null){
                    CharityUserAdapter adapter = (CharityUserAdapter) userDonations.getAdapter();
                    adapter.refresh();
                }

            }
        });

        builder.setNeutralButton(getResources().getString(R.string.charity_cancel_donation_label), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(CharityDetailActivity.this, "Donation cancelled", Toast.LENGTH_SHORT).show();
                donateNowAmountField.clearFocus();
            }
        });

        AlertDialog alertDialog = builder.create();

        alertDialog.show();

        Button posButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if(posButton != null) {
            posButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            posButton.setGravity(Gravity.CENTER);
        }

        Button negButton = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        if(negButton != null) {
            negButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            negButton.setGravity(Gravity.CENTER);
        }
    }

    public void onClickDonateNowCancel(View v){
        Animation.defaultButtonAnimation(v);

        View container = findViewById(R.id.donateNowControlContainer);
        EditText donateNowAmountField = (EditText) container.findViewById(R.id.editTextDonateNowAmount);
        if(container == null || donateNowAmountField == null){
            return;
        }

        donateNowAmountField.setText("");
        container.setVisibility(View.GONE);
    }

    public void onButtonClickPostBody(View v){
        Animation.defaultButtonAnimation(v);
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

        Animation.defaultButtonAnimation(subContainer);
    }

    public void onButtonClickComment(View v){
        if(v.getId() != R.id.frameLayoutCommentContainer) {
            return;
        }

        View subContainer = v.findViewById(R.id.commentSubContainer);
        Animation.defaultButtonAnimation(subContainer);

        viewPost(v, true);
    }

    public void onButtonClickLoadMore(View v){
        if(_currentSelection == CharityInfoList.DATA){
            return;
        }

        ListView listView;
        if(_currentSelection == CharityInfoList.PROFILE){
            listView = (ListView) findViewById(R.id.listViewCharityProfileItems);
            CharityProfileAdapter adapter = (CharityProfileAdapter) listView.getAdapter();
            adapter.incrementNumPosts();
        } else {
            listView = (ListView) findViewById(R.id.listViewCharityUserItems);
            CharityUserAdapter adapter = (CharityUserAdapter) listView.getAdapter();
            adapter.incrementNumPosts();
        }

        listView.invalidate();
    }
}
