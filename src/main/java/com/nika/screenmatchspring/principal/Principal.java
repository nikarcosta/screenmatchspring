package com.nika.screenmatchspring.principal;

import com.nika.screenmatchspring.model.DadosSerie;
import com.nika.screenmatchspring.model.DadosTemporada;
import com.nika.screenmatchspring.model.Episodio;
import com.nika.screenmatchspring.service.RelatorioService;
import com.nika.screenmatchspring.service.SerieService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private SerieService serieService = new SerieService();
    private RelatorioService relatorioService = new RelatorioService();

    private DadosSerie dadosSerie;
    private List<DadosTemporada> temporadas = new ArrayList<>();
    private List<Episodio> episodios = new ArrayList<>();

    public void exibeMenu(){
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n=== Menu ===");
            System.out.println("1 - Buscar série");
            System.out.println("2 - Listar episódios");
            System.out.println("3 - Top 5 episódios");
            System.out.println("4 - Buscar episódio por título");
            System.out.println("5 - Buscar episódios após ano");
            System.out.println("6 - Estatísticas da série");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1 -> buscarSerie();
                case 2 -> listarEpisodios();
                case 3 -> topEpisodios();
                case 4 -> buscarPorTitulo();
                case 5 -> buscarPorAno();
                case 6 -> mostrarEstatisticas();
                case 0 -> System.out.println("Encerrando...");
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void buscarSerie() {
        System.out.print("Digite o nome da série: ");
        var nomeSerie = leitura.nextLine();

        dadosSerie = serieService.buscarSerie(nomeSerie);
        System.out.println(dadosSerie);

        temporadas = serieService.buscarTemporadas(nomeSerie, dadosSerie.totalTemporadas());
        episodios = serieService.buscarEpisodios(temporadas);

        System.out.println("Série carregada com sucesso!");
    }

    private void listarEpisodios() {
        if (episodios.isEmpty()) {
            System.out.println("Nenhuma série carregada. Busque primeiro.");
            return;
        }
        episodios.forEach(System.out::println);
    }

    private void topEpisodios() {
        if (episodios.isEmpty()) {
            System.out.println("Nenhuma série carregada. Busque primeiro.");
            return;
        }
        relatorioService.topEpisodios(episodios, 5).forEach(System.out::println);
    }

    private void buscarPorTitulo() {
        if (episodios.isEmpty()) {
            System.out.println("Nenhuma série carregada. Busque primeiro.");
            return;
        }
        System.out.print("Digite um trecho do título: ");
        var trecho = leitura.nextLine();
        relatorioService.buscarPorTitulo(episodios, trecho)
                .ifPresentOrElse(
                        e -> System.out.println("Episódio encontrado! Temporada: " + e.getTemporada()),
                        () -> System.out.println("Episódio não encontrado!")
                );
    }

    private void buscarPorAno() {
        if (episodios.isEmpty()) {
            System.out.println("Nenhuma série carregada. Busque primeiro.");
            return;
        }
        System.out.print("Digite o ano: ");
        var ano = leitura.nextInt();
        leitura.nextLine();
        var dataBusca = LocalDate.of(ano, 1, 1);
        var formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        relatorioService.episodiosDepoisDe(episodios, dataBusca)
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada()
                                + " | Episódio: " + e.getTitulo()
                                + " | Data: " + e.getDataLancamento().format(formatador)));
    }

    private void mostrarEstatisticas() {
        if (episodios.isEmpty()) {
            System.out.println("Nenhuma série carregada. Busque primeiro.");
            return;
        }
        var est = relatorioService.estatisticas(episodios);
        System.out.println("Média: " + est.getAverage());
        System.out.println("Melhor episódio: " + est.getMax());
        System.out.println("Pior episódio: " + est.getMin());
        System.out.println("Quantidade: " + est.getCount());
    }

}
