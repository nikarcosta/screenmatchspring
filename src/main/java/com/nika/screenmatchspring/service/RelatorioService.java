package com.nika.screenmatchspring.service;

import com.nika.screenmatchspring.model.Episodio;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RelatorioService {
    public List<Episodio> topEpisodios(List<Episodio> episodios, int limite){
        return episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .sorted(Comparator.comparing(Episodio::getAvaliacao).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    public Optional<Episodio> buscarPorTitulo(List<Episodio> episodios, String trechoTitulo) {
        return episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();
    }

    public List<Episodio> episodiosDepoisDe(List<Episodio> episodios, LocalDate data) {
        return episodios.stream()
                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(data))
                .collect(Collectors.toList());
    }

    public DoubleSummaryStatistics estatisticas(List<Episodio> episodios) {
        return episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
    }
}
