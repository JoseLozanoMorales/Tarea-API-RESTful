package com.example.tareajsonapirestful;


import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private AutoCompleteTextView autocListaLugares;
    private ArrayList<String> lugares;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String url = "https://turismo.quevedoenlinea.gob.ec/lugar_turistico/json_getlistadoGridLT";

        autocListaLugares = findViewById(R.id.autocListaLugares);
        lugares = new ArrayList<>();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                lugares
        );
        autocListaLugares.setAdapter(adapter);

        autocListaLugares.setThreshold(1);

        CargarLugares(url);

        autocListaLugares.setOnItemClickListener((parent, view, position, id) -> {
            String lugar = parent.getItemAtPosition(position).toString();
            Toast.makeText(this, lugar, Toast.LENGTH_SHORT).show();
        });
    }
    private void CargarLugares(String url) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        JSONArray array = response.getJSONArray("data");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);

                            String nombre = obj.getString("nombre_lugar");
                            String categoria = obj.getString("categoria");

                            String item = categoria + ": " + nombre;

                            lugares.add(item);
                        }

                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error al intentar consumir la API", Toast.LENGTH_SHORT).show()
        );

        RequestQueue cola = Volley.newRequestQueue(this);
        cola.add(request);
    }
}