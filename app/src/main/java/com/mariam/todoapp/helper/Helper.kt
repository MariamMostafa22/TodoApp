package com.mariam.todoapp.helper

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.cardview.widget.CardView
import com.mariam.todoapp.R
import com.mariam.todoapp.databinding.ChoicesColorBinding
import com.mariam.todoapp.model.ColorsAdapter

object Helper {
    val colors = listOf(
        Color.parseColor("#F48FB1"),
        Color.parseColor("#CDB4DB"),
        Color.parseColor("#91C0F6"),
        Color.parseColor("#BAF2BB"),
        Color.parseColor("#FFD6A5"),
        Color.parseColor("#FFCAD7"),
        Color.parseColor("#E8D7FF"),
        Color.parseColor("#C0FDFF"),
        Color.parseColor("#FDFFB6"),
        0
    )

    fun setColorChoices(context: Context, onColorSelected: (Int) -> Unit): View {
        val binding = ChoicesColorBinding.inflate(LayoutInflater.from(context))
        val customColor = LayoutInflater.from(context).inflate(R.layout.item_color, binding.root, false)
        val colorCard = customColor.findViewById<CardView>(R.id.color_card)
        binding.div.visibility = View.GONE
        binding.customTv.visibility = View.GONE
        customColor.visibility = View.GONE
        val adapter = ColorsAdapter(context, colors) { selectedColor ->
            //Log.d("SelectedColor", "Color: $selectedColor")
            onColorSelected(selectedColor)
            if (!colors.contains(selectedColor)) {
                binding.div.visibility = View.VISIBLE
                binding.customTv.visibility = View.VISIBLE
                colorCard.setCardBackgroundColor(selectedColor)
                customColor.visibility = View.VISIBLE
            } else {
                binding.div.visibility = View.GONE
                binding.customTv.visibility = View.GONE
                customColor.visibility = View.GONE
            }
        }
        binding.colorsGv.adapter = adapter
        binding.root.addView(customColor)
        return binding.root
    }
}