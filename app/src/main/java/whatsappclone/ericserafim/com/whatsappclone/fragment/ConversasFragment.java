package whatsappclone.ericserafim.com.whatsappclone.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import whatsappclone.ericserafim.com.whatsappclone.R;
import whatsappclone.ericserafim.com.whatsappclone.activity.ConversaActivity;
import whatsappclone.ericserafim.com.whatsappclone.adapter.ConversaAdapter;
import whatsappclone.ericserafim.com.whatsappclone.application.ConfiguracaoFirebase;
import whatsappclone.ericserafim.com.whatsappclone.helper.Base64Custom;
import whatsappclone.ericserafim.com.whatsappclone.helper.Preferencias;
import whatsappclone.ericserafim.com.whatsappclone.model.Contato;
import whatsappclone.ericserafim.com.whatsappclone.model.Conversa;

public class ConversasFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter<Conversa> arrayAdapter;
    private ArrayList<Conversa> conversas;
    private ValueEventListener valueEventListener;
    private DatabaseReference dataBase;

    public ConversasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);

        //Monta View
        conversas = new ArrayList<>();
        listView = (ListView) view.findViewById(R.id.lv_conversas);
        arrayAdapter = new ConversaAdapter(getActivity(), conversas);
        listView.setAdapter(arrayAdapter);

        //Recuperar conversas do FireBase
        Preferencias preferencias = new Preferencias(getActivity());
        String idUsuarioLogado = preferencias.getIdentificador();

        dataBase = ConfiguracaoFirebase.getDatabase()
                                        .child("conversas")
                                        .child(idUsuarioLogado);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                conversas.clear();

                for (DataSnapshot dados: dataSnapshot.getChildren()) {
                    Conversa conversa = dados.getValue(Conversa.class);
                    conversas.add(conversa);
                }

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        dataBase.addValueEventListener(valueEventListener);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Conversa conversa = conversas.get(position);
                Contato destinatario = new Contato();

                destinatario.setNome(conversa.getNome());
                destinatario.setEmail(Base64Custom.decodificarBase64(conversa.getIdUsuario()));
                destinatario.setIdentificadorUsuario(conversa.getIdUsuario());

                Intent intent = new Intent(getActivity(), ConversaActivity.class);
                intent.putExtra("destinatario", destinatario);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        dataBase.removeEventListener(valueEventListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        dataBase.addValueEventListener(valueEventListener);
    }
}
