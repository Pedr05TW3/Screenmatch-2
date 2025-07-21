package br.com.pedro.screenmatch;
import br.com.pedro.screenmatch.model.DadosDaSerie;
import br.com.pedro.screenmatch.servico.ConsumoAPI;
import br.com.pedro.screenmatch.servico.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception{
		var consumoAPI = new ConsumoAPI();
		var json = consumoAPI.obterDados("https://www.omdbapi.com/?apikey=5ec34320&t=cars");
		System.out.println(json);
		ConverteDados conversor = new ConverteDados();
		DadosDaSerie dados = conversor.obterDados(json, DadosDaSerie.class);
		System.out.println(dados);

	}


}
