package org.example;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner read = new Scanner(System.in);
        System.out.println("Gostaria de buscar o clima por");
        System.out.println("#1 Nome do pais");
        System.out.println("#2 Nome da cidade");
        String opc = read.nextLine();

        String endpoint = "https://api.openweathermap.org/data/2.5/weather?appid=d131fdbf163a47134e383510eb2d0b50";
        String query = "";

        try {
            switch (opc) {
                case "1":
                    System.out.println("Digite o nome do país");
                    String nomePais = read.nextLine().replace(" ", "%20");
                    query = "q=" + nomePais;
                    break;
                case "2":
                    System.out.println("Digite o nome da cidade");
                    String nomeCidade = read.nextLine().replace(" ", "%20");
                    query = "q=" + nomeCidade;
                    break;
                default:
                    System.out.println("Opção inválida.");
                    return;
            }

            URL url = new URL(endpoint + "&" + query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");


            BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String linha;
            StringBuilder sb = new StringBuilder();

            while ((linha = bf.readLine()) != null) {
                sb.append(linha);
            }
            bf.close();

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(sb.toString());

            JSONObject temperatura = (JSONObject) json.get("main");
            double tempKelvin = (double) temperatura.get("temp");
            double tempCelsius = tempKelvin - 273.15;
            double tempMaxCelsius = (double) temperatura.get("temp_max") - 273.15;
            double tempMinCelsius = (double) temperatura.get("temp_min") - 273.15;

            System.out.printf("Temperatura: %.2f °C\n", tempCelsius);
            System.out.printf("Temperatura Máxima: %.2f °C\n", tempMaxCelsius);
            System.out.printf("Temperatura Mínima: %.2f °C\n", tempMinCelsius);

            JSONObject pais = (JSONObject) json.get("sys");
            System.out.println("País: " + pais.get("country"));
            System.out.println("Cidade: " + json.get("name"));

        } catch (Exception e) {
            System.out.println("Erro de exceção encontrado!");
        }
    }
}
