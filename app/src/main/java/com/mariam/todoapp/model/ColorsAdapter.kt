package com.mariam.todoapp.model

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.mariam.todoapp.R

class ColorsAdapter(
    private val context: Context,
    private val colors: List<Int>,
    private val onColorSelected: (Int) -> Unit
): BaseAdapter() {

    private var selectedPosition = 0

    override fun getCount(): Int = colors.size

    override fun getItem(position: Int): Any = colors[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_color, parent, false)
        val colorCard = view.findViewById<CardView>(R.id.color_card)
        val colorChoice = view.findViewById<ImageButton>(R.id.color_choice)

        if(colors[position] == 0) {
            colorCard.setCardBackgroundColor(Color.LTGRAY)
            colorChoice.setImageResource(R.drawable.baseline_palette_24)
        }
        else {
            colorCard.setCardBackgroundColor(colors[position])
            colorChoice.setImageResource(android.R.color.transparent)
        }

        if(position == selectedPosition && colors[position] != 0) {
            colorChoice.setImageResource(R.drawable.baseline_check_24)
        }

        colorChoice.setOnClickListener {
            if(colors[position] == 0) {
                showColorPickerDialog { selectedColor ->
                    onColorSelected(selectedColor)
                }
                selectedPosition = -1
            }
            else {
                onColorSelected(colors[position])
                selectedPosition = position
            }
            notifyDataSetChanged()
        }

        return view
    }

    private fun showColorPickerDialog(onColorPicked: (Int) -> Unit) {
        ColorPickerDialog
            .Builder(context)
            .setTitle("Choose Color")
            .setColorShape(ColorShape.SQAURE)
            .setDefaultColor(colors[0])
            .setColorListener { color, _ ->
                onColorPicked(color)
            }
            .show()
    }
}