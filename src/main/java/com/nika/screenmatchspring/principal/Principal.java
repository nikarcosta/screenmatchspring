package com.nika.screenmatchspring.principal;

import com.nika.screenmatchspring.model.DadosSerie;
import com.nika.screenmatchspring.service.RelatorioService;
import com.nika.screenmatchspring.service.SerieService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private SerieService serieService = new SerieService();
    private RelatorioService relatorioService = new RelatorioService();

    public void exibeMenu(){
        System.out.println("Digite o nome da série para busca:");
        var nomeSerie = leitura.nextLine();

        DadosSerie dadosSerie = serieService.buscarSerie(nomeSerie);
        System.out.println(dadosSerie);

        var temporadas = serieService.buscarTemporadas(nomeSerie, dadosSerie.totalTemporadas());
        var episodios = serieService.buscarEpisodios(temporadas);

        System.out.println("\nTop 5 episódios:");
        relatorioService.topEpisodios(episodios, 5).forEach(System.out::println);

        System.out.println("Digite um trecho do título do episódio:");
        var trechoTitulo = leitura.nextLine();
        relatorioService.buscarPorTitulo(episodios, trechoTitulo)
                .ifPresentOrElse(
                        e -> System.out.println("Episódio encontrado! Temporada: " + e.getTemporada()),
                        () -> System.out.println("Episódio não encontrado!")
                );

        System.out.println("Entre um ano para buscar os episódios a partir daquela data:");
        var ano = leitura.nextInt(); leitura.nextLine();
        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        relatorioService.episodiosDepoisDe(episodios, dataBusca)
                .forEach(e -> System.out.println("Temporada: " + e.getTemporada()
                        + " | Episódio: " + e.getTitulo()
                        + " | Data de lançamento: " + e.getDataLancamento().format(formatador)));

        var est = relatorioService.estatisticas(episodios);
        System.out.println("Média: " + est.getAverage());
        System.out.println("Melhor episódio: " + est.getMax());
        System.out.println("Pior episódio: " + est.getMin());
        System.out.println("Quantidade: " + est.getCount());

    }
}
