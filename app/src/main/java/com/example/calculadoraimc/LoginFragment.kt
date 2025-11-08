package com.example.calculadoraimc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.calculadoraimc.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        setupObservers()
    }
    private fun setupClickListeners() {

        binding.btnEntrar.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val senha = binding.editTextPassword.text.toString()

            if (email.isBlank() || senha.isBlank()) {
                Toast.makeText(context, "Preencha e-mail e senha", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.login(email, senha)
        }

        binding.btnIrParaCadastro.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_cadastroFragment)
        }

        binding.textEsqueceuSenha.setOnClickListener {

            val email = binding.editTextEmail.text.toString().trim()

            viewModel.recuperarSenha(email)
        }
    }
    private fun setupObservers() {

        viewModel.authResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is AuthResult.Success -> {
                    Toast.makeText(context, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_loginFragment_to_calcularFragment)
                }
                is AuthResult.Error -> {
                    Toast.makeText(context, "Erro: ${result.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.statusMessage.observe(viewLifecycleOwner) { message ->

            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}