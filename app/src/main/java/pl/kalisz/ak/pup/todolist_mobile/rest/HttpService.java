package pl.kalisz.ak.pup.todolist_mobile.rest;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import pl.kalisz.ak.pup.todolist_mobile.TestActivity;
import pl.kalisz.ak.pup.todolist_mobile.rest.exceptions.BadHttpRequestException;

/**
 * Klasa służąca do obsługi requestów HTTP z zewnętrznej aplikacji webowej todolist.
 * @author Maciej Gogulski
 */
public class HttpService {

    /**
     * Pole przechowujące adres url aplikacji webowej todolist.
     */
    private String webAppUrl = "http://10.0.2.2:8000";


    /**
     * Metoda służąca do wysyłania żądania na podaną ścieżkę w aplikacji webowej todolist.
     * @param route Endpoint, na który chcemy wysłać request do aplikacji. Musi zaczynać się od "/".
     * @param method Metoda, którą chcemy wysłać żądanie. Wartości przypisane odpowiednim metodom są zdefiniowane w klasie "Request.Method".
     * @param context Bieżąca aktywność aplikacji - zazwyczaj będzie tu przekazywane "this".
     */
    public void sendRequest(String route, int method, Context context) throws BadHttpRequestException {
        // Utworzenie kolejki żądań.
        RequestQueue queue = Volley.newRequestQueue(context);

        if (!route.startsWith("/")) {
            throw new BadHttpRequestException("Niepoprawny routing: " + webAppUrl + route);
        }

        StringRequest stringRequest = new StringRequest(method, webAppUrl + route,
                response -> {
                    // Miejsce na przetworzenie odpowiedzi JSON
                    Intent intent = new Intent(context, TestActivity.class);
                    intent.putExtra("response", response);
                    context.startActivity(intent);
                },
                error -> {
                    Log.d(TAG, "sendRequest: " + error.toString());
                    // Obsłużenie błędu żądania
//                    Intent intent = new Intent(context, TestActivity.class);
//                    intent.putExtra("response", error.toString());
//                    context.startActivity(intent);
                }
        );
        queue.add(stringRequest);
    }
}