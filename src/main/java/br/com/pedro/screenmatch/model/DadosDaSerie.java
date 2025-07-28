package br.com.pedro.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosDaSerie(@JsonAlias("Title") String titulo,@JsonAlias("Released") String lancamento,@JsonAlias("imdbRating") Double avaliacao,@JsonAlias("totalSeasons") Integer totalTemporadas) {

}
