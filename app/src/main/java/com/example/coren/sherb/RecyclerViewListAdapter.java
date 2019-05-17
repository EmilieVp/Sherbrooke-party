package com.example.coren.sherb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewListAdapter extends RecyclerView.Adapter<RecyclerViewListAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Party> partieList = new ArrayList<>();

    public RecyclerViewListAdapter(Context context, ArrayList<Party> myDataset) {
        this.context = context;
        partieList = myDataset;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = R.layout.layout_listpartie;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layout, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Party partie = partieList.get(position);
        holder.display(partie);

    }

    @Override
    public int getItemCount() {
        return partieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView titre, description, horaire;

        private Party currentParty;

        public ViewHolder(View itemView) {
            super(itemView);

            titre = ((TextView) itemView.findViewById(R.id.titre));
            description = ((TextView) itemView.findViewById(R.id.description));
            horaire = ((TextView) itemView.findViewById(R.id.horaire));
           // prix = ((TextView) itemView.findViewById(R.id.prix));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("party", currentParty);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }

        public void display(Party partie) {
            currentParty = partie;

            titre.setText(partie.getTitre());
            description.setText(partie.getDescription());
            horaire.setText(partie.getSchedule());
           // prix.setText(partie.getPrice());

        }
    }


}
