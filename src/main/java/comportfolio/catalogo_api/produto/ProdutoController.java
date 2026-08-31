package comportfolio.catalogo_api.produto;

import comportfolio.catalogo_api.ProdutoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")

public class ProdutoController {

    private final ProdutoService produtoService;
    public ProdutoController(ProdutoService produtoService){
        this.produtoService = produtoService;
    }

    @GetMapping
    public List<ProdutoResponse> listar(){
        return produtoService.listar();
    }

    @GetMapping("/{id}")
    public ProdutoResponse buscarPorId(@PathVariable("id") Long id){
        return produtoService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<ProdutoResponse> criar(
            @Valid @RequestBody ProdutoRequest request
    ){
        ProdutoResponse resposta = produtoService.criar(request);

        URI localizacao = URI.create("/api/produtos/" + resposta.id());

        return ResponseEntity.created(localizacao).body(resposta);


    }
@PutMapping("/{id}")
    public ProdutoResponse atualizar(
            @PathVariable("id") Long id,
            @Valid @RequestBody ProdutoRequest request
){
        return produtoService.atualizar(id, request);
}
@DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable("id") Long id
){
        produtoService.excluir(id);

        return ResponseEntity.noContent().build();
}
}
