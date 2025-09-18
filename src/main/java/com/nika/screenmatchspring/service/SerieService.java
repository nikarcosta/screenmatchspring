package com.nika.screenmatchspring.service;

import com.nika.screenmatchspring.model.DadosSerie;
import com.nika.screenmatchspring.model.DadosTemporada;
import com.nika.screenmatchspring.model.Episodio;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SerieService {
    private final ConsumoAPI consumoApi = new ConsumoAPI();
    private final ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=9c2bac17";

    public DadosSerie buscarSerie(String nomeSerie){
        var json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        return conversor.obterDados(json, DadosSerie.class);
    }

    public List<DadosTemporada> buscarTemporadas(String nomeSerie, int totalTemporadas){
        List<DadosTemporada> temporadas = new ArrayList<>();
        for(int i = 1; i < totalTemporadas; i++){
            var json = consumoApi.obterDados(
                    ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
            temporadas.add(conversor.obterDados(json, DadosTemporada.class));
        }
        return temporadas;
    }

    public List<Episodio> buscarEpisodios(List<DadosTemporada> temporadas){
        return temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d))
                ).collect(Collectors.toList());
    }

}
