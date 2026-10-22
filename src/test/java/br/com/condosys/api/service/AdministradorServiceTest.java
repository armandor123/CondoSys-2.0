
package br.com.condosys.api.service;

import br.com.condosys.api.domain.enums.PerfilUsuario;
import br.com.condosys.api.domain.model.Administrador;
import br.com.condosys.api.domain.model.Usuario;
import br.com.condosys.api.repository.AdministradorRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Testa as regras de negócio do AdministradorService.
@ExtendWith(MockitoExtension.class)
class AdministradorServiceTest {

    // Simula o acesso ao banco de dados.
    @Mock
    private AdministradorRepository administradorRepository;

    // Simula a consulta de usuários.
    @Mock
    private UsuarioService usuarioService;

    // Simula um usuário do sistema.
    @Mock
    private Usuario usuario;

    // Cria o serviço com as dependências simuladas.
    @InjectMocks
    private AdministradorService administradorService;


    // TESTE 1: Impede cadastrar usuário com perfil incorreto.
    @Test
    void naoPermiteCadastrarUsuarioComPerfilIncorreto() {

        UUID usuarioId = UUID.randomUUID();

        // Simula a busca de um usuário.
        when(usuarioService.buscarPorId(usuarioId))
                .thenReturn(usuario);

        // Define o perfil como PORTEIRO.
        when(usuario.getPerfil())
                .thenReturn(PerfilUsuario.PORTEIRO);

        // Espera uma exceção ao tentar cadastrar.
        assertThrows(
                IllegalArgumentException.class,
                () -> administradorService.cadastrar(
                        usuarioId,
                        LocalDate.now()
                )
        );

        // Garante que nenhum administrador foi salvo.
        verify(administradorRepository, never())
                .save(any(Administrador.class));
    }


    // TESTE 2: Permite cadastrar administrador válido.
    @Test
    void deveCadastrarAdministradorComSucesso() {

        UUID usuarioId = UUID.randomUUID();
        LocalDate dataContratacao = LocalDate.now();

        // Simula um usuário administrador ativo.
        when(usuarioService.buscarPorId(usuarioId))
                .thenReturn(usuario);

        when(usuario.getPerfil())
                .thenReturn(PerfilUsuario.ADMINISTRADOR);

        when(usuario.isAtivo())
                .thenReturn(true);

        // Simula o retorno do objeto salvo.
        when(administradorRepository.save(any(Administrador.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Executa o cadastro.
        Administrador resultado = administradorService.cadastrar(
                usuarioId,
                dataContratacao
        );

        // Verifica o resultado.
        assertNotNull(resultado);
        assertSame(usuario, resultado.getUsuario());
        assertEquals(dataContratacao, resultado.getDataContratacao());

        // Confirma que o Repository salvou o administrador.
        verify(administradorRepository)
                .save(any(Administrador.class));
    }


    // TESTE 3: Impede cadastro duplicado.
    @Test
    void naoPermiteCadastrarAdministradorDuplicado() {

        UUID usuarioId = UUID.randomUUID();

        // Simula um administrador ativo.
        when(usuarioService.buscarPorId(usuarioId))
                .thenReturn(usuario);

        when(usuario.getPerfil())
                .thenReturn(PerfilUsuario.ADMINISTRADOR);

        when(usuario.isAtivo())
                .thenReturn(true);

        // Informa que o administrador já existe.
        when(administradorRepository.existsById(usuarioId))
                .thenReturn(true);

        // Espera uma exceção por duplicidade.
        assertThrows(
                IllegalStateException.class,
                () -> administradorService.cadastrar(
                        usuarioId,
                        LocalDate.now()
                )
        );

        // Impede uma nova gravação.
        verify(administradorRepository, never())
                .save(any(Administrador.class));
    }


    // TESTE 4: Impede cadastro de usuário inativo.
    @Test
    void naoPermiteCadastrarUsuarioInativo() {

        UUID usuarioId = UUID.randomUUID();

        // Simula um usuário administrador.
        when(usuarioService.buscarPorId(usuarioId))
                .thenReturn(usuario);

        when(usuario.getPerfil())
                .thenReturn(PerfilUsuario.ADMINISTRADOR);

        // Define o usuário como inativo.
        when(usuario.isAtivo())
                .thenReturn(false);

        // Espera uma exceção.
        assertThrows(
                IllegalArgumentException.class,
                () -> administradorService.cadastrar(
                        usuarioId,
                        LocalDate.now()
                )
        );

        // Confirma que nada foi salvo.
        verify(administradorRepository, never())
                .save(any(Administrador.class));
    }


    // TESTE 5: Impede data de contratação futura.
    @Test
    void naoPermiteDataContratacaoFutura() {

        UUID usuarioId = UUID.randomUUID();

        // Define a contratação para amanhã.
        LocalDate dataFutura = LocalDate.now().plusDays(1);

        // Simula um usuário administrador ativo.
        when(usuarioService.buscarPorId(usuarioId))
                .thenReturn(usuario);

        when(usuario.getPerfil())
                .thenReturn(PerfilUsuario.ADMINISTRADOR);

        when(usuario.isAtivo())
                .thenReturn(true);

        // Espera uma exceção por data inválida.
        assertThrows(
                IllegalArgumentException.class,
                () -> administradorService.cadastrar(
                        usuarioId,
                        dataFutura
                )
        );

        // Confirma que nada foi salvo.
        verify(administradorRepository, never())
                .save(any(Administrador.class));
    }
}
