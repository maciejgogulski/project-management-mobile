package pl.kalisz.ak.pup.todolist_mobile.rest;

import android.content.Context;
import android.content.Intent;

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
    private String webAppUrl = "http://localhost:8080";


    /**
     * Metoda służąca do wysyłania żądania na podaną ścieżkę w aplikacji webowej todolist.
     * @param route Endpoint, na który chcemy wysłać request do aplikacji. Musi zaczynać się od "/".
     * @param method Metoda, którą chcemy wysłać żądanie. Wartości przypisane odpowiednim metodom są zdefiniowane w klasie "Request.Method".
     * @param context Bieżący stan aplikacji - zazwyczaj będzie tu przekazywane "this".
     */
    public void sendRequest(String route, int method, Context context) throws BadHttpRequestException {
        // Utworzenie kolejki żądań.
        RequestQueue queue = Volley.newRequestQueue(context);

        if (!route.startsWith("/")) {
            throw new BadHttpRequestException("Niepoprawny routing: " + webAppUrl + route);
        }

        StringRequest stringRequest = new StringRequest(method, "https://api.chucknorris.io/jokes/random",
                response -> {
                    // Miejsce na przetworzenie odpowiedzi JSON
                    Intent intent = new Intent(context, TestActivity.class);
                    intent.putExtra("response", response);
                    context.startActivity(intent);
                },
                error -> {
                    // Obsłużenie błędu żądania
                }
        );

        queue.add(stringRequest);
    }
}