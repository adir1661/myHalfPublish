package com.myhalf.controller.navigation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.myhalf.R;
import com.myhalf.controller.tools.Storage;
import com.myhalf.model.backend.Finals;
import com.myhalf.model.entities.Enums;
import com.myhalf.model.entities.UserSeeker;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AdapterRecycleView extends RecyclerView.Adapter<AdapterRecycleView.ViewHolder>
        implements Filterable
{
    //fields
    private static final int SHARE_CODE = 0;
    private List<UserSeeker> itemList = new ArrayList<>();
    private List<UserSeeker> filterList = new ArrayList<>();
    Activity mContext;
    long delay = 0;
    boolean animatioFlag = true;

    //C-tor
    public AdapterRecycleView(List<UserSeeker> filteredList, Context mContext) {
        this.itemList.addAll(filteredList);
        this.mContext = (Activity) mContext;
        filterList.addAll(itemList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_result, parent, false);
        return new AdapterRecycleView.ViewHolder(itemView);
    }

    //@SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final UserSeeker userSeeker = filterList.get(position);

        Storage.getFromStorage(mContext,Finals.FireBase.storage.MAIN_PICTURE,holder.ivMainPicture,userSeeker);

        holder.tvName.setText(bigLetterFirst(userSeeker.getAboutMe().getName()));
        holder.tvAge.setText(userSeeker.getAboutMe().getBirthday().findAge() + "");
        if (userSeeker.getAboutMe().getStatus() != null)
            holder.tvStatus.setText(fixLinesAndSize(userSeeker.getAboutMe().getStatus().toString()));
        else{
            holder.tvStatus.setVisibility(View.INVISIBLE);
        }
        if (!userSeeker.getAboutMe().getView().isEmpty()) {
            String string = fixLinesAndSize(userSeeker.getAboutMe().getView().get(0));
            holder.tvView.setText(string);
        } else {
            holder.tvView.setVisibility(View.INVISIBLE);
        }
        holder.tvCity.setText(userSeeker.getAboutMe().getCity());
        if (userSeeker.getAboutMe().getFreeDescription() != null && userSeeker.getAboutMe().getFreeDescription()!= "Null" )
            holder.tvFreeDescription.setText(fixLinesAndSize(userSeeker.getAboutMe().getFreeDescription()));
        else {
            holder.tvFreeDescription.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Move to fragment full profile getting this user profile....
                FragmentTransaction ftMoveToFullProfile = mContext.getFragmentManager().beginTransaction();
                ftMoveToFullProfile.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                FullProfile fragmentProfile = new FullProfile();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Finals.App.USER_SEEKER, userSeeker);
                fragmentProfile.setArguments(bundle);
                ftMoveToFullProfile.replace(R.id.NavigationDrawContainer, fragmentProfile);
                ftMoveToFullProfile.addToBackStack(null);
                ftMoveToFullProfile.commit();
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                // Menu text
                final String[] items = {"Delete", "Share", "Forward", "Popular"};

                // Menu Icons in Drawable
                final Drawable[] item_icons = {
                        mContext.getResources().getDrawable(R.drawable.ic_delete),
                        mContext.getResources().getDrawable(R.drawable.ic_share),
                        mContext.getResources().getDrawable(R.drawable.ic_forward),
                        mContext.getResources().getDrawable(R.drawable.ic_archive),
                };

                ListAdapter adapter = new ArrayAdapter<String>(mContext.getApplicationContext(), R.layout.custom_menu_dialog, items) {
                    ViewHolder holder;

                    class ViewHolder {
                        ImageView icon;
                        TextView title;
                    }

                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LayoutInflater inflater = (LayoutInflater)mContext.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        if (convertView == null) {
                            convertView = inflater.inflate(R.layout.custom_menu_dialog, null);
                            holder = new ViewHolder();
                            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
                            holder.title = (TextView) convertView.findViewById(R.id.title);
                            convertView.setTag(holder);
                        } else {
                            // view already defined, retrieve view holder
                            holder = (ViewHolder) convertView.getTag();
                        }

                        holder.title.setText(items[position]);
                        holder.icon.setImageDrawable(item_icons[position]);
                        return convertView;
                    }
                };


                AlertDialog.Builder menu_dialog = new AlertDialog.Builder(mContext);

                menu_dialog.setTitle("Select Action");
                menu_dialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        //Toast.makeText(WebViewActivity.this, "You selected: " + items[item], Toast.LENGTH_LONG).show();
                        switch (item) {
                            case 0:
                                on_delete(holder,position);
                                break;
                            case 1: // HOW TO
                                on_share(userSeeker);
                                break;
                            case 2:
                                on_forward();
                                break;
                            case 3: // ABOUT
                                on_popular();
                                break;
                            default: // Do Case 0
                                break;
                        }
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = menu_dialog.create();
                alert.show();
                return true;
            }
        });





        if (userSeeker.getAboutMe().getGender() == Enums.Gender.FEMALE)//TODO: delete this "if" after implementing picture...
            holder.ivMainPicture.setImageResource(R.drawable.student_female);
        else
            holder.ivMainPicture.setImageResource(R.drawable.student);
        if (animatioFlag) {
            final View view = holder.itemView;
            view.setVisibility(View.INVISIBLE);
            ObjectAnimator animation;
            if (position % 2 == 0)
                animation = ObjectAnimator.ofFloat(view, "translationX", -300f, 0f);
            else
                animation = ObjectAnimator.ofFloat(view, "translationX", 300f, 0f);
            animation.setDuration(250);
            delay=0;
            animation.setStartDelay(delay);
            if (delay < 600)
                delay += 100;
            animation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    view.setVisibility(View.VISIBLE);
                }
            });
            animation.start();
        }else {
            scheduleFlagChange();
        }
    }

    private void scheduleFlagChange() {
        Timer timer = new Timer();
        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                animatioFlag = true;
                            }
                        });
                    }
                },
                1500
        );
    }

    public static String fixLinesAndSize(@NonNull String string) {
        if (string!= null) {
            string = bigLetterFirst(string);
            string = string.replaceAll("_", " ");
        }
        return string;
    }

    private void on_forward() {
    }

    private void on_share(UserSeeker userSeeker) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hi, what's up? I send you properties of girl that I think could match you, from the application 'My Half': \n\n"
                                                    + "Name: " + userSeeker.getAboutMe().getName() + "\n\n"
                                                    + "Age: " + userSeeker.getAboutMe().getBirthday().findAge()+ "\n\n"
                                                    + "Status: " + userSeeker.getAboutMe().getStatus().toString() + "\n\n"
                                                    + "Migzar: " +userSeeker.getAboutMe().getView().toString() + "\n\n"
                                                    + "City: " + userSeeker.getAboutMe().getCity() );

        sendIntent.setType("text/plain");
        mContext.startActivity(sendIntent);
    }

    private void on_delete(final ViewHolder holder, final int position) {
        ObjectAnimator animation;
        animation = ObjectAnimator.ofFloat(holder.itemView, "alpha", 1f,0f);
        animation.setDuration(1000);
        animation.start();
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                filterList.remove(position);
                holder.itemView.setVisibility(View.GONE);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, filterList.size());
                animatioFlag = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }


        });


    }
    private void on_popular() {
    }

    private static String bigLetterFirst(String userName) {

        if (userName!= null&&userName.length() >= 1)
            return userName.substring(0, 1).toUpperCase() + userName.substring(1).toLowerCase();
        else return userName;
    }


    private String arrayToString(String[] strings) {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            string.append(strings[i]);
            if (i < strings.length - 1) {

                string.append(" ");
            }
        }
        return string.toString();
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    //---------------------------- filter implementation -----------------------------------------------
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                filterList.clear();
                for (UserSeeker userSeeker : itemList) {
                    if (userSeeker.getAboutMe().getName().contains(constraint))// condtition here: if the word in the searchView included in userseeker name
                        filterList.add(userSeeker);
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
    }




    //--------- class of viewHolder ------------------
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvStatus;
        TextView tvView;
        TextView tvCity;
        TextView tvFreeDescription;
        TextView tvAge;
        ImageView ivMainPicture;
        Button likeButton;
        Button delete;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvAge = (TextView) itemView.findViewById(R.id.tvAge);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
            tvCity = (TextView) itemView.findViewById(R.id.tvCity);
            tvView = (TextView) itemView.findViewById(R.id.tvView);
            tvFreeDescription = (TextView) itemView.findViewById(R.id.tvFreeDescription);
            ivMainPicture = (ImageView) itemView.findViewById(R.id.ivMainPicture);
//            likeButton=(Button)itemView.findViewById(R.id.like);
//            delete=(Button)itemView.findViewById(R.id.delete);

        }
    }
}
