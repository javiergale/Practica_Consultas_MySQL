package com.example.buscadorrecyclerviewmysql;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
        EditText etBuscador;
        RecyclerView rvLista;
        AdaptadorUsuarios adaptador;
        List<Usuario> ListaUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etBuscador = findViewById(R.id.etBuscador);
        etBuscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    filtar(s.toString());
            }
        });
        rvLista = findViewById(R.id.rvLista);
        rvLista.setLayoutManager(new GridLayoutManager(this,1));

        ListaUsuarios = new ArrayList<>();

        obtenerUsuarios();

        adaptador = new AdaptadorUsuarios(MainActivity. this, ListaUsuarios);
        rvLista.setAdapter(adaptador);
    }
    public void obtenerUsuarios() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.URL_USUARIOS),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("Usuarios");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                ListaUsuarios.add(
                                        new Usuario(
                                                jsonObject.getString("idUsuario"),
                                                jsonObject.getString("nombre"),
                                                jsonObject.getString("telefono"),
                                                jsonObject.getString("email"),
                                                jsonObject.getString("usuario"),
                                                jsonObject.getString("contrasena")
                                        )
                                );
                            }
                            adaptador = new AdaptadorUsuarios(MainActivity. this, ListaUsuarios);
                            rvLista.setAdapter(adaptador);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }
        );
        requestQueue.add(stringRequest);

    }
    public void filtar(String texto){
        ArrayList<Usuario> filtrarLista = new ArrayList<>();
        for (Usuario usuario: ListaUsuarios){
            if(usuario.getNombre().toLowerCase().contains(texto.toLowerCase())){
                filtrarLista.add(usuario);
            }
        }
        adaptador.filtrar(filtrarLista);
    }
}
