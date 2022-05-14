package com.example.usermusicplayer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.mViewHolder> {

    Context context;
    ArrayList<String> name;
    ArrayList<String> artist;
    ArrayList<String> dur;
    ArrayList<String> id;
    ArrayList<String> data;

    File begone;

    public MusicAdapter(Context context, ArrayList<String> name, ArrayList<String> artist,
                        ArrayList<String> dur, ArrayList<String> id, ArrayList<String> data) {
        this.context = context;
        this.name = name;
        this.artist = artist;
        this.dur = dur;
        this.id = id;
        this.data = data;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        View v = inf.inflate(R.layout.card_single_song, parent, false);
        return new mViewHolder(v).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        holder.txtName.setText(name.get(position));
        holder.txtArt.setText(artist.get(position));
        holder.txtDur.setText(dur.get(position));

        holder.CL.setOnClickListener(view -> {
            Intent fire = new Intent(context, PlayerMusic.class);
            fire.putExtra("name", name);
            fire.putExtra("art", artist);
            fire.putExtra("dur", dur);
            fire.putExtra("id", id);
            fire.putExtra("data", data);
            fire.putExtra("position", position);
            context.startActivity(fire);
        });

    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class mViewHolder extends RecyclerView.ViewHolder {

        private MusicAdapter dododo;

        TextView txtName, txtArt, txtDur;
        ConstraintLayout CL;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.cardSongName);
            txtArt = itemView.findViewById(R.id.cardArtist);
            txtDur = itemView.findViewById(R.id.cardDuration);
            CL = itemView.findViewById(R.id.constrainedLayout);

            itemView.findViewById(R.id.deleteSong).setOnClickListener(view -> {

                begone = new File(dododo.data.get(getAdapterPosition()));
                if (begone.exists()){
                    if(begone.delete()){
                        Toast.makeText(context, "DELETED", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "File couldn't be deleted",
                                Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(context, "file not found", Toast.LENGTH_SHORT).show();

                dododo.name.remove(getAdapterPosition());
                dododo.artist.remove(getAdapterPosition());
                dododo.id.remove(getAdapterPosition());
                dododo.data.remove(getAdapterPosition());
                dododo.dur.remove(getAdapterPosition());
                dododo.notifyItemRemoved(getAdapterPosition());
            });
        }

        public mViewHolder linkAdapter(MusicAdapter dododo){
            this.dododo = dododo;
            return this;
        }
    }
}
