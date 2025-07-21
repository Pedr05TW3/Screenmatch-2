package br.com.pedro.screenmatch.servico;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
