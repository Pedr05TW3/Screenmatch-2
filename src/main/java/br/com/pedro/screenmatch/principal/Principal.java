package br.com.pedro.screenmatch.principal;

import br.com.pedro.screenmatch.model.DadosDaSerie;
import br.com.pedro.screenmatch.model.DadosEpisodio;
import br.com.pedro.screenmatch.model.DadosTemporada;
import br.com.pedro.screenmatch.model.Episodio;
import br.com.pedro.screenmatch.servico.ConsumoAPI;
import br.com.pedro.screenmatch.servico.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner scanner = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?";
    private final String API_KEY = "apikey=5ec34320&t=";

    public void exibeMenu() {
        System.out.println("Digite o nome da série: ");
        var nomeDaSerie = scanner.nextLine();
        var json = consumoAPI.obterDados(ENDERECO + API_KEY + nomeDaSerie.replace(" ", "+"));
        DadosDaSerie dados = conversor.obterDados(json, DadosDaSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i < dados.totalTemporadas(); i++) {
            json = consumoAPI.obterDados(ENDERECO + API_KEY + nomeDaSerie.replace(" ", "+") + "&season=" + i);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);

        /*for (int i = 0; i < dados.totalTemporadas(); i++) {
            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
            for (int j = 0; j < episodiosTemporada.size(); j++) {
                System.out.println(episodiosTemporada.get(j).titulo());
            }
            }*/
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
        List<DadosEpisodio> dadosEpisodios = temporadas.stream().flatMap(t -> t.episodios().stream())
                .toList();
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numeroTemporada(), d))
                ).collect(Collectors.toList());
        episodios.forEach(System.out::println);

        System.out.println("Digite o nome do episódio para busca: ");

        var trechoTitulo = scanner.nextLine();
        Optional<Episodio> episodiobuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();
        if (episodiobuscado.isPresent()){
            System.out.println("Temporada: " + episodiobuscado.get() );
        }else {
            System.out.println("Episódio não encontrado!");
        }

        Map<Integer, Double> avalicoesPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao()> 0.0)
            .collect(Collectors.groupingBy(Episodio::getTemporada,
            Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println(avalicoesPorTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao()> 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        System.out.println("Média Total: " + est.getAverage());
        System.out.println("Avaliação Mínima: " + est.getMin());
        System.out.println("Avaliação Máxima: " + est.getMax());
        System.out.println("Quantidade de episódios: " + est.getCount());
    }
}