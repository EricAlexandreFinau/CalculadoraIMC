package com.example.calculadoraimc

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.example.calculadoraimc.databinding.FragmentResultadoBinding
class ResultadoFragment : Fragment() {
    private var _binding: FragmentResultadoBinding? = null
    private val binding get() = _binding!!
    private var imcResult: Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imcResult = it.getFloat("resultado_imc")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultadoBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imcFormatado = String.format("%.1f", imcResult)
        val classificacao = getBmiClassification(imcResult)
        binding.textViewResultadoIMC.text = "seu imc é de: $imcFormatado"
        binding.textViewClassificacao.text = "Classificação: $classificacao"

        binding.btnVoltarAoInicio.setOnClickListener {
            findNavController().navigate(R.id.action_resultadoFragment_to_calcularFragment)
        }

        binding.imageViewTabela.setOnClickListener {
            showImageZoom() // Chama a função que agora usará o PhotoView
        }
    }
    private fun showImageZoom() {

        val dialog = Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen)

        dialog.setContentView(R.layout.dialog_zoom_image)

        dialog.findViewById<View>(R.id.photo_view).setOnClickListener {

            val rootLayout = dialog.findViewById<View>(R.id.photo_view).parent as ViewGroup
            rootLayout.setOnClickListener {
                dialog.dismiss()
            }
        }

        dialog.show()
    }
    private fun getBmiClassification(imc: Float): String {
        return when {
            imc < 18.5 -> "Abaixo do Peso"
            imc < 24.9 -> "Peso Normal"
            imc < 29.9 -> "Sobrepeso"
            imc < 39.9 -> "Obesidade"
            else -> "Obesidade Severa"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}