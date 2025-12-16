package br.com.psicare.api.services;

import br.com.psicare.api.dto.DadosAtualizacaoUsuario;
import br.com.psicare.api.dto.DadosCadastroUsuario;
import br.com.psicare.api.dto.DadosDetalhamentoUsuario;
import br.com.psicare.api.model.Usuario;
import br.com.psicare.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public DadosDetalhamentoUsuario cadastrar(DadosCadastroUsuario dados) {
        var senhaHash = passwordEncoder.encode(dados.senha());
        var iniciais = dados.nome().substring(0, Math.min(2, dados.nome().length())).toUpperCase();

        var usuario = new Usuario(
                dados.nome(),
                dados.email(),
                senhaHash,
                dados.telefone(),
                iniciais
        );

        repository.save(usuario);

        return new DadosDetalhamentoUsuario(usuario);
    }

    public DadosDetalhamentoUsuario atualizar(DadosAtualizacaoUsuario dados, Usuario usuarioLogado) {
        // Carrega a referência do usuário logado do banco
        var usuario = repository.getReferenceById(usuarioLogado.getId());

        String senhaHash = null;
        if (dados.senha() != null && !dados.senha().isBlank()) {
            senhaHash = passwordEncoder.encode(dados.senha());
        }

        // ATUALIZADO: Passando a foto para a entidade
        usuario.atualizarInformacoes(dados.nome(), dados.telefone(), senhaHash, dados.foto());

        return new DadosDetalhamentoUsuario(usuario);
    }
}